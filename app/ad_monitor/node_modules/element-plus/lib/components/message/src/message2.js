'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var core = require('@vueuse/core');
var index = require('../../badge/index.js');
var index$1 = require('../../icon/index.js');
var message = require('./message.js');
var instance = require('./instance.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var useGlobalConfig = require('../../config-provider/src/hooks/use-global-config.js');
var icon = require('../../../utils/vue/icon.js');
var event = require('../../../utils/dom/event.js');
var aria = require('../../../constants/aria.js');

const _hoisted_1 = ["id"];
const _hoisted_2 = ["innerHTML"];
const _sfc_main = vue.defineComponent({
  ...{
    name: "ElMessage"
  },
  __name: "message",
  props: message.messageProps,
  emits: message.messageEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const { Close } = icon.TypeComponents;
    const props = __props;
    const emit = __emit;
    const isStartTransition = vue.ref(false);
    const { ns, zIndex } = useGlobalConfig.useGlobalComponentSettings("message");
    const { currentZIndex, nextZIndex } = zIndex;
    const messageRef = vue.ref();
    const visible = vue.ref(false);
    const height = vue.ref(0);
    let stopTimer = void 0;
    const badgeType = vue.computed(
      () => props.type ? props.type === "error" ? "danger" : props.type : "info"
    );
    const typeClass = vue.computed(() => {
      const type = props.type;
      return { [ns.bm("icon", type)]: type && icon.TypeComponentsMap[type] };
    });
    const iconComponent = vue.computed(
      () => props.icon || icon.TypeComponentsMap[props.type] || ""
    );
    const placement = vue.computed(() => props.placement || message.MESSAGE_DEFAULT_PLACEMENT);
    const lastOffset = vue.computed(() => instance.getLastOffset(props.id, placement.value));
    const offset = vue.computed(() => {
      return instance.getOffsetOrSpace(props.id, props.offset, placement.value) + lastOffset.value;
    });
    const bottom = vue.computed(() => height.value + offset.value);
    const horizontalClass = vue.computed(() => {
      if (placement.value.includes("left"))
        return ns.is("left");
      if (placement.value.includes("right"))
        return ns.is("right");
      return ns.is("center");
    });
    const verticalProperty = vue.computed(
      () => placement.value.startsWith("top") ? "top" : "bottom"
    );
    const customStyle = vue.computed(() => ({
      [verticalProperty.value]: `${offset.value}px`,
      zIndex: currentZIndex.value
    }));
    function startTimer() {
      if (props.duration === 0)
        return;
      ({ stop: stopTimer } = core.useTimeoutFn(() => {
        close();
      }, props.duration));
    }
    function clearTimer() {
      stopTimer == null ? void 0 : stopTimer();
    }
    function close() {
      visible.value = false;
      vue.nextTick(() => {
        var _a;
        if (!isStartTransition.value) {
          (_a = props.onClose) == null ? void 0 : _a.call(props);
          emit("destroy");
        }
      });
    }
    function keydown(event$1) {
      const code = event.getEventCode(event$1);
      if (code === aria.EVENT_CODE.esc) {
        close();
      }
    }
    vue.onMounted(() => {
      startTimer();
      nextZIndex();
      visible.value = true;
    });
    vue.watch(
      () => props.repeatNum,
      () => {
        clearTimer();
        startTimer();
      }
    );
    core.useEventListener(document, "keydown", keydown);
    core.useResizeObserver(messageRef, () => {
      height.value = messageRef.value.getBoundingClientRect().height;
    });
    __expose({
      visible,
      bottom,
      close
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createBlock(vue.Transition, {
        name: vue.unref(ns).b("fade"),
        onBeforeEnter: _cache[0] || (_cache[0] = ($event) => isStartTransition.value = true),
        onBeforeLeave: _ctx.onClose,
        onAfterLeave: _cache[1] || (_cache[1] = ($event) => _ctx.$emit("destroy")),
        persisted: ""
      }, {
        default: vue.withCtx(() => [
          vue.withDirectives(vue.createElementVNode("div", {
            id: _ctx.id,
            ref_key: "messageRef",
            ref: messageRef,
            class: vue.normalizeClass([
              vue.unref(ns).b(),
              { [vue.unref(ns).m(_ctx.type)]: _ctx.type },
              vue.unref(ns).is("closable", _ctx.showClose),
              vue.unref(ns).is("plain", _ctx.plain),
              vue.unref(ns).is("bottom", verticalProperty.value === "bottom"),
              horizontalClass.value,
              _ctx.customClass
            ]),
            style: vue.normalizeStyle(customStyle.value),
            role: "alert",
            onMouseenter: clearTimer,
            onMouseleave: startTimer
          }, [
            _ctx.repeatNum > 1 ? (vue.openBlock(), vue.createBlock(vue.unref(index.ElBadge), {
              key: 0,
              value: _ctx.repeatNum,
              type: badgeType.value,
              class: vue.normalizeClass(vue.unref(ns).e("badge"))
            }, null, 8, ["value", "type", "class"])) : vue.createCommentVNode("v-if", true),
            iconComponent.value ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), {
              key: 1,
              class: vue.normalizeClass([vue.unref(ns).e("icon"), typeClass.value])
            }, {
              default: vue.withCtx(() => [
                (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(iconComponent.value)))
              ]),
              _: 1
            }, 8, ["class"])) : vue.createCommentVNode("v-if", true),
            vue.renderSlot(_ctx.$slots, "default", {}, () => [
              !_ctx.dangerouslyUseHTMLString ? (vue.openBlock(), vue.createElementBlock(
                "p",
                {
                  key: 0,
                  class: vue.normalizeClass(vue.unref(ns).e("content"))
                },
                vue.toDisplayString(_ctx.message),
                3
              )) : (vue.openBlock(), vue.createElementBlock(
                vue.Fragment,
                { key: 1 },
                [
                  vue.createCommentVNode(" Caution here, message could've been compromised, never use user's input as message "),
                  vue.createElementVNode("p", {
                    class: vue.normalizeClass(vue.unref(ns).e("content")),
                    innerHTML: _ctx.message
                  }, null, 10, _hoisted_2)
                ],
                2112
              ))
            ]),
            _ctx.showClose ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), {
              key: 2,
              class: vue.normalizeClass(vue.unref(ns).e("closeBtn")),
              onClick: vue.withModifiers(close, ["stop"])
            }, {
              default: vue.withCtx(() => [
                vue.createVNode(vue.unref(Close))
              ]),
              _: 1
            }, 8, ["class"])) : vue.createCommentVNode("v-if", true)
          ], 46, _hoisted_1), [
            [vue.vShow, visible.value]
          ])
        ]),
        _: 3
      }, 8, ["name", "onBeforeLeave"]);
    };
  }
});
var MessageConstructor = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/message/src/message.vue"]]);

exports["default"] = MessageConstructor;
//# sourceMappingURL=message2.js.map
