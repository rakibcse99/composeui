package com.rakib.composeui.domain.usecase

import com.rakib.composeui.domain.model.Message
import com.rakib.composeui.domain.repository.ChatRepository

import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(message: Message) = repository.sendMessage(message)
}