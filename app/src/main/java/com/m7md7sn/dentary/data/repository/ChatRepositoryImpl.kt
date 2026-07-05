package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.ChatConversation
import com.m7md7sn.dentary.data.model.ChatMessage
import com.m7md7sn.dentary.data.model.CreateMessageRequest
import com.m7md7sn.dentary.data.source.remote.ChatDataSource
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository {

    override suspend fun getAllConversations(): Result<List<ChatConversation>, DataError> {
        return chatDataSource.getAllConversations()
    }

    override suspend fun getConversationByPatient(patientId: String): Result<ChatConversation, DataError> {
        return chatDataSource.getConversationByPatient(patientId)
    }

    override suspend fun createConversation(patientId: String): Result<ChatConversation, DataError> {
        return chatDataSource.createConversation(patientId)
    }

    override suspend fun getMessagesByConversation(conversationId: String): Result<List<ChatMessage>, DataError> {
        return chatDataSource.getMessagesByConversation(conversationId)
    }

    override suspend fun sendMessage(request: CreateMessageRequest): Result<ChatMessage, DataError> {
        return chatDataSource.sendMessage(request)
    }

    override suspend fun markMessageAsRead(messageId: String): Result<Unit, DataError> {
        return chatDataSource.markMessageAsRead(messageId)
    }

    override suspend fun markConversationAsRead(conversationId: String): Result<Unit, DataError> {
        return chatDataSource.markConversationAsRead(conversationId)
    }
}