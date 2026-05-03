package com.example.wardrobe_client.domain.repository

import com.example.wardrobe_client.domain.model.References

interface ReferenceRepository {
    suspend fun getReferences(): Result<References>
}