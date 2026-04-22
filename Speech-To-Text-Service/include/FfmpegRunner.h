#pragma once

#include <filesystem>
#include <string>

namespace stt{
    struct FfmpegResult{
        bool success = false;
        int exitCode = -1;
        std::filesystem::path outputPath;
        std::string errorMessage;
    };
    
    class FfmpegRunner{
        public:
            FfmpegResult transcodeToWav(const std::filesystem::path& inputPath,
                                        const std::filesystem::path& outputPath) const;
    };
}