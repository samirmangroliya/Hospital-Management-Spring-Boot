import { Request, Response } from 'express';
import { NotificationModel } from '../models/notification.model';

export const triggerNotification = async (req: Request, res: Response): Promise<void> => {
  try {
    const { senderId, recipient, type, payload } = req.body; // 👈 Extract senderId

    // Create record with sender audit trailing
    const notification = new NotificationModel({ 
      senderId, 
      recipient, 
      type, 
      payload 
    });

    notification.status = 'SENT';

    await notification.save();
    res.status(201).json({ success: true, data: notification });
  } catch (error: any) {
    res.status(500).json({ success: false, error: error.message });
  }
};


// Add a GET endpoint so users/services can query their notification history
export const getNotificationHistory = async (req: Request, res: Response): Promise<void> => {
  try {
    const { userId } = req.params;
    const logs = await NotificationModel.find({ senderId: userId }).sort({ createdAt: -1 });
    res.status(200).json({ success: true, data: logs });
  } catch (error: any) {
    res.status(500).json({ success: false, error: error.message });
  }
};
