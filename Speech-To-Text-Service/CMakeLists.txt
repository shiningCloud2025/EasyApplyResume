cmake_minimum_required(VERSION 3.16)
project(SpeechToTextService)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_executable(SpeechToTextService
    main.cpp
    src/FfmpegRunner.cpp
)

target_include_directories(SpeechToTextService PRIVATE
      include
  )

  target_compile_definitions(SpeechToTextService PRIVATE
      STT_PROJECT_ROOT="${CMAKE_CURRENT_SOURCE_DIR}"
  )
