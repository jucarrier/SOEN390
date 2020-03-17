package com.example.concordiaguide;

import android.view.KeyEvent;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkCampuses() {
        onView(withId(R.id.sv_location)).perform(typeText("Montreal"), closeSoftKeyboard()).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.sv_location)).perform(typeText("adsfasdfasdf"), closeSoftKeyboard()).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.button2)).perform(click());
    }
}
