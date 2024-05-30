package com.example.echolingua.ui.navigator

object RouteName {
    const val SELECT_MODE: String = "select_mode"
    const val AUDIO_TRANSCRIBE_PAGE = "audio_transcribe_page"
    const val CAMERA_TRANSLATE_PAGE = "camera_translate_page"
    const val TRANSLATE_PAGE = "translate_page"
    const val LANGUAGE_SELECT_PAGE = "language_select_page"
    const val MAIN_TRANSLATE_PAGE = "main_translate_page"
    const val DATA_PAGE = "data_page"
    infix fun String.withArgumentKey(key: String) = "$this/{$key}"

    infix fun String.withArgument(arg: Any) = "$this/$arg"
}

