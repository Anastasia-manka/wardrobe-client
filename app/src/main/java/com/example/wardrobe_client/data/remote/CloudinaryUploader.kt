package com.example.wardrobe_client.data.remote

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class CloudinaryUploader @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("cloudinary") private val okHttpClient: OkHttpClient
) {
    private val cloudName = "djrxlxqln"
    private val uploadPreset = "wardrobe_unsigned"
    private val uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"

    suspend fun uploadImage(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
                ?: throw Exception("Не удалось прочитать файл")

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    "photo.jpg",
                    bytes.toRequestBody("image/*".toMediaTypeOrNull())
                )
                .addFormDataPart("upload_preset", uploadPreset)
                .addFormDataPart("folder", "wardrobe_items")
                .build()

            val request = Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val body = response.body?.string() ?: throw Exception("Пустой ответ от Cloudinary")

            if (!response.isSuccessful) throw Exception("Ошибка загрузки: ${response.code}")

            JSONObject(body).getString("secure_url")
        }
    }
}