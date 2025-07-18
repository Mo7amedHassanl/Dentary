package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.ChatConversation
import com.m7md7sn.dentary.data.model.ChatMessage
import com.m7md7sn.dentary.data.model.CreateMessageRequest
import com.m7md7sn.dentary.data.source.remote.ChatDataSource
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository {

    override suspend fun getAllConversations(): Result<List<ChatConversation>> {
        return chatDataSource.getAllConversations()
    }

    override suspend fun getConversationByPatient(patientId: String): Result<ChatConversation> {
        return chatDataSource.getConversationByPatient(patientId)
    }

    override suspend fun createConversation(patientId: String): Result<ChatConversation> {
        return chatDataSource.createConversation(patientId)
    }

    override suspend fun getMessagesByConversation(conversationId: String): Result<List<ChatMessage>> {
        return chatDataSource.getMessagesByConversation(conversationId)
    }

    override suspend fun sendMessage(request: CreateMessageRequest): Result<ChatMessage> {
        return chatDataSource.sendMessage(request)
    }

    override suspend fun markMessageAsRead(messageId: String): Result<Unit> {
        return chatDataSource.markMessageAsRead(messageId)
    }

    override suspend fun markConversationAsRead(conversationId: String): Result<Unit> {
        return chatDataSource.markConversationAsRead(conversationId)
    }
}
