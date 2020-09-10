package poc.sambitm.videoapp.di

import android.content.Context
import org.koin.dsl.module
import poc.sambitm.videoapp.utils.PermissionUtils

val utilityModule = module {
    single { permissionUtils(context = get()) }
}

fun permissionUtils(context: Context): PermissionUtils {
    return PermissionUtils(context)
}