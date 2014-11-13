package action.rest;

import model.Aggregation;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.ModelDriven;

import dao.impl.AggregationDAO;

public class AggregationController implements ModelDriven<Object>{

	private static final long serialVersionUID = 89268916175477696L;
	private Aggregation model;
	private int id;

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
		model = (new AggregationDAO()).findTopLevelAggregation();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public Object getModel() {
		return model;
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		if (id != null) {
			model = (new AggregationDAO()).findByIdEager(id);
		}
		this.id = id;
	}
	
}
