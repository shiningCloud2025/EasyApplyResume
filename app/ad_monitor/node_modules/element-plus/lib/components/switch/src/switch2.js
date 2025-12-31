'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var index$1 = require('../../icon/index.js');
var iconsVue = require('@element-plus/icons-vue');
var _switch = require('./switch.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var useFormItem = require('../../form/src/hooks/use-form-item.js');
var useFormCommonProps = require('../../form/src/hooks/use-form-common-props.js');
var index = require('../../../hooks/use-namespace/index.js');
var style = require('../../../utils/dom/style.js');
var event = require('../../../constants/event.js');
var error = require('../../../utils/error.js');
var shared = require('@vue/shared');
var types = require('../../../utils/types.js');

const _hoisted_1 = ["id", "aria-checked", "aria-disabled", "aria-label", "name", "true-value", "false-value", "disabled", "tabindex"];
const _hoisted_2 = ["aria-hidden"];
const _hoisted_3 = { key: 1 };
const _hoisted_4 = { key: 1 };
const _hoisted_5 = ["aria-hidden"];
const COMPONENT_NAME = "ElSwitch";
const _sfc_main = vue.defineComponent({
  ...{
    name: COMPONENT_NAME
  },
  __name: "switch",
  props: _switch.switchProps,
  emits: _switch.switchEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const { formItem } = useFormItem.useFormItem();
    const switchSize = useFormCommonProps.useFormSize();
    const ns = index.useNamespace("switch");
    const { inputId } = useFormItem.useFormItemInputId(props, {
      formItemContext: formItem
    });
    const switchDisabled = useFormCommonProps.useFormDisabled(
      vue.computed(() => {
        if (props.loading) {
          return true;
        }
        return void 0;
      })
    );
    const isControlled = vue.ref(props.modelValue !== false);
    const input = vue.shallowRef();
    const switchKls = vue.computed(() => [
      ns.b(),
      ns.m(switchSize.value),
      ns.is("disabled", switchDisabled.value),
      ns.is("checked", checked.value)
    ]);
    const labelLeftKls = vue.computed(() => [
      ns.e("label"),
      ns.em("label", "left"),
      ns.is("active", !checked.value)
    ]);
    const labelRightKls = vue.computed(() => [
      ns.e("label"),
      ns.em("label", "right"),
      ns.is("active", checked.value)
    ]);
    const coreStyle = vue.computed(() => ({
      width: style.addUnit(props.width)
    }));
    vue.watch(
      () => props.modelValue,
      () => {
        isControlled.value = true;
      }
    );
    const actualValue = vue.computed(() => {
      return isControlled.value ? props.modelValue : false;
    });
    const checked = vue.computed(() => actualValue.value === props.activeValue);
    if (![props.activeValue, props.inactiveValue].includes(actualValue.value)) {
      emit(event.UPDATE_MODEL_EVENT, props.inactiveValue);
      emit(event.CHANGE_EVENT, props.inactiveValue);
      emit(event.INPUT_EVENT, props.inactiveValue);
    }
    vue.watch(checked, (val) => {
      var _a;
      input.value.checked = val;
      if (props.validateEvent) {
        (_a = formItem == null ? void 0 : formItem.validate) == null ? void 0 : _a.call(formItem, "change").catch((err) => error.debugWarn(err));
      }
    });
    const handleChange = () => {
      const val = checked.value ? props.inactiveValue : props.activeValue;
      emit(event.UPDATE_MODEL_EVENT, val);
      emit(event.CHANGE_EVENT, val);
      emit(event.INPUT_EVENT, val);
      vue.nextTick(() => {
        input.value.checked = checked.value;
      });
    };
    const switchValue = () => {
      if (switchDisabled.value)
        return;
      const { beforeChange } = props;
      if (!beforeChange) {
        handleChange();
        return;
      }
      const shouldChange = beforeChange();
      const isPromiseOrBool = [
        shared.isPromise(shouldChange),
        types.isBoolean(shouldChange)
      ].includes(true);
      if (!isPromiseOrBool) {
        error.throwError(
          COMPONENT_NAME,
          "beforeChange must return type `Promise<boolean>` or `boolean`"
        );
      }
      if (shared.isPromise(shouldChange)) {
        shouldChange.then((result) => {
          if (result) {
            handleChange();
          }
        }).catch((e) => {
          error.debugWarn(COMPONENT_NAME, `some error occurred: ${e}`);
        });
      } else if (shouldChange) {
        handleChange();
      }
    };
    const focus = () => {
      var _a, _b;
      (_b = (_a = input.value) == null ? void 0 : _a.focus) == null ? void 0 : _b.call(_a);
    };
    vue.onMounted(() => {
      input.value.checked = checked.value;
    });
    __expose({
      focus,
      checked
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "div",
        {
          class: vue.normalizeClass(switchKls.value),
          onClick: vue.withModifiers(switchValue, ["prevent"])
        },
        [
          vue.createElementVNode("input", {
            id: vue.unref(inputId),
            ref_key: "input",
            ref: input,
            class: vue.normalizeClass(vue.unref(ns).e("input")),
            type: "checkbox",
            role: "switch",
            "aria-checked": checked.value,
            "aria-disabled": vue.unref(switchDisabled),
            "aria-label": _ctx.ariaLabel,
            name: _ctx.name,
            "true-value": _ctx.activeValue,
            "false-value": _ctx.inactiveValue,
            disabled: vue.unref(switchDisabled),
            tabindex: _ctx.tabindex,
            onChange: handleChange,
            onKeydown: vue.withKeys(switchValue, ["enter"])
          }, null, 42, _hoisted_1),
          !_ctx.inlinePrompt && (_ctx.inactiveIcon || _ctx.inactiveText || _ctx.$slots.inactive) ? (vue.openBlock(), vue.createElementBlock(
            "span",
            {
              key: 0,
              class: vue.normalizeClass(labelLeftKls.value)
            },
            [
              vue.renderSlot(_ctx.$slots, "inactive", {}, () => [
                _ctx.inactiveIcon ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), { key: 0 }, {
                  default: vue.withCtx(() => [
                    (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(_ctx.inactiveIcon)))
                  ]),
                  _: 1
                })) : vue.createCommentVNode("v-if", true),
                !_ctx.inactiveIcon && _ctx.inactiveText ? (vue.openBlock(), vue.createElementBlock("span", {
                  key: 1,
                  "aria-hidden": checked.value
                }, vue.toDisplayString(_ctx.inactiveText), 9, _hoisted_2)) : vue.createCommentVNode("v-if", true)
              ])
            ],
            2
          )) : vue.createCommentVNode("v-if", true),
          vue.createElementVNode(
            "span",
            {
              class: vue.normalizeClass(vue.unref(ns).e("core")),
              style: vue.normalizeStyle(coreStyle.value)
            },
            [
              _ctx.inlinePrompt ? (vue.openBlock(), vue.createElementBlock(
                "div",
                {
                  key: 0,
                  class: vue.normalizeClass(vue.unref(ns).e("inner"))
                },
                [
                  !checked.value ? (vue.openBlock(), vue.createElementBlock(
                    "div",
                    {
                      key: 0,
                      class: vue.normalizeClass(vue.unref(ns).e("inner-wrapper"))
                    },
                    [
                      vue.renderSlot(_ctx.$slots, "inactive", {}, () => [
                        _ctx.inactiveIcon ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), { key: 0 }, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(_ctx.inactiveIcon)))
                          ]),
                          _: 1
                        })) : vue.createCommentVNode("v-if", true),
                        !_ctx.inactiveIcon && _ctx.inactiveText ? (vue.openBlock(), vue.createElementBlock(
                          "span",
                          _hoisted_3,
                          vue.toDisplayString(_ctx.inactiveText),
                          1
                        )) : vue.createCommentVNode("v-if", true)
                      ])
                    ],
                    2
                  )) : (vue.openBlock(), vue.createElementBlock(
                    "div",
                    {
                      key: 1,
                      class: vue.normalizeClass(vue.unref(ns).e("inner-wrapper"))
                    },
                    [
                      vue.renderSlot(_ctx.$slots, "active", {}, () => [
                        _ctx.activeIcon ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), { key: 0 }, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(_ctx.activeIcon)))
                          ]),
                          _: 1
                        })) : vue.createCommentVNode("v-if", true),
                        !_ctx.activeIcon && _ctx.activeText ? (vue.openBlock(), vue.createElementBlock(
                          "span",
                          _hoisted_4,
                          vue.toDisplayString(_ctx.activeText),
                          1
                        )) : vue.createCommentVNode("v-if", true)
                      ])
                    ],
                    2
                  ))
                ],
                2
              )) : vue.createCommentVNode("v-if", true),
              vue.createElementVNode(
                "div",
                {
                  class: vue.normalizeClass(vue.unref(ns).e("action"))
                },
                [
                  _ctx.loading ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), {
                    key: 0,
                    class: vue.normalizeClass(vue.unref(ns).is("loading"))
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(vue.unref(iconsVue.Loading))
                    ]),
                    _: 1
                  }, 8, ["class"])) : checked.value ? vue.renderSlot(_ctx.$slots, "active-action", { key: 1 }, () => [
                    _ctx.activeActionIcon ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), { key: 0 }, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(_ctx.activeActionIcon)))
                      ]),
                      _: 1
                    })) : vue.createCommentVNode("v-if", true)
                  ]) : !checked.value ? vue.renderSlot(_ctx.$slots, "inactive-action", { key: 2 }, () => [
                    _ctx.inactiveActionIcon ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), { key: 0 }, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(_ctx.inactiveActionIcon)))
                      ]),
                      _: 1
                    })) : vue.createCommentVNode("v-if", true)
                  ]) : vue.createCommentVNode("v-if", true)
                ],
                2
              )
            ],
            6
          ),
          !_ctx.inlinePrompt && (_ctx.activeIcon || _ctx.activeText || _ctx.$slots.active) ? (vue.openBlock(), vue.createElementBlock(
            "span",
            {
              key: 1,
              class: vue.normalizeClass(labelRightKls.value)
            },
            [
              vue.renderSlot(_ctx.$slots, "active", {}, () => [
                _ctx.activeIcon ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), { key: 0 }, {
                  default: vue.withCtx(() => [
                    (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(_ctx.activeIcon)))
                  ]),
                  _: 1
                })) : vue.createCommentVNode("v-if", true),
                !_ctx.activeIcon && _ctx.activeText ? (vue.openBlock(), vue.createElementBlock("span", {
                  key: 1,
                  "aria-hidden": !checked.value
                }, vue.toDisplayString(_ctx.activeText), 9, _hoisted_5)) : vue.createCommentVNode("v-if", true)
              ])
            ],
            2
          )) : vue.createCommentVNode("v-if", true)
        ],
        2
      );
    };
  }
});
var Switch = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/switch/src/switch.vue"]]);

exports["default"] = Switch;
//# sourceMappingURL=switch2.js.map
