package Models;



public class CalendarEvent {
    protected int id;
    protected String stringId;   //id is reserved? check this later
    protected String title;
    protected String location;
    protected int dtStart;
    protected int dtEnd;

    public CalendarEvent(int id, String stringId, String title, String location, int dtStart, int dtEnd){
        this.id = id;
        this.stringId = stringId;
        this.title = title;
        this.location = location;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDtStart() {
        return dtStart;
    }

    public void setDtStart(int dtStart) {
        this.dtStart = dtStart;
    }

    public int getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(int dtEnd) {
        this.dtEnd = dtEnd;
    }

}
