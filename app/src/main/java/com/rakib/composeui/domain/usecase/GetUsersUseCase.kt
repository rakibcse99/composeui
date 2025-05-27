package com.rakib.composeui.domain.usecase


import com.rakib.composeui.domain.model.User
import com.rakib.composeui.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val repository: ChatRepository) {
    operator fun invoke(): Flow<List<User>> = repository.getAllUsers()
}