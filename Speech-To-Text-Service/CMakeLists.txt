cmake_minimum_required(VERSION 3.16)
project(SpeechToTextService LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)
set(WHISPER_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(WHISPER_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(WHISPER_BUILD_SERVER OFF CACHE BOOL "" FORCE)

add_subdirectory(third_party/whisper)

add_executable(SpeechToTextService
    main.cpp
    src/FfmpegRunner.cpp
    third_party/whisper/examples/common-whisper.cpp
)

target_include_directories(SpeechToTextService PRIVATE
    include
    third_party/whisper/include
    third_party/whisper/examples
)

target_link_libraries(SpeechToTextService PRIVATE whisper)

target_compile_definitions(SpeechToTextService PRIVATE
    STT_PROJECT_ROOT="${CMAKE_CURRENT_SOURCE_DIR}"
)

if (MSVC)
    target_compile_definitions(SpeechToTextService PRIVATE _CRT_SECURE_NO_WARNINGS)
endif()
