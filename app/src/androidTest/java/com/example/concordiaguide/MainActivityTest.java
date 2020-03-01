package com.example.concordiaguide;

import android.view.KeyEvent;

import androidx.test.espresso.ViewInteraction;
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
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.concordiaguide.CheckCampusNavigationSGW.childAtPosition;
import static org.hamcrest.Matchers.allOf;

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


    }

    @Test
    public void checkSidebarOpens() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.map),
                                                3)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction imageView = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.navigation_header_container),
                                0),
                        0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
    }

    @Test
    public void checkLocateUser() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.addressHere), withText("addressHere"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.map),
                                        4),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("addressHere")));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button2), withText("Find me"),
                        childAtPosition(
                                allOf(withId(R.id.map),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.addressHere), withText("Not on campus"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.map),
                                        4),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Not on campus")));
    }

    @Test
    public void checkCampusNavigationSGW() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.map),
                                                3)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_viewer),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.building_name), withText("Library Building"),
                        childAtPosition(
                                allOf(withId(R.id.building_list_item_parent_layout),
                                        childAtPosition(
                                                withId(R.id.building_list_sgw),
                                                4)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Library Building")));

    }

    @Test
    public void checkCampusNavigationLoyola() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.map),
                                                3)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_viewer),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.campus_switch),
                        childAtPosition(
                                allOf(withId(R.id.building_list_toolbar),
                                        childAtPosition(
                                                withId(R.id.toolbar),
                                                0)),
                                1),
                        isDisplayed()));
        switch_.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.building_name), withText("Psychology Building"),
                        childAtPosition(
                                allOf(withId(R.id.building_list_item_parent_layout),
                                        childAtPosition(
                                                withId(R.id.building_list_loyola),
                                                3)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Psychology Building")));
    }
}
