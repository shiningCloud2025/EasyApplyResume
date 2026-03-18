interface MobileCheckOptions {
    /**
     * 是否强制使用屏幕宽度检查。
     * 默认为 false。
     */
    forceScreenWidth?: boolean;
    /**
     * 自定义的屏幕宽度阈值（单位为像素）。
     * 默认为 768 像素。
     */
    threshold?: number;
    /**
     * 当 User-Agent 检查结果为 false 时，是否自动降级使用屏幕宽度检查。
     * 默认为 true。
     */
    fallbackToScreenWidth?: boolean;
}
/**
 * 判断当前设备是否为移动端设备。
 * 依据：userAgent包含移动端信息并且支持触摸事件，否则降级为使用屏幕宽度判断
 *
 * @param options - 可选参数的对象，用于配置判断逻辑。
 * @returns 如果判断为移动端设备则返回 true，否则返回 false。
 */
export declare const isMobile: (options?: MobileCheckOptions) => boolean;
export {};
