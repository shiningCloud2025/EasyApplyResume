/**
 * 对对象列表按照某个字段进行排序
 *
 * @param list 待排序的列表
 * @param getParam 获取用于排序的字段值
 * @param order 排序方式，默认升序
 * @returns
 */
export declare const objectSort: <T>(list: Array<T>, getParam: (item: T) => unknown, order?: 'asc' | 'desc') => Array<T>;
