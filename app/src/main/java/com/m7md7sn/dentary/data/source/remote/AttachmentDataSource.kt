package com.m7md7sn.dentary.data.source.remote

import android.net.Uri
import com.m7md7sn.dentary.data.model.Attachment
import com.m7md7sn.dentary.data.model.CreateAttachmentRequest
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result

interface AttachmentDataSource {
    suspend fun createAttachment(request: CreateAttachmentRequest): Result<Attachment, DataError>
    suspend fun getAttachmentsByMedicalHistory(medicalHistoryId: String): Result<List<Attachment>, DataError>
    suspend fun uploadAttachmentFile(uri: Uri, medicalHistoryId: String): Result<String, DataError>
    suspend fun deleteAttachment(id: String): Result<Unit, DataError>
}
