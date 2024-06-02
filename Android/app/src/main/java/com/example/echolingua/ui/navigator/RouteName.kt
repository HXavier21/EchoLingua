package com.example.echolingua.ui.navigator

object RouteName {
    const val AUDIO_TRANSCRIBE_PAGE = "audio_transcribe_page"
    const val CAMERA_TRANSLATE_PAGE = "camera_translate_page"
    const val TRANSLATE_PAGE = "translate_page"
    const val TRANSLATE_PAGES = "translate_pages"
    const val BACK_END_TEST_PAGE = "back_end_test_page"
    const val DATA_PAGE = "data_page"

    infix fun String.withArgumentKey(key: String) = "$this/{$key}"

    infix fun String.withArgument(arg: Any) = "$this/$arg"
}

