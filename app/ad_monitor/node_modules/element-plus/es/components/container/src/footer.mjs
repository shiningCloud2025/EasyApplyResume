import { defineComponent, computed, openBlock, createElementBlock, normalizeClass, unref, normalizeStyle, renderSlot } from 'vue';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';

const _sfc_main = defineComponent({
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
    const ns = useNamespace("footer");
    const style = computed(
      () => props.height ? ns.cssVarBlock({ height: props.height }) : {}
    );
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "footer",
        {
          class: normalizeClass(unref(ns).b()),
          style: normalizeStyle(style.value)
        },
        [
          renderSlot(_ctx.$slots, "default")
        ],
        6
      );
    };
  }
});
var Footer = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/container/src/footer.vue"]]);

export { Footer as default };
//# sourceMappingURL=footer.mjs.map
