type Primitive = string | number | boolean | null | undefined | symbol | bigint;
/**
 * 判断对象是否是基础类型，Function也等同
 *
 * @param obj
 * @returns
 */
export declare const isPrimitive: (obj: unknown) => obj is Primitive;
/**
 * 针对常见的数据类型进行深克隆
 *
 * @param obj
 * @returns
 */
export declare const deepClone: <T>(obj: T, hash?: WeakMap<WeakKey, any>) => T;
export {};
