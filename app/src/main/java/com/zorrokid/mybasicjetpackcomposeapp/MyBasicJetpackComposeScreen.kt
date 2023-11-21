package com.zorrokid.mybasicjetpackcomposeapp

import androidx.annotation.StringRes

enum class MyBasicJetpackComposeScreen(@StringRes val title: Int) {
    Start(title = R.string.start),
    LogIn(title = R.string.login),
    Main(title = R.string.main),
}