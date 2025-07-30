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
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return try {
            // Create a trust manager that accepts all certificates (for development only)
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true } // Accept all hostnames (for development)
                .build()
        } catch (e: Exception) {
            // Fallback to default client if SSL configuration fails
            OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

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

            install(Auth) {
                sessionManager = SharedPreferencesSessionManager(context)
                autoSaveToStorage = true
                autoLoadFromStorage = true
            }

            install(Postgrest){

            }
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