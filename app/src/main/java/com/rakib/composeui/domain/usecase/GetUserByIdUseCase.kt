package com.rakib.composeui.domain.usecase

import com.rakib.composeui.domain.model.User
import com.rakib.composeui.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val repository: ChatRepository) {
    operator fun invoke(userId: Int): Flow<User?> = repository.getUserById(userId)
}