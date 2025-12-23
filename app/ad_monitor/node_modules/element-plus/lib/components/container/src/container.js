'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var runtime = require('../../../utils/vue/props/runtime.js');
var index = require('../../../hooks/use-namespace/index.js');

const _sfc_main = vue.defineComponent({
  ...{
    name: "ElContainer"
  },
  __name: "container",
  props: runtime.buildProps({
    direction: {
      type: String,
      values: ["horizontal", "vertical"]
    }
  }),
  setup(__props) {
    const props = __props;
    const slots = vue.useSlots();
    const ns = index.useNamespace("container");
    const isVertical = vue.computed(() => {
      if (props.direction === "vertical") {
        return true;
      } else if (props.direction === "horizontal") {
        return false;
      }
      if (slots && slots.default) {
        const vNodes = slots.default();
        return vNodes.some((vNode) => {
          const tag = vNode.type.name;
          return tag === "ElHeader" || tag === "ElFooter";
        });
      } else {
        return false;
      }
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "section",
        {
          class: vue.normalizeClass([vue.unref(ns).b(), vue.unref(ns).is("vertical", isVertical.value)])
        },
        [
          vue.renderSlot(_ctx.$slots, "default")
        ],
        2
      );
    };
  }
});
var Container = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/container/src/container.vue"]]);

exports["default"] = Container;
//# sourceMappingURL=container.js.map
