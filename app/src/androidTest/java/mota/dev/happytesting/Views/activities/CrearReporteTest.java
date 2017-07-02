package mota.dev.happytesting.Views.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import mota.dev.happytesting.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CrearReporteTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void crearReporteTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editText), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editText), isDisplayed()));
        appCompatEditText2.perform(replaceText("mota69"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editText2), isDisplayed()));
        appCompatEditText3.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button), withText("Login"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Crear Aplicacion"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editText3), isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editText3), isDisplayed()));
        appCompatEditText5.perform(replaceText("Aplicacion de Prueba"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button2), withText("Crear"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Aplicacion de Prueba"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.item_app),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Aplicacion de Prueba")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.apps_list),
                        withParent(withId(R.id.swipeContainer)),
                        isDisplayed()));
       // recyclerView.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction floatingActionButton = onView(
                allOf(withClassName(is("com.github.clans.fab.FloatingActionButton")),
                        withParent(withId(R.id.material_design_android_floating_action_menu)),
                        isDisplayed()));
        floatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(275);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.material_design_floating_action_menu_item3),
                        withParent(withId(R.id.material_design_android_floating_action_menu)),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.input), isDisplayed()));
        appCompatAutoCompleteTextView.perform(click());

        ViewInteraction appCompatAutoCompleteTextView2 = onView(
                allOf(withId(R.id.input), isDisplayed()));
        appCompatAutoCompleteTextView2.perform(replaceText("reporte de prueba"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("reporte de prueba"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.item_app),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("reporte de prueba")));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.reports_list),
                        withParent(withId(R.id.swipeContainer)),
                        isDisplayed()));
       // recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction floatingActionButton3 = onView(
                allOf(withClassName(is("com.github.clans.fab.FloatingActionButton")),
                        withParent(withId(R.id.material_design_android_floating_action_menu)),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(330);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.material_design_floating_action_menu_item4),
                        withParent(withId(R.id.material_design_android_floating_action_menu)),
                        isDisplayed()));
        floatingActionButton4.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("Si"), isDisplayed()));
        appCompatButton4.perform(click());

    }

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
}
