package com.zorrokid.mybasicjetpackcomposeapp

import androidx.annotation.StringRes

enum class MyBasicJetpackComposeScreen(@StringRes val title: Int) {
    Start(title = R.string.start),
    LogIn(title = R.string.login),
    SignUp(title = R.string.signup),
    Main(title = R.string.main),
    Splash(title = R.string.splash),
    Settings(title = R.string.settings),
    AddItem(title = R.string.add_item),
}