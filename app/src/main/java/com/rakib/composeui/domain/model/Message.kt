package com.rakib.composeui.domain.model

data class Message(
    val userId: Int,
    val senderId: String,
    val text: String,
    val timestamp: Long,
    val isSent: Boolean = false
)