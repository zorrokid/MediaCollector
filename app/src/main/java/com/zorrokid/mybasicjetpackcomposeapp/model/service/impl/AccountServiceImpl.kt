package com.zorrokid.mybasicjetpackcomposeapp.model.service.impl

import android.util.Log
import com.zorrokid.mybasicjetpackcomposeapp.model.User
import com.zorrokid.mybasicjetpackcomposeapp.model.service.AccountService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


// Constructor-injected, because Hilt needs to know how to
// provide instances of AccountServiceImpl, too.
class AccountServiceImpl @Inject constructor( ) : AccountService {
    override val currentUserId: String
        get() = TODO("Not yet implemented")
    override val hasUser: Boolean
        get() = TODO("Not yet implemented")
    override val currentUser: Flow<User>
        get() = TODO("Not yet implemented")

    override suspend fun authenticate(email: String, password: String) {
        Log.v("AccountServiceImpl", "authenticate")
    }
}