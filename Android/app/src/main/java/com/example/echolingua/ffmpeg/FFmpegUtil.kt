package com.example.echolingua.ffmpeg

object FFmpegUtil {
    private external fun runffmpeg(commands: Array<String>)

    fun audioToWav(inputPath: String, outputPath: String) =
        runffmpeg(
            arrayOf(
                "ffmpeg",
                "-i",
                inputPath,
                "-acodec",
                "pcm_s16le",
                "-ac",
                "1",
                "-ar",
                "16000",
                outputPath
            )
        )

}