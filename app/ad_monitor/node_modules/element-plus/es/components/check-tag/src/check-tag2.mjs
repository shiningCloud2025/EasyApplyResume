import { defineComponent, computed, openBlock, createElementBlock, normalizeClass, renderSlot } from 'vue';
import { checkTagProps, checkTagEmits } from './check-tag.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { CHANGE_EVENT } from '../../../constants/event.mjs';

const _sfc_main = defineComponent({
  ...{
    name: "ElCheckTag"
  },
  __name: "check-tag",
  props: checkTagProps,
  emits: checkTagEmits,
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const ns = useNamespace("check-tag");
    const containerKls = computed(() => [
      ns.b(),
      ns.is("checked", props.checked),
      ns.is("disabled", props.disabled),
      ns.m(props.type || "primary")
    ]);
    const handleChange = () => {
      if (props.disabled)
        return;
      const checked = !props.checked;
      emit(CHANGE_EVENT, checked);
      emit("update:checked", checked);
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "span",
        {
          class: normalizeClass(containerKls.value),
          onClick: handleChange
        },
        [
          renderSlot(_ctx.$slots, "default")
        ],
        2
      );
    };
  }
});
var CheckTag = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/check-tag/src/check-tag.vue"]]);

export { CheckTag as default };
//# sourceMappingURL=check-tag2.mjs.map
