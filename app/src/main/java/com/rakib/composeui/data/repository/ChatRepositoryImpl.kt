package com.rakib.composeui.data.repository

import com.example.whatsappclone.data.local.dao.ChatDao
import com.example.whatsappclone.data.mapper.toDomain
import com.example.whatsappclone.data.mapper.toEntity
import com.example.whatsappclone.domain.model.Call
import com.example.whatsappclone.domain.model.Message
import com.example.whatsappclone.domain.model.User
import com.example.whatsappclone.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val chatDao: ChatDao) : ChatRepository {
    override fun getAllUsers(): Flow<List<User>> =
        chatDao.getAllUsers().map { users -> users.map { it.toDomain() } }

    override fun getUserById(userId: Int): Flow<User?> =
        chatDao.getUserById(userId).map { it?.toDomain() }

    override fun getMessagesForUser(userId: Int): Flow<List<Message>> =
        chatDao.getMessagesForUser(userId).map { messages -> messages.map { it.toDomain() } }

    override fun getCalls(): Flow<List<Call>> =
        chatDao.getCalls().map { calls -> calls.map { it.toDomain() } }

    override suspend fun insertUser(user: User) = chatDao.insertUser(user.toEntity())

    override suspend fun insertMessage(message: Message) = chatDao.insertMessage(message.toEntity())

    override suspend fun insertCall(call: Call) = chatDao.insertCall(call.toEntity())
}