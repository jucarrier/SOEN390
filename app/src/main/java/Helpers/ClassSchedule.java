package Helpers;

import java.util.ArrayList;

import Models.CalendarEvent;

public class ClassSchedule {
    //TODO: add a save as JSON method
    //TODO: add a load from file method
    //TODO: add a read from calendar method
    protected ArrayList<CalendarEvent> events;
    protected static String[] validClasses = new String[] {"soen", "engr", "comp", "math", "lecture"};

    public ClassSchedule(ArrayList<CalendarEvent> events) {
        this.events = events;
    }

    //TODO: add better duplicated detection - need a .equals method for calendar events?
    public void addEvent(CalendarEvent event){
        if(events.contains(event)) System.out.println("Duplicate event");
        else events.add(event);
    }

    public ArrayList<CalendarEvent> getEvents(){
        return this.events;
    }

    public static String[] getValidClasses() {
        return validClasses;
    }
}
