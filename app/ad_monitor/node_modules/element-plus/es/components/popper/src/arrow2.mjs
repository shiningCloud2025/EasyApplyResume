import { defineComponent, inject, onBeforeUnmount, openBlock, createElementBlock, normalizeClass, unref, normalizeStyle } from 'vue';
import { POPPER_CONTENT_INJECTION_KEY } from './constants.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';

const _sfc_main = defineComponent({
  ...{
    name: "ElPopperArrow",
    inheritAttrs: false
  },
  __name: "arrow",
  setup(__props, { expose: __expose }) {
    const ns = useNamespace("popper");
    const { arrowRef, arrowStyle } = inject(
      POPPER_CONTENT_INJECTION_KEY,
      void 0
    );
    onBeforeUnmount(() => {
      arrowRef.value = void 0;
    });
    __expose({
      arrowRef
    });
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "span",
        {
          ref_key: "arrowRef",
          ref: arrowRef,
          class: normalizeClass(unref(ns).e("arrow")),
          style: normalizeStyle(unref(arrowStyle)),
          "data-popper-arrow": ""
        },
        null,
        6
      );
    };
  }
});
var ElPopperArrow = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/popper/src/arrow.vue"]]);

export { ElPopperArrow as default };
//# sourceMappingURL=arrow2.mjs.map
