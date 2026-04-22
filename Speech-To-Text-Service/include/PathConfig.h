#pragma once
#include <filesystem>

namespace stt{
    class PathConfig{
        public:
            static std::filesystem::path projectRoot(){
                #ifdef STT_PROJECT_ROOT
                    return std::filesystem::path(STT_PROJECT_ROOT);
                #else 
                    return std::filesystem::current_path();
                #endif
            }
            static std::filesystem::path tempDir(){
                return projectRoot() / "temp";
            }
            static std::filesystem::path ffmpegExecutable() {
                #ifdef _WIN32
                        return projectRoot() / "tools" / "ffmpeg" / "bin" / "ffmpeg.exe";
                #else
                        return "ffmpeg";
                #endif
          }
            static std::filesystem::path whisperModelPath() {
               return projectRoot() / "models" / "whisper" / "ggml-base.bin";
          }
    };
}