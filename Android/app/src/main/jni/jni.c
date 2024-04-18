#include <jni.h>
#include <android/log.h>

#include "ffmpeg/include/ffmpeg.h"

JNIEXPORT void JNICALL
Java_com_example_echolingua_ffmpeg_FFmpegUtil_runffmpeg(JNIEnv *env, jobject thiz,
                                                       jobjectArray commands) {
    int argc = (*env)->GetArrayLength(env, commands);
    char *argv[argc];
    int i;
    for (i = 0; i < argc; i++) {
        jstring
        auto js = (*env)->GetObjectArrayElement(env, commands, i);
        argv[i] = (char *) (*env)->GetStringUTFChars(env, js, 0);
    }
    ffmpeg_exec(argc, argv);
}


#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <stdlib.h>
#include <sys/sysinfo.h>
#include <string.h>
#include "whisper/libwhisper/whisper.h"
#include "whisper/libwhisper/ggml.h"

#define UNUSED(x) (void)(x)
#define TAG "JNI"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,     TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,     TAG, __VA_ARGS__)

static inline int min(int a, int b) {
    return (a < b) ? a : b;
}

static inline int max(int a, int b) {
    return (a > b) ? a : b;
}

static size_t asset_read(void *ctx, void *output, size_t read_size) {
    return AAsset_read((AAsset *) ctx, output, read_size);
}

static bool asset_is_eof(void *ctx) {
    return AAsset_getRemainingLength64((AAsset *) ctx) <= 0;
}

static void asset_close(void *ctx) {
    AAsset_close((AAsset *) ctx);
}

JNIEXPORT jlong JNICALL
Java_com_example_echolingua_whisper_WhisperLib_initContext(
        JNIEnv *env, jobject thiz, jstring model_path_str) {
    UNUSED(thiz);
    struct whisper_context *context = NULL;
    const char *model_path_chars = (*env)->GetStringUTFChars(env, model_path_str, NULL);
    context = whisper_init_from_file(model_path_chars);
    (*env)->ReleaseStringUTFChars(env, model_path_str, model_path_chars);
    return (jlong) context;
}

JNIEXPORT void JNICALL
Java_com_example_echolingua_whisper_WhisperLib_freeContext(
        JNIEnv *env, jobject thiz, jlong context_ptr) {
    UNUSED(env);
    UNUSED(thiz);
    struct whisper_context *context = (struct whisper_context *) context_ptr;
    whisper_free(context);
}

static void log_callback(enum ggml_log_level level, const char *text, void *user_data) {
    LOGI("%s", text);
}

typedef struct {
    JNIEnv *env;
    jobject callback;
} callback_context;

JNIEXPORT void JNICALL
progress_callback_user_data(JNIEnv *env, jobject callback, int progress) {
    jclass cls = (*env)->FindClass(env, "com/example/echolingua/whisper/ProgressCallback");
    if (cls == NULL) {
        return;
    }
    jmethodID id = (*env)->GetMethodID(env, cls, "onUpdate", "(I)V");
    if (id == NULL) {
        return;
    }
    (*env)->CallVoidMethod(env, callback, id, progress);
}

void whisper_print_progress_callback(struct whisper_context *ctx, struct whisper_state *state,
                                     int progress, void *user_data) {
    callback_context *callback_ctx = (callback_context *) user_data;
    progress_callback_user_data(callback_ctx->env, callback_ctx->callback, progress);
}

JNIEXPORT void JNICALL
Java_com_example_echolingua_whisper_WhisperLib_fullTranscribe(
        JNIEnv *env, jobject thiz, jlong context_ptr, jstring language, jobject callback,
        jfloatArray audio_data) {
    UNUSED(thiz);
    struct whisper_context *context = (struct whisper_context *) context_ptr;
    jfloat *audio_data_arr = (*env)->GetFloatArrayElements(env, audio_data, NULL);
    const jsize audio_data_length = (*env)->GetArrayLength(env, audio_data);
    whisper_log_set(log_callback, NULL);
    // Leave 2 processors free (i.e. the high-efficiency cores).
    int max_threads = max(1, min(8, get_nprocs() - 2));
    LOGI("Selecting %d threads", max_threads);
    const char *la = (*env)->GetStringUTFChars(env, language, 0);
    LOGI("Transcribing language: %s ", la);
    // The below adapted from the Objective-C iOS sample
    struct whisper_full_params params = whisper_full_default_params(WHISPER_SAMPLING_GREEDY);

    callback_context *ctx = malloc(sizeof(callback_context));
    ctx->env = env;
    ctx->callback = callback;
    params.progress_callback = whisper_print_progress_callback;
    params.progress_callback_user_data = ctx;
    params.print_realtime = true;
    params.print_progress = false;
    params.print_timestamps = true;
    params.print_special = false;
    params.translate = false;
    params.language = la;
    params.n_threads = max_threads;
    params.offset_ms = 0;
    params.no_context = true;
    params.single_segment = false;

    whisper_reset_timings(context);

    LOGI("About to run whisper_full");
    if (whisper_full(context, params, audio_data_arr, audio_data_length) != 0) {
        LOGI("Failed to run the model");
    } else {
        whisper_print_timings(context);
    }
    (*env)->ReleaseFloatArrayElements(env, audio_data, audio_data_arr, JNI_ABORT);
    free(ctx);
}

JNIEXPORT jint JNICALL
Java_com_example_echolingua_whisper_WhisperLib_getTextSegmentCount(
        JNIEnv *env, jobject thiz, jlong context_ptr) {
    UNUSED(env);
    UNUSED(thiz);
    struct whisper_context *context = (struct whisper_context *) context_ptr;
    return whisper_full_n_segments(context);
}

const char *to_timestamp(int64_t t) {
    int64_t msec = t * 10;
    int64_t hr = msec / (1000 * 60 * 60);
    msec = msec - hr * (1000 * 60 * 60);
    int64_t min = msec / (1000 * 60);
    msec = msec - min * (1000 * 60);
    int64_t sec = msec / 1000;
    msec = msec - sec * 1000;

    char *buf = (char *) malloc(sizeof(char) * 32);

    snprintf(buf, 32, "%02d:%02d:%02d.%03d", (int) hr, (int) min, (int) sec,
             (int) msec);

    return buf;
}

JNIEXPORT jstring JNICALL
Java_com_example_echolingua_whisper_WhisperLib_getTextSegment(
        JNIEnv *env, jobject thiz, jlong context_ptr, jint index) {
    UNUSED(thiz);
    struct whisper_context *context = (struct whisper_context *) context_ptr;
    const char *text = whisper_full_get_segment_text(context, index);
    jstring string = (*env)->NewStringUTF(env, text);
    return string;
}