/**
 * 打开链接
 *
 * @param href 跳转链接
 * @param option 是否新窗口打开、是否取消跟踪
 */
export declare const linkTo: (href: string, option?: {
    _blank: boolean;
    nofollow: boolean;
}) => void;
/**
 * 网络文件下载，支持base64
 * 链接需要指定返回文件类型
 *
 * @param href 文件链接
 * @param title 下载文件名
 */
export declare const download: (href: string, title?: string) => void;
