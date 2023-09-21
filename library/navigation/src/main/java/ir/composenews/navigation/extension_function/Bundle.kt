@file:Suppress("PackageNaming", "PackageName", "MagicNumber", "AnnotationOnSeparateLine")

package ir.composenews.navigation.extension_function

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else ->
        @Suppress("DEPRECATION")
        getParcelable(key)
            as? T
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else ->
        @Suppress("DEPRECATION")
        getParcelableArrayList(key)
}
