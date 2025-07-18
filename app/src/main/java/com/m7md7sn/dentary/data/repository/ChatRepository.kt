package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.ChatConversation
import com.m7md7sn.dentary.data.model.ChatMessage
import com.m7md7sn.dentary.data.model.CreateMessageRequest
import com.m7md7sn.dentary.utils.Result

interface ChatRepository {
    suspend fun getAllConversations(): Result<List<ChatConversation>>
    suspend fun getConversationByPatient(patientId: String): Result<ChatConversation>
    suspend fun createConversation(patientId: String): Result<ChatConversation>
    suspend fun getMessagesByConversation(conversationId: String): Result<List<ChatMessage>>
    suspend fun sendMessage(request: CreateMessageRequest): Result<ChatMessage>
    suspend fun markMessageAsRead(messageId: String): Result<Unit>
    suspend fun markConversationAsRead(conversationId: String): Result<Unit>
}
