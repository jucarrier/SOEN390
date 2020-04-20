package com.example.concordiaguide;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
public class ShuttleScheduleTest {

    private Shuttle_schedule schedule;

    @Before
    public void setUp(){
        schedule = Robolectric.buildActivity(Shuttle_schedule.class).create().resume().get();
    }

    @Test
    public void verifyScheduleCreated(){
        assertTrue(schedule.test);
    }
}