package ir.composenews.baselineprofile

import android.Manifest
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class generates a basic startup baseline profile for the target package.
 *
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the [baseline profile documentation](https://d.android.com/topic/performance/baselineprofiles)
 * for more information.
 *
 * You can run the generator with the "Generate Baseline Profile" run configuration in Android Studio or
 * the equivalent `generateBaselineProfile` gradle task:
 * ```
 * ./gradlew :app:generateReleaseBaselineProfile
 * ```
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 *
 * Check [documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
 * for more information about available instrumentation arguments.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 *
 * When using this class to generate a baseline profile, only API 33+ or rooted API 28+ are supported.
 *
 * The minimum required version of androidx.benchmark to generate a baseline profile is 1.2.0.
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        // Grant notification permission
        InstrumentationRegistry
            .getInstrumentation()
            .uiAutomation
            .grantRuntimePermission(
                "ir.composenews",
                Manifest.permission.POST_NOTIFICATIONS
            )


        // The application id for the running build variant is read from the instrumentation arguments.
        rule.collect(
            packageName = "ir.composenews",

            // See: https://d.android.com/topic/performance/baselineprofiles/dex-layout-optimizations
            includeInStartupProfile = true
        ) {
            // This block defines the app's critical user journey. Here we are interested in
            // optimizing for app startup. But you can also navigate and scroll through your most important UI.

            // Start default activity for your app
            pressHome()
            startActivityAndWait()

            check(
                device.wait(
                    Until.hasObject(By.text("Markets")),
                    10_000
                )
            ) {
                "Markets screen was not displayed"
            }

            scrollScreenJourney()
            favoriteScreenJourney()
        }
    }

    private fun MacrobenchmarkScope.favoriteScreenJourney() {
        device.findObject(By.text("Favorite"))?.click()
        device.wait(Until.hasObject(By.text("Your favorite list is empty")), 5_000)
    }

    private fun MacrobenchmarkScope.scrollScreenJourney() {
        val scrollable = device.wait(
            Until.findObject(By.scrollable(true)),
            15_000
        )

        checkNotNull(scrollable) {
            "Market list didn't become scrollable. Check loading/network/error state."
        }

        // Set gesture margin to avoid triggering gesture navigation.
        scrollable.setGestureMargin(device.displayWidth / 5)
        scrollable.fling(Direction.DOWN)
        device.waitForIdle()
    }
}