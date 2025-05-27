package com.rakib.composeui.domain.usecase


import com.rakib.composeui.domain.model.User
import com.rakib.composeui.domain.repository.ChatRepository
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(user: User) = repository.insertUser(user)
}