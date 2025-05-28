package com.rakib.composeui.domain.usecase

import com.rakib.composeui.domain.model.Call
import com.rakib.composeui.domain.repository.ChatRepository

import javax.inject.Inject

class InsertCallUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(call: Call) = repository.insertCall(call)
}