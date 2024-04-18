include_directories(app)

add_library(avcodec

        SHARED

        IMPORTED)

add_library(avfilter

        SHARED

        IMPORTED)

add_library(avutil

        SHARED

        IMPORTED)

add_library(avformat

        SHARED

        IMPORTED)

add_library(swscale

        SHARED

        IMPORTED)

add_library(swresample

        SHARED

        IMPORTED)

add_library(postproc

        SHARED

        IMPORTED)

add_library(avdevice

        SHARED

        IMPORTED)

add_library(x264

        SHARED

        IMPORTED)

add_library(ffmpeg
        SHARED
        fftools/ffmpeg_filter.c
        fftools/ffmpeg_hw.c
        fftools/ffmpeg_opt.c
        fftools/ffmpeg_demux.c
        fftools/ffmpeg_mux.c
        fftools/cmdutils.c
        fftools/opt_common.c
        fftools/ffmpeg_mux_init.c
        fftools/opt_common.c
        fftools/sync_queue.c
        fftools/thread_queue.c
        fftools/objpool.c
        fftools/ffmpeg.c
        )

set_target_properties(x264

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libx264.so)

set_target_properties(avcodec

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libavcodec.so)

set_target_properties(avfilter

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libavfilter.so)

set_target_properties(avformat

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libavformat.so)

set_target_properties(avdevice

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libavdevice.so)

set_target_properties(avutil

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libavutil.so)

set_target_properties(swresample

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libswresample.so)

set_target_properties(swscale

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libswscale.so)

set_target_properties(postproc

        PROPERTIES IMPORTED_LOCATION

        ${CMAKE_SOURCE_DIR}/ffmpeg/lib/${CMAKE_ANDROID_ARCH_ABI}/libpostproc.so)

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

include_directories(
        ${CMAKE_SOURCE_DIR}/ffmpeg/include
)

include_directories(
        ${CMAKE_SOURCE_DIR}/ffmpeg/fftools
)

target_link_libraries(
        ffmpeg
        avfilter
        avformat
        avdevice
        avcodec
        avutil
        swresample
        swscale
        postproc
        x264
        ${log-lib})