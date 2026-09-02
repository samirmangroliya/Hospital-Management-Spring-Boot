import { Kafka, Consumer } from 'kafkajs';
import { NotificationModel } from '../models/notification.model';

const kafkaBroker = 'kafka:9092';

const kafka = new Kafka({
  clientId: 'notification-service',
  brokers: [kafkaBroker],
});

export const consumer: Consumer = kafka.consumer({ groupId: 'notification-group' });

export const connectKafka = async (): Promise<void> => {
  try {
    await consumer.connect();
    console.log('📦 Kafka Consumer connected successfully.');

    // Subscribe to notification event topics
    await consumer.subscribe({ topic: 'appointment-saga-topic', fromBeginning: false });

    // Start listening for message streams
    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        try {
          if (!message.value) return;
          
          // Parse the raw payload sent from your other microservices
          const eventData = JSON.parse(message.value.toString());
          console.log(`✉️ Received Kafka event from topic [${topic}]:`, eventData);

          const { senderId, recipient, type, payload } = eventData;

          // Hydrate and save inside MongoDB transaction log layers
          const notification = new NotificationModel({
            senderId: senderId || 'SYSTEM',
            recipient,
            type,
            payload,
            status: 'SENT' // Mark as sent after dispatching to external APIs
          });

          await notification.save();
          console.log(`✅ Asynchronously processed & logged Kafka alert ID: ${notification._id}`);
        } catch (err) {
          console.error('❌ Failed processing specific Kafka message payload:', err);
        }
      },
    });
  } catch (error) {
    console.error('❌ Kafka connection runtime error:', error);
  }
};
