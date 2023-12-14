package com.zorrokid.mediacollector

import androidx.annotation.StringRes

enum class MediaCollectorScreen(@StringRes val title: Int) {
    Start(title = R.string.start),
    LogIn(title = R.string.login),
    SignUp(title = R.string.signup),
    Main(title = R.string.main),
    Splash(title = R.string.splash),
    Settings(title = R.string.settings),
    AddItem(title = R.string.add_item),
    AddItemParent(title = R.string.add_item),
    Search(title = R.string.search),
    TextRecognition(title = R.string.text_recognition),
}

const val ID = "id"
const val ID_ARG = "?$ID={$ID}"
