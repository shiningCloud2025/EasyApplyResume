import { defineComponent, ref, computed, onMounted, watch, openBlock, createBlock, Transition, unref, withCtx, withDirectives, createElementVNode, normalizeClass, normalizeStyle, createCommentVNode, resolveDynamicComponent, renderSlot, createElementBlock, toDisplayString, Fragment, withModifiers, createVNode, vShow, nextTick } from 'vue';
import { useEventListener, useResizeObserver, useTimeoutFn } from '@vueuse/core';
import { ElBadge } from '../../badge/index.mjs';
import { ElIcon } from '../../icon/index.mjs';
import { messageProps, messageEmits, MESSAGE_DEFAULT_PLACEMENT } from './message.mjs';
import { getLastOffset, getOffsetOrSpace } from './instance.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useGlobalComponentSettings } from '../../config-provider/src/hooks/use-global-config.mjs';
import { TypeComponentsMap, TypeComponents } from '../../../utils/vue/icon.mjs';
import { getEventCode } from '../../../utils/dom/event.mjs';
import { EVENT_CODE } from '../../../constants/aria.mjs';

const _hoisted_1 = ["id"];
const _hoisted_2 = ["innerHTML"];
const _sfc_main = defineComponent({
  ...{
    name: "ElMessage"
  },
  __name: "message",
  props: messageProps,
  emits: messageEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const { Close } = TypeComponents;
    const props = __props;
    const emit = __emit;
    const isStartTransition = ref(false);
    const { ns, zIndex } = useGlobalComponentSettings("message");
    const { currentZIndex, nextZIndex } = zIndex;
    const messageRef = ref();
    const visible = ref(false);
    const height = ref(0);
    let stopTimer = void 0;
    const badgeType = computed(
      () => props.type ? props.type === "error" ? "danger" : props.type : "info"
    );
    const typeClass = computed(() => {
      const type = props.type;
      return { [ns.bm("icon", type)]: type && TypeComponentsMap[type] };
    });
    const iconComponent = computed(
      () => props.icon || TypeComponentsMap[props.type] || ""
    );
    const placement = computed(() => props.placement || MESSAGE_DEFAULT_PLACEMENT);
    const lastOffset = computed(() => getLastOffset(props.id, placement.value));
    const offset = computed(() => {
      return getOffsetOrSpace(props.id, props.offset, placement.value) + lastOffset.value;
    });
    const bottom = computed(() => height.value + offset.value);
    const horizontalClass = computed(() => {
      if (placement.value.includes("left"))
        return ns.is("left");
      if (placement.value.includes("right"))
        return ns.is("right");
      return ns.is("center");
    });
    const verticalProperty = computed(
      () => placement.value.startsWith("top") ? "top" : "bottom"
    );
    const customStyle = computed(() => ({
      [verticalProperty.value]: `${offset.value}px`,
      zIndex: currentZIndex.value
    }));
    function startTimer() {
      if (props.duration === 0)
        return;
      ({ stop: stopTimer } = useTimeoutFn(() => {
        close();
      }, props.duration));
    }
    function clearTimer() {
      stopTimer == null ? void 0 : stopTimer();
    }
    function close() {
      visible.value = false;
      nextTick(() => {
        var _a;
        if (!isStartTransition.value) {
          (_a = props.onClose) == null ? void 0 : _a.call(props);
          emit("destroy");
        }
      });
    }
    function keydown(event) {
      const code = getEventCode(event);
      if (code === EVENT_CODE.esc) {
        close();
      }
    }
    onMounted(() => {
      startTimer();
      nextZIndex();
      visible.value = true;
    });
    watch(
      () => props.repeatNum,
      () => {
        clearTimer();
        startTimer();
      }
    );
    useEventListener(document, "keydown", keydown);
    useResizeObserver(messageRef, () => {
      height.value = messageRef.value.getBoundingClientRect().height;
    });
    __expose({
      visible,
      bottom,
      close
    });
    return (_ctx, _cache) => {
      return openBlock(), createBlock(Transition, {
        name: unref(ns).b("fade"),
        onBeforeEnter: _cache[0] || (_cache[0] = ($event) => isStartTransition.value = true),
        onBeforeLeave: _ctx.onClose,
        onAfterLeave: _cache[1] || (_cache[1] = ($event) => _ctx.$emit("destroy")),
        persisted: ""
      }, {
        default: withCtx(() => [
          withDirectives(createElementVNode("div", {
            id: _ctx.id,
            ref_key: "messageRef",
            ref: messageRef,
            class: normalizeClass([
              unref(ns).b(),
              { [unref(ns).m(_ctx.type)]: _ctx.type },
              unref(ns).is("closable", _ctx.showClose),
              unref(ns).is("plain", _ctx.plain),
              unref(ns).is("bottom", verticalProperty.value === "bottom"),
              horizontalClass.value,
              _ctx.customClass
            ]),
            style: normalizeStyle(customStyle.value),
            role: "alert",
            onMouseenter: clearTimer,
            onMouseleave: startTimer
          }, [
            _ctx.repeatNum > 1 ? (openBlock(), createBlock(unref(ElBadge), {
              key: 0,
              value: _ctx.repeatNum,
              type: badgeType.value,
              class: normalizeClass(unref(ns).e("badge"))
            }, null, 8, ["value", "type", "class"])) : createCommentVNode("v-if", true),
            iconComponent.value ? (openBlock(), createBlock(unref(ElIcon), {
              key: 1,
              class: normalizeClass([unref(ns).e("icon"), typeClass.value])
            }, {
              default: withCtx(() => [
                (openBlock(), createBlock(resolveDynamicComponent(iconComponent.value)))
              ]),
              _: 1
            }, 8, ["class"])) : createCommentVNode("v-if", true),
            renderSlot(_ctx.$slots, "default", {}, () => [
              !_ctx.dangerouslyUseHTMLString ? (openBlock(), createElementBlock(
                "p",
                {
                  key: 0,
                  class: normalizeClass(unref(ns).e("content"))
                },
                toDisplayString(_ctx.message),
                3
              )) : (openBlock(), createElementBlock(
                Fragment,
                { key: 1 },
                [
                  createCommentVNode(" Caution here, message could've been compromised, never use user's input as message "),
                  createElementVNode("p", {
                    class: normalizeClass(unref(ns).e("content")),
                    innerHTML: _ctx.message
                  }, null, 10, _hoisted_2)
                ],
                2112
              ))
            ]),
            _ctx.showClose ? (openBlock(), createBlock(unref(ElIcon), {
              key: 2,
              class: normalizeClass(unref(ns).e("closeBtn")),
              onClick: withModifiers(close, ["stop"])
            }, {
              default: withCtx(() => [
                createVNode(unref(Close))
              ]),
              _: 1
            }, 8, ["class"])) : createCommentVNode("v-if", true)
          ], 46, _hoisted_1), [
            [vShow, visible.value]
          ])
        ]),
        _: 3
      }, 8, ["name", "onBeforeLeave"]);
    };
  }
});
var MessageConstructor = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/message/src/message.vue"]]);

export { MessageConstructor as default };
//# sourceMappingURL=message2.mjs.map
