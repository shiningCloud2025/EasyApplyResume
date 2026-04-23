#include "FfmpegRunner.h"
#include "PathConfig.h"

#include<iostream>


int main(){
      const auto inputPath = stt::PathConfig::projectRoot() / "标准录音 1.mp3";
      const auto outputPath = stt::PathConfig::tempDir() / "task_001.wav";

      stt::FfmpegRunner ffmpegRunner;
      const auto result = ffmpegRunner.transcodeToWav(inputPath, outputPath);

      if (!result.success) {
          std::cerr << "FFmpeg 转码失败: " << result.errorMessage << std::endl;
          return 1;
      }

      std::cout << "FFmpeg 转码成功: " << result.outputPath.string() << std::endl;
      return 0;
}
