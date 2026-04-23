#include "FfmpegRunner.h"
#include "PathConfig.h"

#include <cstdlib>
#include <filesystem>
#include <sstream>
#include <string>
#include <system_error>

namespace stt {
namespace {
#ifdef _WIN32
std::wstring quoteArgument(const std::filesystem::path & path) {
    return L"\"" + path.wstring() + L"\"";
}

int executeCommand(const std::wstring & command) {
    return _wsystem(command.c_str());
}
#else
std::string quoteArgument(const std::filesystem::path & path) {
    return "\"" + path.string() + "\"";
}

int executeCommand(const std::string & command) {
    return std::system(command.c_str());
}
#endif
} // namespace

FfmpegResult FfmpegRunner::transcodeToWav(const std::filesystem::path & inputPath,
                                          const std::filesystem::path & outputPath) const {
    FfmpegResult result;
    result.outputPath = outputPath;

    if (!std::filesystem::exists(inputPath)) {
        result.errorMessage = "输入音频文件不存在: " + inputPath.string();
        return result;
    }

    std::error_code errorCode;
    if (outputPath.has_parent_path()) {
        std::filesystem::create_directories(outputPath.parent_path(), errorCode);
        if (errorCode) {
            result.errorMessage = "创建输出目录失败: " + errorCode.message();
            return result;
        }
    }

    std::filesystem::remove(outputPath, errorCode);
    errorCode.clear();

    const auto ffmpegPath = PathConfig::ffmpegExecutable();
#ifdef _WIN32
    if (!std::filesystem::exists(ffmpegPath)) {
        result.errorMessage = "FFmpeg 可执行文件不存在: " + ffmpegPath.string();
        return result;
    }

    const std::wstring command =
        quoteArgument(ffmpegPath) +
        L" -y -hide_banner -loglevel error -i " + quoteArgument(inputPath) +
        L" -vn -ar 16000 -ac 1 -c:a pcm_s16le " + quoteArgument(outputPath);
#else
    const std::string command =
        quoteArgument(ffmpegPath) +
        " -y -hide_banner -loglevel error -i " + quoteArgument(inputPath) +
        " -vn -ar 16000 -ac 1 -c:a pcm_s16le " + quoteArgument(outputPath);
#endif

    result.exitCode = executeCommand(command);
    if (result.exitCode != 0) {
        std::ostringstream message;
        message << "FFmpeg 执行失败，退出码: " << result.exitCode;
        result.errorMessage = message.str();
        return result;
    }

    if (!std::filesystem::exists(outputPath)) {
        result.errorMessage = "FFmpeg 执行完成，但未生成输出文件: " + outputPath.string();
        return result;
    }

    result.success = true;
    return result;
}
} // namespace stt
