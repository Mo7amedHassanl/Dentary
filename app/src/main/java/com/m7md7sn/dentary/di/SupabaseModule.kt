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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides
    @Singleton
    fun provideSupabaseClient(
        @ApplicationContext context: Context
    ): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Auth) {
                sessionManager = SharedPreferencesSessionManager(context)
                autoSaveToStorage = true
                autoLoadFromStorage = true
            }
        }
    }

    @Provides
    @Singleton
    fun provideAuth(
        supabaseClient: SupabaseClient
    ): Auth {
        return supabaseClient.auth
    }
}