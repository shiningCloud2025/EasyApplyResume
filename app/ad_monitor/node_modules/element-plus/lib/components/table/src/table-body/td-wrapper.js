'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var pluginVue_exportHelper = require('../../../../_virtual/plugin-vue_export-helper.js');

const _hoisted_1 = ["colspan", "rowspan"];
const _sfc_main = vue.defineComponent({
  ...{
    name: "TableTdWrapper"
  },
  __name: "td-wrapper",
  props: {
    colspan: {
      type: Number,
      default: 1
    },
    rowspan: {
      type: Number,
      default: 1
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock("td", {
        colspan: __props.colspan,
        rowspan: __props.rowspan
      }, [
        vue.renderSlot(_ctx.$slots, "default")
      ], 8, _hoisted_1);
    };
  }
});
var TdWrapper = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/table/src/table-body/td-wrapper.vue"]]);

exports["default"] = TdWrapper;
//# sourceMappingURL=td-wrapper.js.map
