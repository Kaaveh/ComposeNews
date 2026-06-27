package ir.composenews.ui

import android.Manifest
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun favoriteScreenIsShownWhenFavoriteNavigationItemClicked() {
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Favorite")
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Your favorite list is empty")
            .assertExists()
    }
}
