package io.github.jan.einkaufszettel.common.di

import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.host
import io.github.jan.supabase.gotrue.scheme

actual fun GoTrue.Config.setupPlatform() {
    scheme = "shoppinglist"
    host = "login"
}