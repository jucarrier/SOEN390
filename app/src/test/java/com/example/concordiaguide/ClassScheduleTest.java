package com.example.concordiaguide;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class ClassScheduleTest extends ClassScheduleActivity {
    private ClassScheduleActivity activity;
    private MainActivity mainActivity;
    private FloatingActionButton toggleNotifications;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(ClassScheduleActivity.class)
                .create()
                .start()
                .resume()
                .visible()
                .get();
    }

    @Test
    public void checkButtonsExist() {
        assertNotNull(activity.findViewById(R.id.buttonToggleNotifications));
        assertNotNull(activity.findViewById(R.id.buttonRefreshCalendar));
        assertNotNull(activity.findViewById(R.id.textViewNotificationsOnOrOff));

    }


    @Test
    public void checkScheduleExists() {
        assertNotNull(schedule.getEvents());
    }

    @Test
    public void testAlarms() {
        activity.startAlarm(1, 1, 1);
        activity.startAlarm(2, 2, 2);
        assertNotNull(activity.activeAlarmIds);
        assert activity.activeAlarmIds.size() == 2;

        activity.cancelAllAlarms();
        assert activity.activeAlarmIds.size() == 0;

    }

    @Test
    public void calendarEventsFound() {
        assertNotNull(activity.findViewById(R.id.classScheduleRecyclerView));
    }

    @Test
    public void testNotificationToggle() {
        assert !activity.notificationsActive || (activity.findViewById(R.id.buttonToggleNotifications).getTag().equals(true));
    }

    @Test
    public void checkPreferenceLoading() {
        activity.savePreference(true);
        activity.loadPreference();
        assert activity.notificationsActive == true;

        activity.savePreference(false);
        activity.loadPreference();
        assert activity.notificationsActive == false;
    }

    @Test
    public void checkEventsRead() {
        activity.readEvents();
        System.out.println(activity.eventsRead);
        assert activity.eventsRead.equals("eventsRead");
    }

    @Test
    public void testToggleNotifications() {
        FloatingActionButton toggle = activity.findViewById(R.id.buttonToggleNotifications);
        System.out.println(toggle.performClick());
    }

}