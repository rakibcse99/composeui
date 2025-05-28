package com.rakib.whatsappclone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rakib.composeui.data.local.entity.CallEntity
import com.rakib.composeui.data.local.entity.MessageEntity
import com.rakib.composeui.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): Flow<UserEntity?>

    @Query("SELECT * FROM messages WHERE userId = :userId")
    fun getMessagesForUser(userId: Int): Flow<List<MessageEntity>>

    @Query("SELECT * FROM calls")
    fun getCalls(): Flow<List<CallEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCall(call: CallEntity)
}