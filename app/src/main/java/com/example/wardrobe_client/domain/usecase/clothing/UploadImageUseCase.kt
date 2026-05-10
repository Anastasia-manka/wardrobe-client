package com.example.wardrobe_client.domain.usecase.clothing

import android.net.Uri
import com.example.wardrobe_client.data.remote.CloudinaryUploader
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val cloudinaryUploader: CloudinaryUploader
) {
    suspend operator fun invoke(uri: Uri): Result<String> =
        cloudinaryUploader.uploadImage(uri)
}