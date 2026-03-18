/**
 * 创建独立的方法
 *
 * @returns
 */
export declare const createSmoothScroll: () => (ele: HTMLElement, top: number, cb?: () => void, timeout?: number | boolean) => void;
/**
 * 平滑滚动
 *
 * @param ele 滚动容器
 * @param top 目标位置
 * @param cb 执行完成时的回执事件
 * @param timeout 延迟执行回执事件毫秒值，false不延迟
 */
export declare const smoothScroll: (ele: HTMLElement, top: number, cb?: () => void, timeout?: number | boolean) => void;
