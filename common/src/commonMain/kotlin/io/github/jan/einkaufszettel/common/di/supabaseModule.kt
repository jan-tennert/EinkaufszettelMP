package io.github.jan.einkaufszettel.common.di

import com.russhwolf.settings.ObservableSettings
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.GoTrueConfig
import io.github.jan.supabase.gotrue.SettingsSessionManager
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.createChannel
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.Storage
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

@OptIn(SupabaseInternal::class)
val supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = "https://arnyfaeuskyqfxkvotgj.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFybnlmYWV1c2t5cWZ4a3ZvdGdqIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NTMwMzkxMTEsImV4cCI6MTk2ODYxNTExMX0.ItmL8lfnOL9oy7CEX9N6TnYt10VVhk-KTlwley4aq1M"
        ) {
            install(GoTrue) {
                sessionManager = SettingsSessionManager(get<ObservableSettings>())
                setupPlatform()
            }
            install(Storage)
            install(Postgrest) {
                propertyConversionMethod = PropertyConversionMethod.SERIAL_NAME
            }
            install(Realtime) {
                heartbeatInterval = 30.seconds
            }
        }
    }
    single {
         get<SupabaseClient>().realtime.createChannel("shopping_list")
    }
}

expect fun GoTrueConfig.setupPlatform()