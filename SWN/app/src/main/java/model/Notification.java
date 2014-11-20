package model;

/**
 * Created by kumudini on 9/30/14.
 */
public class Notification {
    private int id;
    private String title;
    private String date;
    private NotificationDetails details;
    private boolean read;
    private int image;
    private IssueState status;
    private String resolvedDate;

    public Notification(){

    }

    public Notification(int id, String title, String date, NotificationDetails details, boolean read, int image, IssueState status, String resolvedDate) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.details = details;
        this.read = read;
        this.image = image;
        this.status = status;

        this.resolvedDate = resolvedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IssueState getStatus() {
        return status;
    }

    public void setStatus(IssueState status) {
        this.status = status;
    }

    public String getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(String resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDetails(NotificationDetails details) {
        this.details = details;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public NotificationDetails getDetails() {
        return details;
    }

    public boolean isRead() {
        return read;
    }

    public String getDescription(){
        if(details == null)
            return "No Description Available";
        return details.getShortDescription();
    }

}
