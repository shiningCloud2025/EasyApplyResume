import { defineComponent, shallowRef, ref, computed, watch, onMounted, watchEffect, openBlock, createElementBlock, normalizeClass, unref, normalizeStyle, createVNode, withCtx, createElementVNode, renderSlot, nextTick } from 'vue';
import { useWindowSize, useElementBounding, useEventListener } from '@vueuse/core';
import { ElTeleport } from '../../teleport/index.mjs';
import { affixProps, affixEmits } from './affix.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { getScrollContainer } from '../../../utils/dom/scroll.mjs';
import { addUnit } from '../../../utils/dom/style.mjs';
import { CHANGE_EVENT } from '../../../constants/event.mjs';
import { throwError } from '../../../utils/error.mjs';

const COMPONENT_NAME = "ElAffix";
const _sfc_main = defineComponent({
  ...{
    name: COMPONENT_NAME
  },
  __name: "affix",
  props: affixProps,
  emits: affixEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const ns = useNamespace("affix");
    const target = shallowRef();
    const root = shallowRef();
    const scrollContainer = shallowRef();
    const { height: windowHeight } = useWindowSize();
    const {
      height: rootHeight,
      width: rootWidth,
      top: rootTop,
      bottom: rootBottom,
      left: rootLeft,
      update: updateRoot
    } = useElementBounding(root, { windowScroll: false });
    const targetRect = useElementBounding(target);
    const fixed = ref(false);
    const scrollTop = ref(0);
    const transform = ref(0);
    const teleportDisabled = computed(() => {
      return !props.teleported || !fixed.value;
    });
    const rootStyle = computed(() => {
      return {
        height: fixed.value ? `${rootHeight.value}px` : "",
        width: fixed.value ? `${rootWidth.value}px` : ""
      };
    });
    const affixStyle = computed(() => {
      if (!fixed.value)
        return {};
      const offset = addUnit(props.offset);
      return {
        height: `${rootHeight.value}px`,
        width: `${rootWidth.value}px`,
        top: props.position === "top" ? offset : "",
        bottom: props.position === "bottom" ? offset : "",
        left: props.teleported ? `${rootLeft.value}px` : "",
        transform: transform.value ? `translateY(${transform.value}px)` : "",
        zIndex: props.zIndex
      };
    });
    const update = () => {
      if (!scrollContainer.value)
        return;
      scrollTop.value = scrollContainer.value instanceof Window ? document.documentElement.scrollTop : scrollContainer.value.scrollTop || 0;
      const { position, target: target2, offset } = props;
      const rootHeightOffset = offset + rootHeight.value;
      if (position === "top") {
        if (target2) {
          const difference = targetRect.bottom.value - rootHeightOffset;
          fixed.value = offset > rootTop.value && targetRect.bottom.value > 0;
          transform.value = difference < 0 ? difference : 0;
        } else {
          fixed.value = offset > rootTop.value;
        }
      } else if (target2) {
        const difference = windowHeight.value - targetRect.top.value - rootHeightOffset;
        fixed.value = windowHeight.value - offset < rootBottom.value && windowHeight.value > targetRect.top.value;
        transform.value = difference < 0 ? -difference : 0;
      } else {
        fixed.value = windowHeight.value - offset < rootBottom.value;
      }
    };
    const updateRootRect = async () => {
      if (!fixed.value) {
        updateRoot();
        return;
      }
      fixed.value = false;
      await nextTick();
      updateRoot();
      fixed.value = true;
    };
    const handleScroll = async () => {
      updateRoot();
      await nextTick();
      emit("scroll", {
        scrollTop: scrollTop.value,
        fixed: fixed.value
      });
    };
    watch(fixed, (val) => emit(CHANGE_EVENT, val));
    onMounted(() => {
      var _a;
      if (props.target) {
        target.value = (_a = document.querySelector(props.target)) != null ? _a : void 0;
        if (!target.value)
          throwError(COMPONENT_NAME, `Target does not exist: ${props.target}`);
      } else {
        target.value = document.documentElement;
      }
      scrollContainer.value = getScrollContainer(root.value, true);
      updateRoot();
    });
    useEventListener(scrollContainer, "scroll", handleScroll);
    watchEffect(update);
    __expose({
      update,
      updateRoot: updateRootRect
    });
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "div",
        {
          ref_key: "root",
          ref: root,
          class: normalizeClass(unref(ns).b()),
          style: normalizeStyle(rootStyle.value)
        },
        [
          createVNode(unref(ElTeleport), {
            disabled: teleportDisabled.value,
            to: _ctx.appendTo
          }, {
            default: withCtx(() => [
              createElementVNode(
                "div",
                {
                  class: normalizeClass({ [unref(ns).m("fixed")]: fixed.value }),
                  style: normalizeStyle(affixStyle.value)
                },
                [
                  renderSlot(_ctx.$slots, "default")
                ],
                6
              )
            ]),
            _: 3
          }, 8, ["disabled", "to"])
        ],
        6
      );
    };
  }
});
var Affix = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/affix/src/affix.vue"]]);

export { Affix as default };
//# sourceMappingURL=affix2.mjs.map
