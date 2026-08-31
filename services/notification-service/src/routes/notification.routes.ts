import { Router, Request, Response } from 'express';
import { triggerNotification, getNotificationHistory } from '../controllers/notification.controller';

const router = Router();

// 💡 Corrected: Wrapped in an Express middleware callback function
// Health Check Route
router.get('/', (req: Request, res: Response) => {
  res.json({ message: 'Notification Service is running' });
});


router.post('/', triggerNotification);
router.get('/user/:userId', getNotificationHistory);

export default router;
