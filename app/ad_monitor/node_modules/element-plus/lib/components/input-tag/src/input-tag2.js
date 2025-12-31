'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var index$3 = require('../../tooltip/index.js');
var index$4 = require('../../icon/index.js');
var index$2 = require('../../tag/index.js');
var inputTag = require('./input-tag.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var useInputTag = require('./composables/use-input-tag.js');
var useHovering = require('./composables/use-hovering.js');
var index$1 = require('../../../hooks/use-calc-input-width/index.js');
var useDragTag = require('./composables/use-drag-tag.js');
var useInputTagDom = require('./composables/use-input-tag-dom.js');
var index = require('../../../hooks/use-attrs/index.js');
var useFormItem = require('../../form/src/hooks/use-form-item.js');
var icon = require('../../../utils/vue/icon.js');
var shared = require('@vue/shared');

const _hoisted_1 = ["id", "minlength", "maxlength", "disabled", "readonly", "autocomplete", "tabindex", "placeholder", "autofocus", "ariaLabel"];
const _hoisted_2 = ["textContent"];
const _sfc_main = vue.defineComponent({
  ...{
    name: "ElInputTag",
    inheritAttrs: false
  },
  __name: "input-tag",
  props: inputTag.inputTagProps,
  emits: inputTag.inputTagEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const attrs = index.useAttrs();
    const slots = vue.useSlots();
    const { form, formItem } = useFormItem.useFormItem();
    const { inputId } = useFormItem.useFormItemInputId(props, { formItemContext: formItem });
    const needStatusIcon = vue.computed(() => {
      var _a;
      return (_a = form == null ? void 0 : form.statusIcon) != null ? _a : false;
    });
    const validateState = vue.computed(() => (formItem == null ? void 0 : formItem.validateState) || "");
    const validateIcon = vue.computed(() => {
      return validateState.value && icon.ValidateComponentsMap[validateState.value];
    });
    const {
      inputRef,
      wrapperRef,
      tagTooltipRef,
      isFocused,
      inputValue,
      size,
      tagSize,
      placeholder,
      closable,
      disabled,
      showTagList,
      collapseTagList,
      handleDragged,
      handleInput,
      handleKeydown,
      handleKeyup,
      handleRemoveTag,
      handleClear,
      handleCompositionStart,
      handleCompositionUpdate,
      handleCompositionEnd,
      focus,
      blur
    } = useInputTag.useInputTag({ props, emit, formItem });
    const { hovering, handleMouseEnter, handleMouseLeave } = useHovering.useHovering();
    const { calculatorRef, inputStyle } = index$1.useCalcInputWidth();
    const {
      dropIndicatorRef,
      showDropIndicator,
      handleDragStart,
      handleDragOver,
      handleDragEnd
    } = useDragTag.useDragTag({ wrapperRef, handleDragged, afterDragged: focus });
    const {
      ns,
      nsInput,
      containerKls,
      containerStyle,
      innerKls,
      showClear,
      showSuffix,
      tagStyle,
      collapseItemRef,
      innerRef
    } = useInputTagDom.useInputTagDom({
      props,
      hovering,
      isFocused,
      inputValue,
      disabled,
      size,
      validateState,
      validateIcon,
      needStatusIcon
    });
    __expose({
      focus,
      blur
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "div",
        {
          ref_key: "wrapperRef",
          ref: wrapperRef,
          class: vue.normalizeClass(vue.unref(containerKls)),
          style: vue.normalizeStyle(vue.unref(containerStyle)),
          onMouseenter: _cache[8] || (_cache[8] = (...args) => vue.unref(handleMouseEnter) && vue.unref(handleMouseEnter)(...args)),
          onMouseleave: _cache[9] || (_cache[9] = (...args) => vue.unref(handleMouseLeave) && vue.unref(handleMouseLeave)(...args))
        },
        [
          vue.unref(slots).prefix ? (vue.openBlock(), vue.createElementBlock(
            "div",
            {
              key: 0,
              class: vue.normalizeClass(vue.unref(ns).e("prefix"))
            },
            [
              vue.renderSlot(_ctx.$slots, "prefix")
            ],
            2
          )) : vue.createCommentVNode("v-if", true),
          vue.createElementVNode(
            "div",
            {
              ref_key: "innerRef",
              ref: innerRef,
              class: vue.normalizeClass(vue.unref(innerKls))
            },
            [
              (vue.openBlock(true), vue.createElementBlock(
                vue.Fragment,
                null,
                vue.renderList(vue.unref(showTagList), (item, index) => {
                  return vue.openBlock(), vue.createBlock(vue.unref(index$2.ElTag), {
                    key: index,
                    size: vue.unref(tagSize),
                    closable: vue.unref(closable),
                    type: _ctx.tagType,
                    effect: _ctx.tagEffect,
                    draggable: vue.unref(closable) && _ctx.draggable,
                    style: vue.normalizeStyle(vue.unref(tagStyle)),
                    "disable-transitions": "",
                    onClose: ($event) => vue.unref(handleRemoveTag)(index),
                    onDragstart: (event) => vue.unref(handleDragStart)(event, index),
                    onDragover: (event) => vue.unref(handleDragOver)(event, index),
                    onDragend: vue.unref(handleDragEnd),
                    onDrop: _cache[0] || (_cache[0] = vue.withModifiers(() => {
                    }, ["stop"]))
                  }, {
                    default: vue.withCtx(() => [
                      vue.renderSlot(_ctx.$slots, "tag", {
                        value: item,
                        index
                      }, () => [
                        vue.createTextVNode(
                          vue.toDisplayString(item),
                          1
                        )
                      ])
                    ]),
                    _: 2
                  }, 1032, ["size", "closable", "type", "effect", "draggable", "style", "onClose", "onDragstart", "onDragover", "onDragend"]);
                }),
                128
              )),
              _ctx.collapseTags && _ctx.modelValue && _ctx.modelValue.length > _ctx.maxCollapseTags ? (vue.openBlock(), vue.createBlock(vue.unref(index$3.ElTooltip), {
                key: 0,
                ref_key: "tagTooltipRef",
                ref: tagTooltipRef,
                disabled: !_ctx.collapseTagsTooltip,
                "fallback-placements": ["bottom", "top", "right", "left"],
                effect: _ctx.tagEffect,
                placement: "bottom"
              }, {
                default: vue.withCtx(() => [
                  vue.createElementVNode(
                    "div",
                    {
                      ref_key: "collapseItemRef",
                      ref: collapseItemRef
                    },
                    [
                      vue.createVNode(vue.unref(index$2.ElTag), {
                        closable: false,
                        size: vue.unref(tagSize),
                        type: _ctx.tagType,
                        effect: _ctx.tagEffect,
                        "disable-transitions": ""
                      }, {
                        default: vue.withCtx(() => [
                          vue.createTextVNode(
                            " + " + vue.toDisplayString(_ctx.modelValue.length - _ctx.maxCollapseTags),
                            1
                          )
                        ]),
                        _: 1
                      }, 8, ["size", "type", "effect"])
                    ],
                    512
                  )
                ]),
                content: vue.withCtx(() => [
                  vue.createElementVNode(
                    "div",
                    {
                      class: vue.normalizeClass(vue.unref(ns).e("input-tag-list"))
                    },
                    [
                      (vue.openBlock(true), vue.createElementBlock(
                        vue.Fragment,
                        null,
                        vue.renderList(vue.unref(collapseTagList), (item, index) => {
                          return vue.openBlock(), vue.createBlock(vue.unref(index$2.ElTag), {
                            key: index,
                            size: vue.unref(tagSize),
                            closable: vue.unref(closable),
                            type: _ctx.tagType,
                            effect: _ctx.tagEffect,
                            "disable-transitions": "",
                            onClose: ($event) => vue.unref(handleRemoveTag)(index + _ctx.maxCollapseTags)
                          }, {
                            default: vue.withCtx(() => [
                              vue.renderSlot(_ctx.$slots, "tag", {
                                value: item,
                                index: index + _ctx.maxCollapseTags
                              }, () => [
                                vue.createTextVNode(
                                  vue.toDisplayString(item),
                                  1
                                )
                              ])
                            ]),
                            _: 2
                          }, 1032, ["size", "closable", "type", "effect", "onClose"]);
                        }),
                        128
                      ))
                    ],
                    2
                  )
                ]),
                _: 3
              }, 8, ["disabled", "effect"])) : vue.createCommentVNode("v-if", true),
              vue.createElementVNode(
                "div",
                {
                  class: vue.normalizeClass(vue.unref(ns).e("input-wrapper"))
                },
                [
                  vue.withDirectives(vue.createElementVNode("input", vue.mergeProps({
                    id: vue.unref(inputId),
                    ref_key: "inputRef",
                    ref: inputRef,
                    "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => vue.isRef(inputValue) ? inputValue.value = $event : null)
                  }, vue.unref(attrs), {
                    type: "text",
                    minlength: _ctx.minlength,
                    maxlength: _ctx.maxlength,
                    disabled: vue.unref(disabled),
                    readonly: _ctx.readonly,
                    autocomplete: _ctx.autocomplete,
                    tabindex: _ctx.tabindex,
                    placeholder: vue.unref(placeholder),
                    autofocus: _ctx.autofocus,
                    ariaLabel: _ctx.ariaLabel,
                    class: vue.unref(ns).e("input"),
                    style: vue.unref(inputStyle),
                    onCompositionstart: _cache[2] || (_cache[2] = (...args) => vue.unref(handleCompositionStart) && vue.unref(handleCompositionStart)(...args)),
                    onCompositionupdate: _cache[3] || (_cache[3] = (...args) => vue.unref(handleCompositionUpdate) && vue.unref(handleCompositionUpdate)(...args)),
                    onCompositionend: _cache[4] || (_cache[4] = (...args) => vue.unref(handleCompositionEnd) && vue.unref(handleCompositionEnd)(...args)),
                    onInput: _cache[5] || (_cache[5] = (...args) => vue.unref(handleInput) && vue.unref(handleInput)(...args)),
                    onKeydown: _cache[6] || (_cache[6] = (...args) => vue.unref(handleKeydown) && vue.unref(handleKeydown)(...args)),
                    onKeyup: _cache[7] || (_cache[7] = (...args) => vue.unref(handleKeyup) && vue.unref(handleKeyup)(...args))
                  }), null, 16, _hoisted_1), [
                    [vue.vModelText, vue.unref(inputValue)]
                  ]),
                  vue.createElementVNode("span", {
                    ref_key: "calculatorRef",
                    ref: calculatorRef,
                    "aria-hidden": "true",
                    class: vue.normalizeClass(vue.unref(ns).e("input-calculator")),
                    textContent: vue.toDisplayString(vue.unref(inputValue))
                  }, null, 10, _hoisted_2)
                ],
                2
              ),
              vue.withDirectives(vue.createElementVNode(
                "div",
                {
                  ref_key: "dropIndicatorRef",
                  ref: dropIndicatorRef,
                  class: vue.normalizeClass(vue.unref(ns).e("drop-indicator"))
                },
                null,
                2
              ), [
                [vue.vShow, vue.unref(showDropIndicator)]
              ])
            ],
            2
          ),
          vue.unref(showSuffix) ? (vue.openBlock(), vue.createElementBlock(
            "div",
            {
              key: 1,
              class: vue.normalizeClass(vue.unref(ns).e("suffix"))
            },
            [
              vue.renderSlot(_ctx.$slots, "suffix"),
              vue.unref(showClear) ? (vue.openBlock(), vue.createBlock(vue.unref(index$4.ElIcon), {
                key: 0,
                class: vue.normalizeClass([vue.unref(ns).e("icon"), vue.unref(ns).e("clear")]),
                onMousedown: vue.withModifiers(vue.unref(shared.NOOP), ["prevent"]),
                onClick: vue.unref(handleClear)
              }, {
                default: vue.withCtx(() => [
                  (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(_ctx.clearIcon)))
                ]),
                _: 1
              }, 8, ["class", "onMousedown", "onClick"])) : vue.createCommentVNode("v-if", true),
              validateState.value && validateIcon.value && needStatusIcon.value ? (vue.openBlock(), vue.createBlock(vue.unref(index$4.ElIcon), {
                key: 1,
                class: vue.normalizeClass([
                  vue.unref(nsInput).e("icon"),
                  vue.unref(nsInput).e("validateIcon"),
                  vue.unref(nsInput).is("loading", validateState.value === "validating")
                ])
              }, {
                default: vue.withCtx(() => [
                  (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(validateIcon.value)))
                ]),
                _: 1
              }, 8, ["class"])) : vue.createCommentVNode("v-if", true)
            ],
            2
          )) : vue.createCommentVNode("v-if", true)
        ],
        38
      );
    };
  }
});
var InputTag = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/input-tag/src/input-tag.vue"]]);

exports["default"] = InputTag;
//# sourceMappingURL=input-tag2.js.map
