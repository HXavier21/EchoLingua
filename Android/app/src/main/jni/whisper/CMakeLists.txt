set(CMAKE_CXX_STANDARD 11)

set(
        SOURCE_FILES
        libwhisper/ggml.c
        libwhisper/whisper.cpp
        libwhisper/ggml-alloc.c
        libwhisper/ggml-backend.c
        libwhisper/ggml-quants.c
)
add_library(
        whisper_v8fp16_va
        SHARED
        ${SOURCE_FILES}
)
target_link_libraries(whisper_v8fp16_va ${log-lib} android)

target_compile_options(whisper_v8fp16_va PRIVATE -march=armv8.2-a+fp16)

if (NOT CMAKE_BUILD_TYPE STREQUAL "Debug")
    target_compile_options(whisper_v8fp16_va PRIVATE -O3)
    target_link_options(whisper_v8fp16_va PRIVATE -Wl,--gc-sections)
    target_link_options(whisper_v8fp16_va PRIVATE -Wl,--exclude-libs,ALL)
    target_compile_options(whisper_v8fp16_va PRIVATE -flto)
endif ()
