/**
 * localtion.search 转对象
 *
 * @param search localtion.search eg: '?age=18&name=lili'
 * @param decode 是否需要解密
 * @returns
 */
export declare const searchToObj: (search: string, decode?: boolean) => {
    [key: string]: any;
};
/**
 *  对象转search
 *
 * @param obj 待转化对象
 * @param encode 是否加密 默认true
 * @returns
 */
export declare const objToSearch: (obj: any, encode?: boolean) => string;
