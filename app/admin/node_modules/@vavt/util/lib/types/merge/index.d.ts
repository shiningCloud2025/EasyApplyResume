interface DeepMergeOptions {
    excludeKeys?: (key: string) => boolean;
}
/**
 * 深度合并对象与数组：对象字段与数组项都会按层级递归合并，其他类型均会被新的值替换。
 * 如果某层级的 key 被过滤，则执行替换而不继续深度合并。
 */
export declare const deepMerge: <T extends object, U extends object>(target: T, source: U, options?: DeepMergeOptions) => T & U;
export {};
