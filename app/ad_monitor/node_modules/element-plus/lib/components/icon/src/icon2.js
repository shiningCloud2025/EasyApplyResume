'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var icon = require('./icon.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../hooks/use-namespace/index.js');
var style = require('../../../utils/dom/style.js');

const _sfc_main = vue.defineComponent({
  ...{
    name: "ElIcon",
    inheritAttrs: false
  },
  __name: "icon",
  props: icon.iconProps,
  setup(__props) {
    const props = __props;
    const ns = index.useNamespace("icon");
    const style$1 = vue.computed(() => {
      const { size, color } = props;
      const fontSize = style.addUnit(size);
      if (!fontSize && !color)
        return {};
      return {
        fontSize,
        "--color": color
      };
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "i",
        vue.mergeProps({
          class: vue.unref(ns).b(),
          style: style$1.value
        }, _ctx.$attrs),
        [
          vue.renderSlot(_ctx.$slots, "default")
        ],
        16
      );
    };
  }
});
var Icon = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/icon/src/icon.vue"]]);

exports["default"] = Icon;
//# sourceMappingURL=icon2.js.map
