import { defineComponent, openBlock, createElementBlock, renderSlot } from 'vue';
import _export_sfc from '../../../../_virtual/plugin-vue_export-helper.mjs';

const _hoisted_1 = ["colspan", "rowspan"];
const _sfc_main = defineComponent({
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
      return openBlock(), createElementBlock("td", {
        colspan: __props.colspan,
        rowspan: __props.rowspan
      }, [
        renderSlot(_ctx.$slots, "default")
      ], 8, _hoisted_1);
    };
  }
});
var TdWrapper = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/table/src/table-body/td-wrapper.vue"]]);

export { TdWrapper as default };
//# sourceMappingURL=td-wrapper.mjs.map
