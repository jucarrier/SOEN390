package com.example.concordiaguide;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.wait(Until.hasObject(By.pkg(getLauncherPackageName()).depth(0)), 1000);
        UiObject2 acceptLocation = mDevice.findObject(By.text("Allow only while using the app"));
        if (acceptLocation != null) {
            acceptLocation.click();
        }
//        try {
//            mDevice.wait(1000);
//        } catch (InterruptedException e) {
//            System.out.println("Patience during tests");
//        }
    }

    @Test
    public void validateLocateButton() {
        UiObject2 locateButton = mDevice.findObject(By.res("com.example.concordiaguide:id/locateButton"));
        assertNotNull(locateButton);
        locateButton.click();
    }

    @Test void validateNavigateButton() {
        UiObject2 navigateToCampus = mDevice.findObject(By.text("Navigate to Campus"));
        assertNotNull(navigateToCampus);
    }

//    @Test void validateSearch() {
//        UiObject2 searchBar = mDevice.findObject(By.text("Search"));
//        searchBar.click();
//        searchBar.setText("Hall building");
//    }

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

}
