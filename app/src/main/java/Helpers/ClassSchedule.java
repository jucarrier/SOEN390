package Helpers;

import java.util.ArrayList;
import java.util.HashMap;

import Models.CalendarEvent;

public class ClassSchedule {

    //TODO: add all concordia class codes here
    protected static String[] validClasses = new String[]{"soen", "engr", "comp", "math", "lecture", "tutorial"};
    protected static HashMap<String, Long> importantDates = new HashMap<>();

    static {
        importantDates.put("startDate", 1577768400000L);
        importantDates.put("winter2020start", 1577854800000L);  //jan1
        importantDates.put("winter2020end", 1586923200000L);    //april15
        importantDates.put("summer2020start", 1588305600000L);  //may1
        importantDates.put("summer2020end", 1598932800000L);    //sept1
    }

    protected ArrayList<CalendarEvent> events;

    public ClassSchedule(ArrayList<CalendarEvent> events) {
        this.events = events;
    }

    public static String[] getValidClasses() {
        return validClasses;
    }

    public static HashMap<String, Long> getImportantDates() {
        return importantDates;
    }

    public void addEvent(CalendarEvent event) {
        if (events.contains(event)) System.out.println("Duplicate event");
        else events.add(event);
    }

    public ArrayList<CalendarEvent> getEvents() {
        return this.events;
    }
}
