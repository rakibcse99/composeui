package com.rakib.composeui.domain.usecase


import com.rakib.composeui.domain.model.Message
import com.rakib.composeui.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesForUserUseCase @Inject constructor(private val repository: ChatRepository) {
    operator fun invoke(userId: Int): Flow<List<Message>> = repository.getMessagesForUser(userId)
}