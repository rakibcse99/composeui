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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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

    data class ChatsUiState(
        val users: List<User> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _chatsUiState = MutableStateFlow(ChatsUiState(isLoading = true))
    val chatsUiState: StateFlow<ChatsUiState> = _chatsUiState.asStateFlow()

    data class CallsUiState(
        val calls: List<Call> = emptyList(),
        val users: List<User> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _callsUiState = MutableStateFlow(CallsUiState(isLoading = true))
    val callsUiState: StateFlow<CallsUiState> = _callsUiState.asStateFlow()

    data class ChatUiState(
        val messages: List<Message> = emptyList(),
        val user: User? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _chatUiState = MutableStateFlow(ChatUiState(isLoading = true))
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val existingUsers = getUsersUseCase().firstOrNull() ?: emptyList()
                if (existingUsers.isEmpty()) {
                    insertUserUseCase(User(1, "Rakib Sheikh"))
                    insertUserUseCase(User(2, "Noman"))
                    insertUserUseCase(User(3, "Rokon"))
                    insertUserUseCase(User(4, "sakib Sheikh"))
                    insertUserUseCase(User(5, "sajeeb"))
                    insertUserUseCase(User(6, "Rabby"))
                }
                _chatsUiState.value = ChatsUiState(users = getUsersUseCase().first(), isLoading = false)

                val existingCalls = getCallsUseCase().firstOrNull() ?: emptyList()
                if (existingCalls.isEmpty()) {
                    insertCallUseCase(Call(1, 1, System.currentTimeMillis(), true, false))
                    insertCallUseCase(Call(2, 2, System.currentTimeMillis() - 3600000, false, true))
                }
                _callsUiState.value = CallsUiState(
                    calls = getCallsUseCase().first(),
                    users = getUsersUseCase().first(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _chatsUiState.value = ChatsUiState(isLoading = false, error = "Failed to load users")
                _callsUiState.value = CallsUiState(isLoading = false, error = "Failed to load calls")
            }
        }
    }

    fun loadMessagesForUser(userId: Int) {
        viewModelScope.launch {
            _chatUiState.value = ChatUiState(isLoading = true)
            try {
                getMessagesForUserUseCase(userId)
                    .catch { e ->
                        _chatUiState.value = ChatUiState(
                            isLoading = false,
                            error = "Failed to load messages: ${e.message}"
                        )
                    }
                    .collect { messages ->
                        val user = getUserByIdUseCase(userId).firstOrNull()
                        _chatUiState.value = ChatUiState(
                            messages = messages,
                            user = user,
                            isLoading = false
                        )
                    }
            } catch (e: Exception) {
                _chatUiState.value = ChatUiState(
                    isLoading = false,
                    error = "Failed to load messages: ${e.message}"
                )
            }
        }
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            try {
                sendMessageUseCase(message)
                loadMessagesForUser(message.userId)
            } catch (e: Exception) {
                _chatUiState.value = _chatUiState.value.copy(
                    error = "Failed to send message: ${e.message}"
                )
            }
        }
    }

    fun getUser(userId: Int): StateFlow<User?> {
        val state = MutableStateFlow<User?>(null)
        viewModelScope.launch {
            getUserByIdUseCase(userId).collect { user ->
                state.value = user
            }
        }
        return state.asStateFlow()
    }
}