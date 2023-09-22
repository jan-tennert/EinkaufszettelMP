package io.github.jan.einkaufszettel.android

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.request.CachePolicy
import io.github.jan.einkaufszettel.common.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
            androidLogger()
        }
    }

    override fun newImageLoader() = ImageLoader.Builder(this)
        .diskCache(DiskCache.Builder()
            .directory(cacheDir.resolve("image_cache")) //using files as cache directory because it is not cleared on app update
            .maxSizeBytes(1024 * 1024 * 100) // 100MB)
            .build()
        )
        .respectCacheHeaders(false)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()

}