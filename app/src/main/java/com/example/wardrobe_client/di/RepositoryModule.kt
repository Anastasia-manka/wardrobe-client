package com.example.wardrobe_client.di

import com.example.wardrobe_client.data.repository.AuthRepositoryImpl
import com.example.wardrobe_client.data.repository.ClothingItemRepositoryImpl
import com.example.wardrobe_client.data.repository.OutfitRepositoryImpl
import com.example.wardrobe_client.data.repository.ProfileRepositoryImpl
import com.example.wardrobe_client.data.repository.ReferenceRepositoryImpl
import com.example.wardrobe_client.data.repository.TripRepositoryImpl
import com.example.wardrobe_client.domain.repository.AuthRepository
import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import com.example.wardrobe_client.domain.repository.OutfitRepository
import com.example.wardrobe_client.domain.repository.ProfileRepository
import com.example.wardrobe_client.domain.repository.ReferenceRepository
import com.example.wardrobe_client.domain.repository.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindReferenceRepository(impl: ReferenceRepositoryImpl): ReferenceRepository

    @Binds
    @Singleton
    abstract fun bindClothingItemRepository(impl: ClothingItemRepositoryImpl): ClothingItemRepository

    @Binds
    @Singleton
    abstract fun bindOutfitRepository(impl: OutfitRepositoryImpl): OutfitRepository

    @Binds
    @Singleton
    abstract fun bindTripRepository(impl: TripRepositoryImpl): TripRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}