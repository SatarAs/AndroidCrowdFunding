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
public class LoginInstrumentedTest {
    @Before
    public void init() {
        ActivityScenario.launch(LoginActivity.class);
    }

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLogin() {
        onView(withId(R.id.email2)).perform(replaceText("titi@aaaa.fr"), closeSoftKeyboard());
        onView(withId(R.id.password2)).perform(replaceText("azertyuiop"), closeSoftKeyboard());
        onView(withId(R.id.login2)).perform(click());
    }
}