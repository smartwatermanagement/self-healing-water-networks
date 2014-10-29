package login;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware{
    private String username;
    private String password;
    private Map<String, Object> sessionMap;
 
    public String execute() {
        if (this.username.equals("admin")
                && this.password.equals("admin123")) {
        	sessionMap.put("username", username);
            return "success";
        } else {
            return "error";
        }
    }
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
		
		
	}

    
}