package com.zorrokid.mybasicjetpackcomposeapp.model.service

import com.zorrokid.mybasicjetpackcomposeapp.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
}