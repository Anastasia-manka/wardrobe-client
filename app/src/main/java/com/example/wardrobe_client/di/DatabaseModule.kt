package com.example.wardrobe_client.di

import android.content.Context
import androidx.room.Room
import com.example.wardrobe_client.data.local.WardrobeDatabase
import com.example.wardrobe_client.data.local.dao.ClothingItemDao
import com.example.wardrobe_client.data.local.dao.OutfitDao
import com.example.wardrobe_client.data.local.dao.ReferencesDao
import com.example.wardrobe_client.data.local.dao.TripDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WardrobeDatabase =
        Room.databaseBuilder(
            context,
            WardrobeDatabase::class.java,
            "wardrobe_database"
        ).build()

    @Provides
    fun provideClothingItemDao(db: WardrobeDatabase): ClothingItemDao = db.clothingItemDao()

    @Provides
    fun provideOutfitDao(db: WardrobeDatabase): OutfitDao = db.outfitDao()

    @Provides
    fun provideTripDao(db: WardrobeDatabase): TripDao = db.tripDao()

    @Provides
    fun provideReferencesDao(db: WardrobeDatabase): ReferencesDao = db.referencesDao()
}