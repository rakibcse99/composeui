package com.rakib.composeui.domain.repository


import com.rakib.composeui.domain.model.Call
import com.rakib.composeui.domain.model.Message
import com.rakib.composeui.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getUsers(): Flow<List<User>>
    fun getUserById(userId: Int): Flow<User?>
    fun getMessagesForUser(userId: Int): Flow<List<Message>>
    fun getCalls(): Flow<List<Call>>
    suspend fun sendMessage(message: Message)
    suspend fun insertUser(user: User)
    suspend fun insertCall(call: Call)
}