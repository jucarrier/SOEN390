package Models;


import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CalendarEvent {
    protected int id;
    protected String stringId;   //id is reserved? check this later
    protected String title;
    protected String location;
    protected long dtStart;
    protected long dtEnd;

    protected String semester;
    protected int year;
    protected Map<String, Boolean> days;


    public CalendarEvent(int id, String stringId, String title, String location, long dtStart, long dtEnd, String repRule){
        this.id = id;
        this.stringId = stringId;
        this.title = title;
        this.location = location;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;

        if (repRule!=null){
            System.out.println(repRule + " <- this is the rule");

            String third = repRule.split(";")[2];
            System.out.println(third + " <- third");


            days = new Map<String, Boolean>() {
                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean containsKey(@Nullable Object o) {
                    return false;
                }

                @Override
                public boolean containsValue(@Nullable Object o) {
                    return false;
                }

                @Nullable
                @Override
                public Boolean get(@Nullable Object o) {
                    return null;
                }

                @Nullable
                @Override
                public Boolean put(String s, Boolean aBoolean) {
                    return null;
                }

                @Nullable
                @Override
                public Boolean remove(@Nullable Object o) {
                    return null;
                }

                @Override
                public void putAll(@NonNull Map<? extends String, ? extends Boolean> map) {

                }

                @Override
                public void clear() {

                }

                @NonNull
                @Override
                public Set<String> keySet() {
                    return null;
                }

                @NonNull
                @Override
                public Collection<Boolean> values() {
                    return null;
                }

                @NonNull
                @Override
                public Set<Entry<String, Boolean>> entrySet() {
                    return null;
                }
            };


            if(third.contains("SU")) days.put("Sunday", true); else days.put("Sunday", false);
            if(third.contains("MO")) days.put("Monday", true); else days.put("Monday", false);
            if(third.contains("TU")) days.put("Tuesday", true); else days.put("Tuesday", false);
            if(third.contains("WE")) days.put("Wednesday", true); else days.put("Wednesday", false);
            if(third.contains("TH")) days.put("Thursday", true); else days.put("Thursday", false);
            if(third.contains("FR")) days.put("Friday", true); else days.put("Friday", false);
            if(third.contains("SA")) days.put("Saturday", true); else days.put("Saturday", false);





        } else System.out.println("repRule null");
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

}
