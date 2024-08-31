package dev.nordix.resourcesharing

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.nordix.core.annotations.OnApplicationCreated
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var handlers: Set<@JvmSuppressWildcards OnApplicationCreated>

    override fun onCreate() {
        super.onCreate()
        handlers.forEach { it.handler(this) }
    }

}
