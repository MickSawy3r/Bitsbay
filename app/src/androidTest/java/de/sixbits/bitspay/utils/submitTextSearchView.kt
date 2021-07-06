package de.sixbits.bitspay.utils

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf


class submitTextSearchView(private val text: String) :
    ViewAction {
    override fun getConstraints(): Matcher<View> {
        return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
    }

    override fun getDescription(): String {
        return "Change view text"
    }

    override fun perform(uiController: UiController, view: View) {
        (view as SearchView).setQuery(text, true)
    }
}