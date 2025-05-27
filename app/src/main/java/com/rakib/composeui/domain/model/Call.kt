package com.rakib.composeui.domain.model

data class Call(
    val id: Int,
    val userId: Int,
    val timestamp: Long,
    val isOutgoing: Boolean,
    val isVideo: Boolean
)