package Models;


import java.util.HashMap;

public class CalendarEventDisplayCard {
    private int imageResource;
    private String title;
    private String description;
    private HashMap<String, Boolean> days;

    public CalendarEventDisplayCard(int imageResource, String title, String description, HashMap<String, Boolean> days){
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
        this.days = days;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, Boolean> getDays() {
        return days;
    }
}
