import { defineComponent, useSlots, computed, openBlock, createElementBlock, normalizeClass, unref, renderSlot } from 'vue';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { buildProps } from '../../../utils/vue/props/runtime.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';

const _sfc_main = defineComponent({
  ...{
    name: "ElContainer"
  },
  __name: "container",
  props: buildProps({
    direction: {
      type: String,
      values: ["horizontal", "vertical"]
    }
  }),
  setup(__props) {
    const props = __props;
    const slots = useSlots();
    const ns = useNamespace("container");
    const isVertical = computed(() => {
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
      return openBlock(), createElementBlock(
        "section",
        {
          class: normalizeClass([unref(ns).b(), unref(ns).is("vertical", isVertical.value)])
        },
        [
          renderSlot(_ctx.$slots, "default")
        ],
        2
      );
    };
  }
});
var Container = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/container/src/container.vue"]]);

export { Container as default };
//# sourceMappingURL=container.mjs.map
