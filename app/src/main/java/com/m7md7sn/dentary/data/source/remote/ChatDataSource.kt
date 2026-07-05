package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.ChatConversation
import com.m7md7sn.dentary.data.model.ChatMessage
import com.m7md7sn.dentary.data.model.CreateMessageRequest
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result

interface ChatDataSource {
    suspend fun getAllConversations(): Result<List<ChatConversation>, DataError>
    suspend fun getConversationByPatient(patientId: String): Result<ChatConversation, DataError>
    suspend fun createConversation(patientId: String): Result<ChatConversation, DataError>
    suspend fun getMessagesByConversation(conversationId: String): Result<List<ChatMessage>, DataError>
    suspend fun sendMessage(request: CreateMessageRequest): Result<ChatMessage, DataError>
    suspend fun markMessageAsRead(messageId: String): Result<Unit, DataError>
    suspend fun markConversationAsRead(conversationId: String): Result<Unit, DataError>
}