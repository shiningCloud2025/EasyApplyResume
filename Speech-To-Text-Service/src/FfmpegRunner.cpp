#include"PathCch.h"
#include"FfmpegRunner.h"

#include<cstdlib>
#include<filesystem>
#include<string>

namespace stt{
    namespace{
        #ifdef _WIN32
        std::wstring quoteArgument(const std::filesystem::path& path){
            return L"\""+path.wstring()+L"\"";
        }
        int executeCommand(const std::wstring& command){
            return _wsystem(command.c_str());
        }
        #else
        std::string quoteArgument(const std::filesystem::path& path){
            return "\""+path.string()+"\"";
        }
        int executeCommand(const std::string& command){
            return std::system(command.c_str);
        }
        #endif
    }
}
