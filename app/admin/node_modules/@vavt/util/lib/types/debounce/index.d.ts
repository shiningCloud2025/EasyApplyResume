/**
 * 防抖方法封装
 *
 * @param fn 目标方法
 * @param ms 防抖延迟
 * @returns
 */
export declare const debounce: <T, V>(fn: (...params: Array<T>) => V, ms?: number) => (...params: Array<T>) => Promise<unknown>;
