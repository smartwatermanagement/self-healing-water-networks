package action.struts2.rest.example;

import java.util.Collection;
import java.util.Map;

import model.Aggregation;
import model.Notification;
import model.User;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.ModelDriven;

import dao.impl.AggregationDAO;
import dao.impl.NotificationDAO;

public class AggregationController implements ModelDriven<Object>{

	private static final long serialVersionUID = 89268916175477696L;
	private Aggregation model;
	private int id;
	private Collection<Aggregation> list;

	/*public HttpHeaders create() {
		MessageService.save(model);
		return new DefaultHttpHeaders("create");
	}*/
	
	/*public HttpHeaders destroy() {
		return new DefaultHttpHeaders("destroy");
	}
*/
	public HttpHeaders show() {
		return new DefaultHttpHeaders("show").disableCaching();
	}

	/*public HttpHeaders update() {
		MessageService.save(model);
		return new DefaultHttpHeaders("update");
	}*/

	public HttpHeaders index() {
		list = (new AggregationDAO()).findAllTopLevelAggregations();
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
			model = (new AggregationDAO()).findById(id);
			list = null;
		}
		this.id = id;
	}
	
}
