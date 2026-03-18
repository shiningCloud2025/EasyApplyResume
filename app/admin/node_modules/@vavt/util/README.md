# vavt-util

一个 JavaScript 工具库

```shell
yarn add @vavt/util
# or
npm i @vavt/util
```

## 使用

### deepClone

深克隆，支持大部分基础引用类型

```js
const newObj = deepClone({ a: 1 });
```

### debounce

函数防抖，默认延迟 200 毫秒

```js
const handler = debounce(() => {
  // 做点什么
}, 100);
```

### linkTo

模拟跳转

```js
linkTo('https://github.com/imzbf/vavt-util', {
  // 是否新窗口打开，默认true
  _blank: false,
  // 是否可访问opener，默认true
  nofollow: false
});
```

### download

基础下载，仅支持`get`，同时支持下载`base64`为图片

```js
download('https://github.com/imzbf/vavt-util/archive/refs/tags/v1.0.0.zip', 'v1.0.0.zip');

download('data:image/svg+xml,xxxxx', 'v1.0.0.png');
```

### smoothScroll

平滑滚动至指定高度

```js
smoothScroll(
  // 滚动元素
  document.documentElement,
  // 滚动至100px高度
  100,
  // 滚动结束回调
  () => {
    console.log('滚动结束');
  },
  // 是否延迟执行滚动结束回调，默认100毫秒
  50
);
```

创建独立的滚动方法

```js
import { createSmoothScroll } from '@vavt/util';
const smoothScroll = createSmoothScroll();
```

### throttle

函数节流，默认延迟 200 毫秒

```js
const handler = throttle(() => {
  // 做点什么
}, 100);
```

### searchToObj

将`location.search`转换为对象结构

```js
searchToObj('?age=18&name=lili&h=1&h=2');
// { age: 18, name: 'lili', h: [1, 2] }
```

### objToSearch

将对象转换为`location.search`结构

```js
objToSearch({ age: 18, name: 'lili', h: [1, 2] });
// 'age=18&name=lili&h=1&h=2'
```

### objectSort

将对象数组进行关键词排序

```js
objectSort(
  [
    { name: 'F', age: 25 },
    { name: 'D', age: null },
    { name: 'E', age: 20 },
    { name: 'H', age: 30 },
    { name: 'A', age: undefined },
    { name: 'J', age: 'b' },
    { name: 'K', age: 'c' },
    { name: 'I', age: 'a' },
    { name: 'B', age: undefined },
    { name: 'G', age: 25 },
    { name: 'C', age: undefined }
  ],
  (item) => item.age
);
// [
//   { name: 'A', age: undefined },
//   { name: 'B', age: undefined },
//   { name: 'C', age: undefined },
//   { name: 'D', age: null },
//   { name: 'E', age: 20 },
//   { name: 'F', age: 25 },
//   { name: 'G', age: 25 },
//   { name: 'H', age: 30 },
//   { name: 'I', age: 'a' },
//   { name: 'J', age: 'b' },
//   { name: 'K', age: 'c' }
// ];
```

### draggingScroll

按住元素时，拖拽滚动元素

```js
const removeListener = draggingScroll(document.getElementById('id'));

// removeListener()
```

### randomId

生成一串随机字符

```js
console.log(randomId());
// lmsimogsk7pukfcia4
```

### isNumber

判断一个字符串是否是数字，科学计数法也会被认为是数字

```js
console.log(isNumber('0.23e-1'));
// true
```

### deepMerge

深度合并对象，只有对象会被合并，其他类型均会被新的值替换

```js
const a = { a: 1, b: { c: 2, d: 3 }, e: 4 };
const b = { a: 1, b: { c: 5, f: 6 }, e: 4 };

console.log(deepMerge(a, b));
// { a: 1, b: { c: 5, d: 3, f: 6 }, e: 4 }
```

### getRootOffset

获取相对位置，默认获取到 html 标签的 offsetTop, offsetLeft

### isMobile

判断当前设备是否为移动端设备。依据：userAgent 包含移动端信息并且支持触摸事件，否则降级为使用屏幕宽度判断。

入参描述：

```ts
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
```

...更多待更新

## 开发

```shell
yarn dev
```

### 构建

```shell
# 同时构建es\cjs\umd版本
yarn build

# 构建指定版本
yarm build:es
```

### 文件夹说明

```
/dev             // 开发调试代码
/packages        // 组件库源码
  /xxx           // 工具
  /index.ts      // 统一导出
```
