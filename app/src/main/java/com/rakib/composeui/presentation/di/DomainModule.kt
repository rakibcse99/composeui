package com.rakib.composeui.presentation.di

import com.rakib.composeui.domain.repository.ChatRepository
import com.rakib.composeui.domain.usecase.GetCallsUseCase
import com.rakib.composeui.domain.usecase.GetMessagesForUserUseCase
import com.rakib.composeui.domain.usecase.GetUserByIdUseCase
import com.rakib.composeui.domain.usecase.GetUsersUseCase
import com.rakib.composeui.domain.usecase.InsertCallUseCase
import com.rakib.composeui.domain.usecase.InsertUserUseCase
import com.rakib.composeui.domain.usecase.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    fun provideGetUsersUseCase(repository: ChatRepository): GetUsersUseCase {
        return GetUsersUseCase(repository)
    }

    @Provides
    fun provideGetUserByIdUseCase(repository: ChatRepository): GetUserByIdUseCase {
        return GetUserByIdUseCase(repository)
    }

    @Provides
    fun provideGetMessagesForUserUseCase(repository: ChatRepository): GetMessagesForUserUseCase {
        return GetMessagesForUserUseCase(repository)
    }

    @Provides
    fun provideSendMessageUseCase(repository: ChatRepository): SendMessageUseCase {
        return SendMessageUseCase(repository)
    }

    @Provides
    fun provideInsertUserUseCase(repository: ChatRepository): InsertUserUseCase {
        return InsertUserUseCase(repository)
    }

    @Provides
    fun provideGetCallsUseCase(repository: ChatRepository): GetCallsUseCase {
        return GetCallsUseCase(repository)
    }

    @Provides
    fun provideInsertCallUseCase(repository: ChatRepository): InsertCallUseCase {
        return InsertCallUseCase(repository)
    }
}