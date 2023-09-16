@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.navigation.extension_function

import androidx.navigation.NavBackStackEntry

fun <T> NavBackStackEntry.parcelableData(key: String): T? {
    return arguments?.parcelable(key) as? T
}
