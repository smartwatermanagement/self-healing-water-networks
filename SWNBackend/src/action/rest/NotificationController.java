package action.rest;

import java.util.Collection;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import model.Notification;
import model.User;

import com.opensymphony.xwork2.ModelDriven;

import dao.impl.NotificationDAO;

public class NotificationController implements ModelDriven<Object>, SessionAware{

	private static final long serialVersionUID = 89268916175477696L;
	private Notification model;
	private int id;
	private Collection<Notification> list;
	private Map<String, Object> sessionsMap;

	/*public HttpHeaders create() {
		MessageService.save(model);
		return new DefaultHttpHeaders("create");
	}
*/
	public HttpHeaders destroy() {
		(new NotificationDAO()).deleteNotification(model);
		return new DefaultHttpHeaders("destroy");
	}

	public HttpHeaders show() {
		return new DefaultHttpHeaders("show").disableCaching();
	}

	/*public HttpHeaders update() {
		MessageService.save(model);
		return new DefaultHttpHeaders("update");
	}*/

	public HttpHeaders index() {
		list = (new NotificationDAO()).findByUser((User)sessionsMap.get("user"));
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public Object getModel() {
		return (list != null ? list : model);
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		if (id != null) {
			this.list = (new NotificationDAO()).findByUser((User)sessionsMap.get("user"));
			for(Notification i: list){
				if(i.getId() == id)
					model = i;
			}
		}
		list = null;
		this.id = id;
	}

	public void setSession(Map<String, Object> arg0) {
		sessionsMap = arg0;
	}
}
