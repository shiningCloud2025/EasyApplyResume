package com.zyh.easyapplyresume;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * test如果误删
 * 打开项目结构设置：
 * File → Project Structure（或快捷键 Ctrl+Alt+Shift+S）。
 * 进入模块配置：
 * 左侧选择 Modules → 选中你的项目模块 → 切换到 Paths 标签页。
 * 禁用 IDE 的默认输出目录：
 * 取消勾选 Use module compile output path（此时会显示 “Inherit project compile output path”）；
 * 确保下方的 “Project compile output” 指向 Maven 的target目录（如果未设置，点击右侧 “…” 选择项目根目录下的target）。
 * 配置完成后，模块的编译输出会完全继承项目的设置，而项目会使用 Maven 的target目录，不再生成out。
 * 然后再改回来
 *
 * 打开设置：File → Settings（或 Ctrl+Alt+S）。
 * 进入构建工具配置：
 * 左侧导航栏选择 Build, Execution, Deployment → Build Tools → Maven → Runner。
 * 勾选 Delegate IDE build/run actions to Maven（将 IDE 的构建 / 运行操作委托给 Maven）。
 * 这样，无论是点击 IDE 的 “运行”“编译” 按钮，还是使用快捷键，都会调用 Maven 命令，产物只会输出到target目录。
 * 最后改回来
 *
 * 删除残留的out目录并清理缓存
 * 手动删除项目根目录下的out文件夹（如果存在）。
 * 清理 IDE 缓存（避免旧配置干扰）：
 * File → Invalidate Caches...
 * 勾选 “Clear file system cache and local history” → 点击 “Invalidate and Restart”，重启 IDE。
 */
@SpringBootTest
class EasyApplyResumeApplicationTests {

    @Test
    void contextLoads() {
    }

}
