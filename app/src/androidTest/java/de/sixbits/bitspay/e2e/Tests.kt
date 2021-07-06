package de.sixbits.bitspay.e2e

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.sixbits.bitspay.R
import de.sixbits.bitspay.main.adapters.SearchResultRecyclerAdapter
import de.sixbits.bitspay.main.ui.MainActivity
import de.sixbits.bitspay.utils.submitTextSearchView
import io.americanexpress.busybee.BusyBee
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val RESOURCE = "GLOBAL"

@RunWith(AndroidJUnit4::class)
class Tests {

    private val busyBee = BusyBee.singleton()

    @get:Rule
    val mainScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    // I'm Not using this, but it for backward compatibility with my previous code
    // For now, I'm using Busy Bee to handle these type of work
    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(countingIdlingResource)
    }

    @Test
    fun testMainFlow() {
        busyBee.payAttentionToCategory(BusyBee.Category.NETWORK)

        // Given the app is running

        // When I search for "Dogs"
        onView(withId(R.id.et_search_bar))
            .perform(submitTextSearchView("Dogs"))

        // Then I should see the search result
        onView(withId(R.id.rv_search_result))
            .check(matches(isDisplayed()))

        // When I click the second item from the result
        onView(withId(R.id.rv_search_result))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SearchResultRecyclerAdapter.SearchResultRecyclerViewHolder>(
                    1,
                    click()
                )
            )

        // Then I should see a dialog explaining if I wanted to see details
        onView(withText("Yes")).check(matches(isDisplayed()))

        // When I confirm
        onView(withText("Yes")).perform(click())

        // Then I should move to details activity
        onView(withId(R.id.rv_search_result))
            .check(doesNotExist())

        // When I go back
        Espresso.pressBack()

        // Then I should see the same result from before
        onView(withId(R.id.rv_search_result))
            .check(matches(isDisplayed()))
    }
}