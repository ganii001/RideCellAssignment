package com.example.ridecellassignment.di

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.ridecellassignment.BuildConfig
import com.example.ridecellassignment.ConstantClass
import com.example.ridecellassignment.MyPreferenses
import com.example.ridecellassignment.Utils
import com.example.ridecellassignment.network.api.apihelper.ApiHelper
import com.example.ridecellassignment.network.api.apihelperimpl.ApiHelperImpl
import com.example.ridecellassignment.network.api.apiservice.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize: Long = 10 * 1024 * 1024 //10MB
        return Cache(File(application.cacheDir, "http-cache"), cacheSize)
    }

    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        var logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        // set your desired log level
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return logging
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache, context: Context): OkHttpClient {
        val httpClient = OkHttpClient.Builder().cache(cache)
        try {
            httpClient.addInterceptor(Interceptor { chain ->
                val original: Request = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                    //     .header("Authorization", finalToken)
                    .header("content-type", "application/json")
                    .method(original.method, original.body)

                val token: String =
                    MyPreferenses(context).getString(ConstantClass.TOKEN, "")!!

                if (token.isNotEmpty()) {
                    requestBuilder.header("Authorization", token)
                }
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            })

            /*val httpClient: OkHttpClient = OkHttpClient.Builder().cache(cache)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            httpClient.connectTimeout(5, TimeUnit.MINUTES)
            httpClient.readTimeout(5, TimeUnit.MINUTES)
            return httpClient.build()*/

        } catch (e: Exception) {
            e.printStackTrace()
            Utils.showToast(context, "Network Issue...!")
        }
        return httpClient
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, context: Context, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://blooming-stream-45371.herokuapp.com/")
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}