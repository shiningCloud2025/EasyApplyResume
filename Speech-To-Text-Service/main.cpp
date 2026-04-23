#include "FfmpegRunner.h"
#include "PathConfig.h"

#include "common-whisper.h"
#include "whisper.h"

#include <algorithm>
#include <filesystem>
#include <iostream>
#include <sstream>
#include <string>
#include <thread>
#include <vector>

namespace {
std::filesystem::path resolveInputPath(int argc, char ** argv) {
    if (argc > 1) {
        return std::filesystem::absolute(std::filesystem::path(argv[1]));
    }
    return stt::PathConfig::projectRoot() / "标准录音 1.mp3";
}

int resolveThreadCount() {
    const unsigned int hardwareThreads = std::thread::hardware_concurrency();
    const unsigned int safeThreads = hardwareThreads == 0 ? 1 : hardwareThreads;
    return static_cast<int>(std::min(4u, safeThreads));
}

std::string collectTranscriptionText(whisper_context * context) {
    std::ostringstream text;
    const int segmentCount = whisper_full_n_segments(context);
    for (int index = 0; index < segmentCount; ++index) {
        const char * segmentText = whisper_full_get_segment_text(context, index);
        if (segmentText == nullptr) {
            continue;
        }
        if (index > 0) {
            text << '\n';
        }
        text << segmentText;
    }
    return text.str();
}
} // namespace

int main(int argc, char ** argv) {
    const auto inputPath = resolveInputPath(argc, argv);
    const auto outputPath = stt::PathConfig::tempDir() / "task_001.wav";
    const auto modelPath = stt::PathConfig::whisperModelPath();

    if (!std::filesystem::exists(inputPath)) {
        std::cerr << "输入音频不存在: " << inputPath.string() << std::endl;
        return 1;
    }

    if (!std::filesystem::exists(modelPath)) {
        std::cerr << "Whisper 模型不存在: " << modelPath.string() << std::endl;
        return 1;
    }

    std::filesystem::create_directories(stt::PathConfig::tempDir());

    stt::FfmpegRunner ffmpegRunner;
    const auto transcodeResult = ffmpegRunner.transcodeToWav(inputPath, outputPath);
    if (!transcodeResult.success) {
        std::cerr << "FFmpeg 转码失败: " << transcodeResult.errorMessage << std::endl;
        return 1;
    }

    std::vector<float> pcmf32;
    std::vector<std::vector<float>> pcmf32s;
    if (!read_audio_data(outputPath.string(), pcmf32, pcmf32s, false)) {
        std::cerr << "读取标准化音频失败: " << outputPath.string() << std::endl;
        return 1;
    }

    if (pcmf32.empty()) {
        std::cerr << "标准化音频为空，无法继续识别。" << std::endl;
        return 1;
    }

    whisper_context_params contextParams = whisper_context_default_params();
    contextParams.use_gpu = false;
    contextParams.flash_attn = false;
    contextParams.gpu_device = 0;

    whisper_context * context = whisper_init_from_file_with_params(modelPath.string().c_str(), contextParams);
    if (context == nullptr) {
        std::cerr << "Whisper 模型加载失败: " << modelPath.string() << std::endl;
        return 1;
    }

    whisper_full_params params = whisper_full_default_params(WHISPER_SAMPLING_GREEDY);
    params.n_threads = resolveThreadCount();
    params.translate = false;
    params.no_timestamps = true;
    params.print_realtime = false;
    params.print_progress = false;
    params.print_timestamps = false;
    params.single_segment = false;
    params.detect_language = true;
    params.language = "auto";

    const int recognizeCode = whisper_full(context, params, pcmf32.data(), static_cast<int>(pcmf32.size()));
    if (recognizeCode != 0) {
        whisper_free(context);
        std::cerr << "Whisper 识别失败，错误码: " << recognizeCode << std::endl;
        return 1;
    }

    const std::string transcriptionText = collectTranscriptionText(context);
    if (transcriptionText.empty()) {
        whisper_free(context);
        std::cerr << "Whisper 识别完成，但未生成文本结果。" << std::endl;
        return 1;
    }

    std::cout << "转码输出文件: " << transcodeResult.outputPath.string() << std::endl;
    std::cout << "识别结果:" << std::endl;
    std::cout << transcriptionText << std::endl;
    whisper_print_timings(context);
    whisper_free(context);
    return 0;
}
