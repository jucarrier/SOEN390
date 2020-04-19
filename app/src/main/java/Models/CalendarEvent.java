package Models;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarEvent {
    protected int id;
    protected String stringId;   //id is reserved? check this later
    protected String title;
    protected String location;
    protected long dtStart;
    protected long dtEnd;
    protected Date startDate;
    protected Date endDate;
    protected String rRule;

    protected String semester;
    protected int year;
    protected HashMap<String, Boolean> days;


    public CalendarEvent(int id, String stringId, String title, String location, long dtStart, long dtEnd, String repRule) {
        this.id = id;
        this.stringId = stringId;

        this.title = title;
        this.location = location;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;

        this.startDate = new Date(dtStart);
        this.endDate = new Date(dtEnd);
        this.rRule = repRule;

        if (repRule != null) {
            System.out.println(repRule + " <- this is the rule");

            String third = "";

            String[] test = repRule.split(";");
            for (String s : test) {
                if (s.contains("BYDAY")) {
                    System.out.println(s + " <- byday check");
                    third = s;
                }
            }


            System.out.println(third + " <- third");


            days = new HashMap<>();


            days.put("Sunday", third.contains("SU"));
            days.put("Monday", third.contains("MO"));
            days.put("Tuesday", third.contains("TU"));
            days.put("Wednesday", third.contains("WE"));
            days.put("Thursday", third.contains("TH"));
            days.put("Friday", third.contains("FR"));
            days.put("Saturday", third.contains("SA"));

        } else System.out.println("repRule null");
    }

    public Date getStartDate() {
        return startDate;
    }

    public Map<String, Boolean> getDays() {
        return days;
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


    public long getDtStart() {
        return dtStart;
    }

    public void setDtStart(int dtStart) {
        this.dtStart = dtStart;
    }


    public long getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(int dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getrRule() {
        return rRule;
    }
}