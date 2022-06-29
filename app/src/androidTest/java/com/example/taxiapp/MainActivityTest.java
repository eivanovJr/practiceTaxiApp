package com.example.taxiapp;

import static android.service.autofill.Validators.not;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.android.gms.maps.model.LatLng;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {


    private ActivityScenario<MainActivity> activityScenario;



    public static ViewAction setTextInTextView(final String value){
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TextView.class));
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((TextView) view).setText(value);
            }

            @Override
            public String getDescription() {
                return "replace text";
            }
        };
    }


    @Test
    public void onCreate_test_visibility() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
        Espresso.onView(withId(R.id.departure)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.arrival)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.maps_button1)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.maps_button2)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.main_button)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.time)).check(matches(isDisplayed()));
    }


    @Test
    public void map_button_1_opens_map() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

        Espresso.onView(withId(R.id.maps_button1)).perform(click());
        Espresso.onView(withId(R.id.maps)).check(matches(isDisplayed()));
    }

    @Test
    public void map_button_2_opens_map() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

        Espresso.onView(withId(R.id.maps_button2)).perform(click());
        Espresso.onView(withId(R.id.maps)).check(matches(isDisplayed()));
    }


    @Test
    public void main_button_throws_toast_message() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);


        //Espresso.onView(withId(R.id.arrival)).perform(setTextInTextView("Arrival Address"));
        //Espresso.onView(withId(R.id.departure)).perform(setTextInTextView("Departure address"));

        Espresso.onView(withId(R.id.main_button)).perform(click());

        Espresso.onView(withText(R.string.direction_warning)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void main_button_throws_time_warning_toast_message() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

        Espresso.onView(withId(R.id.arrival)).perform(setTextInTextView("Arrival Address"));
        Espresso.onView(withId(R.id.departure)).perform(setTextInTextView("Departure address"));
        Espresso.onView(withId(R.id.time)).perform(replaceText("16"));

        Espresso.onView(withId(R.id.main_button)).perform(click());
        Espresso.onView(withText(R.string.time_bounds)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void main_button_opens_results() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
        Espresso.onView(withId(R.id.arrival)).perform(setTextInTextView("Arrival Address"));
        Espresso.onView(withId(R.id.departure)).perform(setTextInTextView("Departure address"));

        activityScenario.onActivity(activity -> {
            activity.arrivalCoordinates = new LatLng(30.0, 50.0);
            activity.departureCoordinates = new LatLng(35.0, 45.0);
        });

        Espresso.onView(withId(R.id.main_button)).perform(click());
        Espresso.onView(withId(R.id.results)).check(matches(isDisplayed()));
    }
}