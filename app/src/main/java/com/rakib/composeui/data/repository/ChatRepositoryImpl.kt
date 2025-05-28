package com.rakib.composeui.data.repository

import com.rakib.composeui.data.mapper.toDomain
import com.rakib.composeui.data.mapper.toEntity
import com.rakib.composeui.domain.model.Call
import com.rakib.composeui.domain.model.Message
import com.rakib.composeui.domain.model.User
import com.rakib.composeui.domain.repository.ChatRepository
import com.rakib.whatsappclone.data.local.dao.ChatDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao
) : ChatRepository {
    override fun getUsers(): Flow<List<User>> =
        chatDao.getAllUsers().map { users -> users.map { it.toDomain() } }

    override fun getUserById(userId: Int): Flow<User?> =
        chatDao.getUserById(userId).map { it?.toDomain() }

    override fun getMessagesForUser(userId: Int): Flow<List<Message>> =
        chatDao.getMessagesForUser(userId).map { messages -> messages.map { it.toDomain() } }

    override fun getCalls(): Flow<List<Call>> =
        chatDao.getCalls().map { calls -> calls.map { it.toDomain() } }

    override suspend fun sendMessage(message: Message) {
        chatDao.insertMessage(message.toEntity())
    }

    override suspend fun insertUser(user: User) {
        chatDao.insertUser(user.toEntity())
    }

    override suspend fun insertCall(call: Call) {
        chatDao.insertCall(call.toEntity())
    }
}