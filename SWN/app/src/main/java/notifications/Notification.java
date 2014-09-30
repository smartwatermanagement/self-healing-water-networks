package notifications;

/**
 * Created by kumudini on 9/30/14.
 */
public class Notification {
    private String title;
    private String date;
    private String description;
    private boolean read;
    private int image;



    public Notification(String title, String date, String description, boolean read, int image) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.read = read;
        this.image = image;

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

    public void setDescription(String description) {
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public boolean isRead() {
        return read;
    }
}
