package model;

/**
 * Created by kumudini on 9/30/14.
 */
public class Notification {
    private String title;
    private String date;
    private NotificationDetails details;
    private boolean read;
    private int image;
    private boolean resolved;
    private String resolvedDate;

    public Notification(String title, String date, NotificationDetails details, boolean read, int image, boolean resolved, String resolvedDate) {
        this.title = title;
        this.date = date;
        this.details = details;
        this.read = read;
        this.image = image;
        this.resolved = resolved;
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
        return details.getShortDescription();
    }

}
