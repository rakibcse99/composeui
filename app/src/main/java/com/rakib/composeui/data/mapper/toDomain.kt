package com.rakib.composeui.data.mapper

import com.rakib.composeui.data.local.entity.CallEntity
import com.rakib.composeui.data.local.entity.MessageEntity
import com.rakib.composeui.data.local.entity.UserEntity
import com.rakib.composeui.domain.model.Call
import com.rakib.composeui.domain.model.Message
import com.rakib.composeui.domain.model.User


fun UserEntity.toDomain(): User = User(id = id, name = name)

fun User.toEntity(): UserEntity = UserEntity(id = id, name = name)

fun MessageEntity.toDomain(): Message = Message(
    userId = userId,
    senderId = senderId,
    text = text,
    timestamp = timestamp,
    isSent = senderId == userId.toString()
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    userId = userId,
    senderId = senderId,
    text = text,
    timestamp = timestamp
)

fun CallEntity.toDomain(): Call = Call(
    id = id,
    userId = userId,
    timestamp = timestamp,
    isOutgoing = isOutgoing,
    isVideo = isVideo
)

fun Call.toEntity(): CallEntity = CallEntity(
    id = id,
    userId = userId,
    timestamp = timestamp,
    isOutgoing = isOutgoing,
    isVideo = isVideo
)