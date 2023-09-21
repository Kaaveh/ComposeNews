package ir.composenews.designsystem.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ThemePreviews

@Preview(name = "Phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "Foldable", device = "spec:width=673.5dp,height=841dp,dpi=480")
@Preview(name = "Tablet", device = "spec:width=1280dp,height=800dp,dpi=480")
@Preview(name = "Desktop", device = "spec:width=1920dp,height=1080dp,dpi=480")
annotation class DevicesPreviews
