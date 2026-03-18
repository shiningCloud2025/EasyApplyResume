# @vavt/copy2clipboard

Use `navigator.clipboard` to copy content, use `execCommand` when it doesn't work compatibility.

## Install

```shell
yarn add @vavt/copy2clipboard
```

Or

```shell
npm i @vavt/copy2clipboard
```

## Usage

```js
import copy2clipboard from '@vavt/copy2clipboard';

copy2clipboard('Hello!')
  .then(() => {
    console.log('✅ Successed!');
  })
  .catch(() => {
    console.log('❌ Failed!');
  });
```
