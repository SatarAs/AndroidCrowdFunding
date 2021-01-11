package com.devforxkill.androidcrowdfunding;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

@LargeTest
public class RegisterInstrumentedTest {
    @Before
    public void init() {
        ActivityScenario.launch(RegisterActivity.class);
    }

    @Rule
    public ActivityScenarioRule<RegisterActivity> registerActivityActivityScenarioRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void testLogin() {
        onView(withId(R.id.email)).perform(replaceText("titi@aaaa.fr"), closeSoftKeyboard());
        onView(withId(R.id.pseudo)).perform(replaceText("titi76"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(replaceText("azertyuiop"), closeSoftKeyboard());
        onView(withId(R.id.birthdate)).perform(replaceText("12/12/1970"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
    }
}