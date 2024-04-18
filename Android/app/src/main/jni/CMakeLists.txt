# Minimum required version of CMake
cmake_minimum_required(VERSION 3.10)

# Project name
project(app)

if (${ANDROID_ABI} STREQUAL "arm64-v8a")

    ## Add the subdirectories for Whisper and ffmpeg
    add_subdirectory(whisper)
    add_subdirectory(ffmpeg)

    include_directories(${CMAKE_SOURCE_DIR}/ffmpeg/include)

    # Add the source files for the Android app
    add_library(app SHARED ${CMAKE_SOURCE_DIR}/jni.c)

    target_link_libraries(app whisper_v8fp16_va ffmpeg)

endif ()