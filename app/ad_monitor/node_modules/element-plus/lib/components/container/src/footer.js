'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../hooks/use-namespace/index.js');

const _sfc_main = vue.defineComponent({
  ...{
    name: "ElFooter"
  },
  __name: "footer",
  props: {
    height: {
      type: String,
      default: null
    }
  },
  setup(__props) {
    const props = __props;
    const ns = index.useNamespace("footer");
    const style = vue.computed(
      () => props.height ? ns.cssVarBlock({ height: props.height }) : {}
    );
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "footer",
        {
          class: vue.normalizeClass(vue.unref(ns).b()),
          style: vue.normalizeStyle(style.value)
        },
        [
          vue.renderSlot(_ctx.$slots, "default")
        ],
        6
      );
    };
  }
});
var Footer = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/container/src/footer.vue"]]);

exports["default"] = Footer;
//# sourceMappingURL=footer.js.map
