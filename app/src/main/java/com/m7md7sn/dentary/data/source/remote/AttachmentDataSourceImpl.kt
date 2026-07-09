package com.m7md7sn.dentary.data.source.remote

import android.content.Context
import android.net.Uri
import com.m7md7sn.dentary.data.model.Attachment
import com.m7md7sn.dentary.data.model.CreateAttachmentRequest
import com.m7md7sn.dentary.data.util.toDataError
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.Storage
import java.util.UUID
import javax.inject.Inject

class AttachmentDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
    @ApplicationContext private val context: Context
) : AttachmentDataSource {

    override suspend fun createAttachment(request: CreateAttachmentRequest): Result<Attachment, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val attachment = postgrest
                .from("attachments")
                .insert(request) { select() }
                .decodeSingle<Attachment>()

            Result.Success(attachment)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getAttachmentsByMedicalHistory(medicalHistoryId: String): Result<List<Attachment>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val attachments = postgrest
                .from("attachments")
                .select {
                    filter { eq("medical_history_id", medicalHistoryId) }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<Attachment>()

            Result.Success(attachments)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun uploadAttachmentFile(uri: Uri, medicalHistoryId: String): Result<String, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val bytes = if (uri.scheme == "file") {
                java.io.File(uri.path!!).readBytes()
            } else {
                val inputStream = context.contentResolver.openInputStream(uri)
                    ?: return Result.Error(DataError.Network.UNKNOWN, "Cannot open file")
                inputStream.readBytes()
            }

            val extension = uri.lastPathSegment?.substringAfterLast('.', "jpg") ?: "jpg"
            val fileName = "${UUID.randomUUID()}.$extension"
            val path = "$medicalHistoryId/$fileName"

            storage.from("medical_attachments").upload(path, bytes)

            val publicUrl = storage.from("medical_attachments").publicUrl(path)

            Result.Success(publicUrl)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun deleteAttachment(id: String): Result<Unit, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            postgrest
                .from("attachments")
                .delete { filter { eq("id", id) } }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }
}
