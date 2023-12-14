package com.zorrokid.mediacollector

import androidx.annotation.StringRes

enum class MediaCollectorScreen(@StringRes val title: Int) {
    Start(title = R.string.start),
    LogIn(title = R.string.login),
    SignUp(title = R.string.signup),
    Main(title = R.string.main),
    Splash(title = R.string.splash),
    Settings(title = R.string.settings),
    AddOrEditItemForm(title = R.string.add_or_edit_item),
    AddOrEditItem(title = R.string.add_or_edit_item),
    Search(title = R.string.search),
    TextRecognition(title = R.string.text_recognition),
}

const val ID = "id"
const val ID_ARG = "?$ID={$ID}"
