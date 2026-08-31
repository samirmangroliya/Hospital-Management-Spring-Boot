import { Schema, model, Document } from 'mongoose';

export interface INotification extends Document {
  senderId: string;    // 👈 Added to track who sent it
  recipient: string;
  type: 'EMAIL' | 'SMS' | 'PUSH';
  status: 'PENDING' | 'SENT' | 'FAILED';
  payload: Record<string, any>;
  createdAt: Date;
}

const notificationSchema = new Schema<INotification>({
  senderId: { type: String, required: true }, // 👈 Enforced in schema validation
  recipient: { type: String, required: true },
  type: { type: String, enum: ['EMAIL', 'SMS', 'PUSH'], required: true },
  status: { type: String, enum: ['PENDING', 'SENT', 'FAILED'], default: 'PENDING' },
  payload: { type: Map, of: Schema.Types.Mixed, required: true },
  createdAt: { type: Date, default: Date.now }
});

export const NotificationModel = model<INotification>('NotificationLog', notificationSchema, 'notification_logs');
