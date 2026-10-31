package ir.composenews.tv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ir.composenews.apptv.ui.ComposeNewsTvTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNewsTvTheme {
                ComposeNewsTvApp()
            }
        }
    }
}
