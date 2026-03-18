/**
 * 节流函数
 *
 * @param fn 目标方法
 * @param ms 节流延迟
 * @returns
 */
export declare const throttle: <T, V>(fn: (...params: Array<T>) => V, ms?: number) => (...params: Array<T>) => void;
