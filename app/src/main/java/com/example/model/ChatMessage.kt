package com.example.model

import java.util.UUID

enum class ChatSender {
    USER,
    ASSISTANT,
    SYSTEM
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: ChatSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val suggestedActions: List<String> = emptyList()
)
