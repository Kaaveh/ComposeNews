package ir.composenews.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun marketListScreen_DisplayedCorrectly() {
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithText("BTC").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("BTC").performClick()
        composeTestRule.onNodeWithText("Market Data").assertExists()
    }
}
