package com.m7md7sn.dentary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatConversation(
    val id: String,
    val userId: String,
    val patientId: String,
    val lastMessageAt: String? = null,
    val createdAt: String? = null
)

@Serializable
data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderType: SenderType,
    val messageText: String,
    val messageType: MessageType = MessageType.TEXT,
    val fileUrl: String? = null,
    val isRead: Boolean = false,
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
    val conversationId: String,
    val senderType: SenderType,
    val messageText: String,
    val messageType: MessageType = MessageType.TEXT,
    val fileUrl: String? = null
)
