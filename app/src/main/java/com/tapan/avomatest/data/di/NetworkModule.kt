package com.tapan.avomatest.data.di

import android.app.Application
import com.tapan.avomatest.BuildConfig
import com.tapan.avomatest.GlobalVar
import com.tapan.avomatest.data.base.TokenInterceptor
import com.tapan.avomatest.data.exception.StringProvider
import com.tapan.avomatest.data.exception.StringProviderImpl
import com.tapan.avomatest.data.utils.RssConverterFactory
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        converterFactory: RssConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GlobalVar.BASE_URL)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        //custom type adapter shall be added if required
        return Gson()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactor(): RssConverterFactory {
        return RssConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        interceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .addInterceptor(interceptor)
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideCache(context: Application): Cache {
        val cacheSize: Long = 10 * 1024L * 1024L //10 MB
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideStringProvider(context: Application): StringProvider {
        return StringProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }


}