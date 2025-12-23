'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var constants = require('./constants.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../hooks/use-namespace/index.js');

const _sfc_main = vue.defineComponent({
  ...{
    name: "ElPopperArrow",
    inheritAttrs: false
  },
  __name: "arrow",
  setup(__props, { expose: __expose }) {
    const ns = index.useNamespace("popper");
    const { arrowRef, arrowStyle } = vue.inject(
      constants.POPPER_CONTENT_INJECTION_KEY,
      void 0
    );
    vue.onBeforeUnmount(() => {
      arrowRef.value = void 0;
    });
    __expose({
      arrowRef
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "span",
        {
          ref_key: "arrowRef",
          ref: arrowRef,
          class: vue.normalizeClass(vue.unref(ns).e("arrow")),
          style: vue.normalizeStyle(vue.unref(arrowStyle)),
          "data-popper-arrow": ""
        },
        null,
        6
      );
    };
  }
});
var ElPopperArrow = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/popper/src/arrow.vue"]]);

exports["default"] = ElPopperArrow;
//# sourceMappingURL=arrow2.js.map
