package com.rizrmdhn.core.di

import androidx.room.Room
import com.rizrmdhn.core.BuildConfig
import com.rizrmdhn.core.data.StoryRepository
import com.rizrmdhn.core.data.source.local.LocalDataSource
import com.rizrmdhn.core.data.source.local.preferences.SettingPreferences
import com.rizrmdhn.core.data.source.local.preferences.dataStore
import com.rizrmdhn.core.data.source.local.room.StoryDatabase
import com.rizrmdhn.core.data.source.remote.RemoteDataSource
import com.rizrmdhn.core.data.source.remote.network.ApiService
import com.rizrmdhn.core.domain.repository.IStoryRepository
import com.rizrmdhn.core.utils.AppExecutors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<StoryDatabase>().storyDao() }
    factory { get<StoryDatabase>().remoteKeysDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java, "Story.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val preferencesModule = module {
    single {
        val settingPreferences = SettingPreferences(androidContext().dataStore)
        settingPreferences
    }
}

val networkModule = module {
    single {
        val certificatePinner = CertificatePinner.Builder()
            .add(
                BuildConfig.BASE_URL_HOSTNAME,
                "sha256/qRXOtBLL57LL0c7e8w/vou6FL8GMrasDduMdcQqXeBw= "
            )
            .add(
                BuildConfig.BASE_URL_HOSTNAME,
                "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0="
            )
            .build()

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val token = runBlocking { get<SettingPreferences>().getAuthToken().first() }
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }

        OkHttpClient.Builder()
            .addInterceptor(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                else HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.NONE
                )
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .addInterceptor(authInterceptor)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get(), get(), get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IStoryRepository> {
        StoryRepository(
            get(),
            get(),
        )
    }
}