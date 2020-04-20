package com.example.concordiaguide;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ClassScheduleActivityTest {

    @Rule
    public ActivityTestRule<ClassScheduleActivity> mActivityTestRule = new ActivityTestRule<>(ClassScheduleActivity.class, true, false);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.READ_CALENDAR");

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    //method used to get text from textviews - this is needed to test whether the notifications have been toggled properly
    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = {null};
        onView(matcher).perform(new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    @Test
    public void checkButtonsExist() {
        mActivityTestRule.launchActivity(null);

        onView(withId(R.id.buttonToggleNotifications)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonRefreshCalendar)).check(matches(isDisplayed()));
    }

    @Test
    public void toggleNotificationsTest() {
        mActivityTestRule.launchActivity(null);

        //get the text before the toggle button is clicked
        String a = getText(withId(R.id.textViewNotificationsOnOrOff));
        System.out.println(a);

        //assert that the text is one of the two states of the toggle button
        assert a.equals("Notifications are ON") || a.equals("Notifications are OFF");

        //toggle notifications
        onView(withId(R.id.buttonToggleNotifications)).perform(click());

        //get new text after toggling
        String b = getText(withId(R.id.textViewNotificationsOnOrOff));

        //assert that the new text is different from the pre-toggle text, i.e. the text has successfully been changed
        if (a.equals("Notifications are ON")) assert b.equals("Notifications are OFF");
        else assert b.equals("Notifications are ON");

        //add another click to make sure cancelAllAlarms has been tested (in the event that alarms were toggled off when the test began)
        onView(withId(R.id.buttonToggleNotifications)).perform(click());
    }
}
