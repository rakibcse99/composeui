package com.rakib.composeui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rakib.composeui.domain.model.Call
import com.rakib.composeui.domain.model.Message
import com.rakib.composeui.domain.model.User
import com.rakib.composeui.domain.usecase.GetCallsUseCase
import com.rakib.composeui.domain.usecase.GetMessagesForUserUseCase
import com.rakib.composeui.domain.usecase.GetUserByIdUseCase
import com.rakib.composeui.domain.usecase.GetUsersUseCase
import com.rakib.composeui.domain.usecase.InsertCallUseCase
import com.rakib.composeui.domain.usecase.InsertUserUseCase
import com.rakib.composeui.domain.usecase.SendMessageUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getMessagesForUserUseCase: GetMessagesForUserUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val insertUserUseCase: InsertUserUseCase,
    private val getCallsUseCase: GetCallsUseCase,
    private val insertCallUseCase: InsertCallUseCase
) : ViewModel() {
    init {
        viewModelScope.launch {
            insertUserUseCase(User(1, "Alice"))
            insertUserUseCase(User(2, "Bob"))
            insertUserUseCase(User(3, "Charlie"))
            insertCallUseCase(Call(1, 1, System.currentTimeMillis(), true, false))
            insertCallUseCase(Call(2, 2, System.currentTimeMillis() - 3600000, false, true))
        }
    }

    val users: Flow<List<User>> = getUsersUseCase()
    val calls: Flow<List<Call>> = getCallsUseCase()

    fun getUser(userId: Int): Flow<User?> = getUserByIdUseCase(userId)

    fun getMessagesForUser(userId: Int): Flow<List<Message>> = getMessagesForUserUseCase(userId)

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            sendMessageUseCase(message)
        }
    }
}