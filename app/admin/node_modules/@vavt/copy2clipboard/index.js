(function (root, factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD 模块
    define([], factory);
  } else if (typeof exports === 'object' && typeof module !== 'undefined') {
    // CommonJS 模块
    module.exports = factory();
  } else {
    // 浏览器全局变量
    root.copy2Clipboard = factory();
  }
})(typeof self !== 'undefined' ? self : this, function () {
  return async function copy2Clipboard(text) {
    if (typeof text !== 'string') return;

    if (window.isSecureContext && navigator.clipboard) {
      return await navigator.clipboard.writeText(text);
    } else {
      const textarea = document.createElement('textarea');
      let successed = false;
      textarea.value = text;
      textarea.style.position = 'fixed';
      textarea.style.opacity = 0;
      textarea.style.zIndex = '-10000';
      textarea.style.top = '-10000';
      document.body.appendChild(textarea);
      textarea.select();
      successed = document.execCommand('copy');
      document.body.removeChild(textarea);

      if (successed) {
        return;
      }

      throw new Error('Failed to copy content via "execCommand"!');
    }
  };
});
