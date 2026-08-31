import './instrumentation'; // Must be imported first for tracing hooks
import express from 'express';
import { connectDB } from './config/db';
import notificationRoutes from './routes/notification.routes';
import { Eureka } from 'eureka-js-client';
import { connectKafka } from './config/Kafka';

// Initialize Kafka consumer connection

const app = express();
const PORT = parseInt(process.env.PORT || '8085', 10);

app.use(express.json());
app.use('/api/notifications', notificationRoutes);

// Eureka Service Discovery Registration
const eurekaClient = new Eureka({
  instance: {
    app: 'NOTIFICATION-SERVICE',
    hostName: 'hospital-notification-service',
    ipAddr: 'hospital-notification-service',
    port: { '$': PORT, '@enabled': true },
    vipAddress: 'notification-service',
    dataCenterInfo: { '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo', name: 'MyOwn' },
  },
  eureka: {
    // Dynamically parses your injected eureka connection string format
    host: process.env.EUREKA_URL ? new URL(process.env.EUREKA_URL).hostname : 'localhost',
    port: process.env.EUREKA_URL ? parseInt(new URL(process.env.EUREKA_URL).port, 10) : 8761,
    servicePath: '/eureka/apps/',
  },
});

// Replace the eurekaClient.start block with this:
eurekaClient.start((error: any) => {
  if (error) {
    console.error('❌ Eureka registration failed:', error);
  } else {
    console.log('🎯 Registered with Eureka server successfully.');
  }
});


connectDB().then(() => {
  app.listen(PORT, () => {
    console.log(`🚀 Notification Service running on port ${PORT}`);
    
    // Register application inside Spring Cloud dashboard
    eurekaClient.start((error: any) => {
      if (error) console.error('❌ Eureka registration failed:', error);
      else console.log('🎯 Registered with Eureka server successfully.');
    });
  });
});
