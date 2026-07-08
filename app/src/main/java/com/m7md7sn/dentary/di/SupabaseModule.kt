package com.m7md7sn.dentary.di

import android.content.Context
import com.m7md7sn.dentary.BuildConfig
import com.m7md7sn.dentary.data.session.SharedPreferencesSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.annotations.SupabaseInternal
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @OptIn(SupabaseInternal::class)
    @Provides
    @Singleton
    fun provideSupabaseClient(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            // Configure HTTP engine with custom OkHttp client
            httpEngine = OkHttp.create {
                preconfigured = okHttpClient
            }

            // Configure Ktor client timeouts
            httpConfig {
                install(HttpTimeout) {
                    requestTimeoutMillis = 30000
                    connectTimeoutMillis = 30000
                    socketTimeoutMillis = 30000
                }
            }

            install(Auth) {
                sessionManager = SharedPreferencesSessionManager(context)
            }

            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideAuth(
        supabaseClient: SupabaseClient
    ): Auth {
        return supabaseClient.auth
    }

    @Provides
    @Singleton
    fun providePostgrest(
        supabaseClient: SupabaseClient
    ): Postgrest {
        return supabaseClient.postgrest
    }

    @Provides
    @Singleton
    fun provideStorage(
        supabaseClient: SupabaseClient
    ): Storage {
        return supabaseClient.storage
    }
}