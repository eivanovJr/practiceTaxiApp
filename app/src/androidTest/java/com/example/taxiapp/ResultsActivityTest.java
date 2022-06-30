package com.example.taxiapp;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class ResultsActivityTest {

    static File filesDir = null;
    static Intent intent;
    private static String fakeJSON = "[{\"name\": \"Vasya\", \"price\": 100, \"rideClass\": \"econom\"}, " +
            "{\"name\": \"Anton\", \"price\": 250, \"rideClass\": \"econom\"}, " +
            "{\"name\": \"Tolya\", \"price\": 125, \"rideClass\": \"econom\"}]";
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("API Data", fakeJSON);
        bundle.putString("departure", "dep");
        bundle.putString("arrival", "arr");
        intent.putExtras(bundle);
    }


    @Test
    public void drivers_and_addresses_are_shown() {
        ActivityScenario<ResultsActivity> resultsScenario = ActivityScenario.launch(intent);

        Espresso.onView(withId(R.id.from)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.to)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.max_button)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.min_button)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.all_button)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.file_button)).check(matches(isDisplayed()));
        Espresso.onView(allOf(withId(R.id.driver_info), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        resultsScenario.onActivity(activity -> {
            assertEquals(3, activity.getDrivers().size());
        });
    }

    @Test
    public void min_price_driver_shown() {
        ActivityScenario<ResultsActivity> resultsScenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.max_button)).perform(click());
        Espresso.onView(withId(R.id.driver_info)).check(matches(isDisplayed()));
        Espresso.onView(withText("250")).check(matches(isDisplayed()));
    }

    @Test
    public void max_price_driver_shown() {
        ActivityScenario<ResultsActivity> resultsScenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.min_button)).perform(click());
        Espresso.onView(withId(R.id.driver_info)).check(matches(isDisplayed()));
        Espresso.onView(withText("100")).check(matches(isDisplayed()));
    }

    @Test
    public void file_is_written() {
        ActivityScenario<ResultsActivity> resultsScenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.file_button)).perform(click());
        Espresso.onView(withText(R.string.file_success)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        String fileName = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
        resultsScenario.onActivity(activity -> {
            filesDir = activity.getFilesDir();
        });
        File root = new File(filesDir, "TaxiAppOrders");
        assert root.exists();
        File file = new File(root, fileName);
        assert file.exists();
        assert file.delete();
    }

}