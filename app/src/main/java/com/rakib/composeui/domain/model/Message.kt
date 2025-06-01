package com.rakib.composeui.domain.model


data class Message(
    val userId: Int,
    val senderId: String,
    val text: String,
    val fileUri: String? = null, // New field for file URI
    val timestamp: Long,
    val isSent: Boolean
)