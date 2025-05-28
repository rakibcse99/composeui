package com.rakib.composeui.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calls")
data class CallEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val timestamp: Long,
    val isOutgoing: Boolean,
    val isVideo: Boolean
)