package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.ChatConversation
import com.m7md7sn.dentary.data.model.ChatMessage
import com.m7md7sn.dentary.data.model.CreateMessageRequest
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject

class ChatDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) : ChatDataSource {

    override suspend fun getAllConversations(): Result<List<ChatConversation>> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val conversations = postgrest.from("chat_conversations")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                    }
                    order(column = "last_message_at", order = Order.DESCENDING)
                }
                .decodeList<ChatConversation>()

            Result.Success(conversations)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch conversations")
        }
    }

    override suspend fun getConversationByPatient(patientId: String): Result<ChatConversation> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val conversation = postgrest.from("chat_conversations")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        eq("patient_id", patientId)
                    }
                }
                .decodeSingle<ChatConversation>()

            Result.Success(conversation)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch conversation")
        }
    }

    override suspend fun createConversation(patientId: String): Result<ChatConversation> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val conversationData = mapOf(
                "user_id" to currentUserId,
                "patient_id" to patientId
            )

            val conversation = postgrest.from("chat_conversations")
                .insert(conversationData) {
                    select()
                }
                .decodeSingle<ChatConversation>()

            Result.Success(conversation)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to create conversation")
        }
    }

    override suspend fun getMessagesByConversation(conversationId: String): Result<List<ChatMessage>> {
        return try {
            val messages = postgrest.from("chat_messages")
                .select {
                    filter {
                        eq("conversation_id", conversationId)
                    }
                    order(column = "created_at", order = Order.ASCENDING)
                }
                .decodeList<ChatMessage>()

            Result.Success(messages)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch messages")
        }
    }

    override suspend fun sendMessage(request: CreateMessageRequest): Result<ChatMessage> {
        return try {
            val message = postgrest.from("chat_messages")
                .insert(request) {
                    select()
                }
                .decodeSingle<ChatMessage>()

            // Update conversation's last_message_at
            postgrest.from("chat_conversations")
                .update(mapOf("last_message_at" to "NOW()")) {
                    filter {
                        eq("id", request.conversationId)
                    }
                }

            Result.Success(message)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to send message")
        }
    }

    override suspend fun markMessageAsRead(messageId: String): Result<Unit> {
        return try {
            postgrest.from("chat_messages")
                .update(mapOf("is_read" to true)) {
                    filter {
                        eq("id", messageId)
                    }
                }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to mark message as read")
        }
    }

    override suspend fun markConversationAsRead(conversationId: String): Result<Unit> {
        return try {
            postgrest.from("chat_messages")
                .update(mapOf("is_read" to true)) {
                    filter {
                        eq("conversation_id", conversationId)
                        eq("is_read", false)
                    }
                }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to mark conversation as read")
        }
    }
}
