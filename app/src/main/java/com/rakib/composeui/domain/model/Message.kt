package com.rakib.composeui.domain.model

data class Message(
    val id: Int,
    val userId: Int,
    val content: String,
    val timestamp: Long,
    val isSent: Boolean
)