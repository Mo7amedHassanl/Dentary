package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.ChatConversation
import com.m7md7sn.dentary.data.model.ChatMessage
import com.m7md7sn.dentary.data.model.CreateMessageRequest
import com.m7md7sn.dentary.data.util.toDataError
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class ChatDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) : ChatDataSource {

    override suspend fun getAllConversations(): Result<List<ChatConversation>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val conversations = postgrest
                .from("chat_conversations")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<ChatConversation>()

            Result.Success(conversations)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getConversationByPatient(patientId: String): Result<ChatConversation, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val conversation = postgrest
                .from("chat_conversations")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        eq("patient_id", patientId)
                    }
                }
                .decodeSingle<ChatConversation>()

            Result.Success(conversation)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun createConversation(patientId: String): Result<ChatConversation, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val conversation = postgrest
                .from("chat_conversations")
                .insert(mapOf("user_id" to currentUserId, "patient_id" to patientId)) {
                    select()
                }
                .decodeSingle<ChatConversation>()

            Result.Success(conversation)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getMessagesByConversation(conversationId: String): Result<List<ChatMessage>, DataError> {
        return try {
            val messages = postgrest
                .from("chat_messages")
                .select {
                    filter {
                        eq("conversation_id", conversationId)
                    }
                }
                .decodeList<ChatMessage>()

            Result.Success(messages)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun sendMessage(request: CreateMessageRequest): Result<ChatMessage, DataError> {
        return try {
            val message = postgrest
                .from("chat_messages")
                .insert(request) {
                    select()
                }
                .decodeSingle<ChatMessage>()

            Result.Success(message)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun markMessageAsRead(messageId: String): Result<Unit, DataError> {
        return try {
            postgrest
                .from("chat_messages")
                .update(mapOf("is_read" to true)) {
                    filter {
                        eq("id", messageId)
                    }
                }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun markConversationAsRead(conversationId: String): Result<Unit, DataError> {
        return try {
            postgrest
                .from("chat_messages")
                .update(mapOf("is_read" to true)) {
                    filter {
                        eq("conversation_id", conversationId)
                    }
                }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }
}