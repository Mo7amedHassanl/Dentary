package com.m7md7sn.dentary.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatConversation(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("patient_id")
    val patientId: String,
    @SerialName("last_message_at")
    val lastMessageAt: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class ChatMessage(
    @SerialName("id")
    val id: String,
    @SerialName("conversation_id")
    val conversationId: String,
    @SerialName("sender_type")
    val senderType: SenderType,
    @SerialName("message_text")
    val messageText: String,
    @SerialName("message_type")
    val messageType: MessageType = MessageType.TEXT,
    @SerialName("file_url")
    val fileUrl: String? = null,
    @SerialName("is_read")
    val isRead: Boolean = false,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
enum class SenderType {
    DOCTOR,
    PATIENT
}

@Serializable
enum class MessageType {
    TEXT,
    IMAGE,
    FILE
}

@Serializable
data class CreateMessageRequest(
    @SerialName("conversation_id")
    val conversationId: String,
    @SerialName("sender_type")
    val senderType: SenderType,
    @SerialName("message_text")
    val messageText: String,
    @SerialName("message_type")
    val messageType: MessageType = MessageType.TEXT,
    @SerialName("file_url")
    val fileUrl: String? = null
)
