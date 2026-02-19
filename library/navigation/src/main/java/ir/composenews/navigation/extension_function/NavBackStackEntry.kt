@file:Suppress("PackageNaming", "PackageName", "ktlint")

package ir.composenews.navigation.extension_function

import android.os.Parcelable
import androidx.navigation.NavBackStackEntry

inline fun <reified T : Parcelable> NavBackStackEntry.parcelableData(key: String): T? {
    return arguments?.parcelable<T>(key)
}
