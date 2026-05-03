package com.example.wardrobe_client.di

import com.example.wardrobe_client.data.remote.api.AuthApi
import com.example.wardrobe_client.data.remote.api.ClothingItemApi
import com.example.wardrobe_client.data.remote.api.OutfitApi
import com.example.wardrobe_client.data.remote.api.ProfileApi
import com.example.wardrobe_client.data.remote.api.ReferenceApi
import com.example.wardrobe_client.data.remote.api.TripApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideReferenceApi(retrofit: Retrofit): ReferenceApi =
        retrofit.create(ReferenceApi::class.java)

    @Provides
    @Singleton
    fun provideClothingItemApi(retrofit: Retrofit): ClothingItemApi =
        retrofit.create(ClothingItemApi::class.java)

    @Provides
    @Singleton
    fun provideOutfitApi(retrofit: Retrofit): OutfitApi =
        retrofit.create(OutfitApi::class.java)

    @Provides
    @Singleton
    fun provideTripApi(retrofit: Retrofit): TripApi =
        retrofit.create(TripApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi =
        retrofit.create(ProfileApi::class.java)
}