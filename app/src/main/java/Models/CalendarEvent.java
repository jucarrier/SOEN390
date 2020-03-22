package Models;



public class CalendarEvent {
    protected String _id;
    protected String title;
    protected String location;
    protected int dtStart;
    protected int dtEnd;

    public CalendarEvent(String _id, String title, String location, int dtStart, int dtEnd){
        this._id = _id;
        this.title = title;
        this.location = location;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
