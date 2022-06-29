package com.example.taxiapp;

import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class MapsActivityTest {

    @Test
    public void button_and_map_displayed() {
        ActivityScenario<MapsActivity> activityScenario = ActivityScenario.launch(MapsActivity.class);
        Espresso.onView(withId(R.id.map)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.map_button)).check(matches(isDisplayed()));
    }

    @Test
    public void position_out_spb_shows_dialog() {
        ActivityScenario<MapsActivity> activityScenario = ActivityScenario.launch(MapsActivity.class);
        activityScenario.onActivity(activity -> {
            activity.marker.setPosition(new LatLng(59.797895, 29.702749));
        });
        Espresso.onView(withId(R.id.map_button)).perform(click());
        Espresso.onView(withText(R.string.warning_message)).check(matches(isDisplayed()));
    }

    @Test
    public void position_is_correct_sendback() {
        ActivityScenario<MapsActivity> activityScenario = ActivityScenario.launch(MapsActivity.class);
        activityScenario.onActivity(activity -> {
            activity.marker.setPosition(new LatLng(59.973332, 30.300647));
        });
        Espresso.onView(withId(R.id.map_button)).perform(click());
        assertEquals("DESTROYED", activityScenario.getState().toString());
    }

}