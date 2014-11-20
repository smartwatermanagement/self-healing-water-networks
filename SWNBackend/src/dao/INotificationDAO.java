package dao;

import java.util.List;
import model.Notification;
import model.User;

public interface INotificationDAO {
	
	public List<Notification> findByUser(User user);
	public void deleteNotification(Notification notification);
	public void markNotificationRead(Notification notification);

}
