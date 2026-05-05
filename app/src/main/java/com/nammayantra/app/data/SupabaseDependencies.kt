package com.nammayantra.app.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import com.nammayantra.app.BuildConfig
import kotlinx.serialization.json.Json
import io.github.jan.supabase.serializer.KotlinXSerializer

object SupabaseDependencies {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            defaultSerializer = KotlinXSerializer(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
            install(Postgrest)
            install(Auth)
            install(Realtime)
        }
    }
}