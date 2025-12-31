import { defineComponent, ref, watch, toRef, computed, inject, openBlock, createElementBlock, normalizeStyle, unref, normalizeClass, createVNode, withCtx, renderSlot, createCommentVNode } from 'vue';
import ElFocusTrap from '../../focus-trap/src/focus-trap.mjs';
import { tourContentProps, tourContentEmits } from './content.mjs';
import { useFloating, tourKey } from './helper.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';

const _hoisted_1 = ["data-side"];
const _sfc_main = defineComponent({
  ...{
    name: "ElTourContent"
  },
  __name: "content",
  props: tourContentProps,
  emits: tourContentEmits,
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const placement = ref(props.placement);
    const strategy = ref(props.strategy);
    const contentRef = ref(null);
    const arrowRef = ref(null);
    watch(
      () => props.placement,
      () => {
        placement.value = props.placement;
      }
    );
    const { contentStyle, arrowStyle } = useFloating(
      toRef(props, "reference"),
      contentRef,
      arrowRef,
      placement,
      strategy,
      toRef(props, "offset"),
      toRef(props, "zIndex"),
      toRef(props, "showArrow")
    );
    const side = computed(() => {
      return placement.value.split("-")[0];
    });
    const { ns } = inject(tourKey);
    const onCloseRequested = () => {
      emit("close");
    };
    const onFocusoutPrevented = (event) => {
      if (event.detail.focusReason === "pointer") {
        event.preventDefault();
      }
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock("div", {
        ref_key: "contentRef",
        ref: contentRef,
        style: normalizeStyle(unref(contentStyle)),
        class: normalizeClass(unref(ns).e("content")),
        "data-side": side.value,
        tabindex: "-1"
      }, [
        createVNode(unref(ElFocusTrap), {
          loop: "",
          trapped: "",
          "focus-start-el": "container",
          "focus-trap-el": contentRef.value || void 0,
          onReleaseRequested: onCloseRequested,
          onFocusoutPrevented
        }, {
          default: withCtx(() => [
            renderSlot(_ctx.$slots, "default")
          ]),
          _: 3
        }, 8, ["focus-trap-el"]),
        _ctx.showArrow ? (openBlock(), createElementBlock(
          "span",
          {
            key: 0,
            ref_key: "arrowRef",
            ref: arrowRef,
            style: normalizeStyle(unref(arrowStyle)),
            class: normalizeClass(unref(ns).e("arrow"))
          },
          null,
          6
        )) : createCommentVNode("v-if", true)
      ], 14, _hoisted_1);
    };
  }
});
var ElTourContent = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/tour/src/content.vue"]]);

export { ElTourContent as default };
//# sourceMappingURL=content2.mjs.map
