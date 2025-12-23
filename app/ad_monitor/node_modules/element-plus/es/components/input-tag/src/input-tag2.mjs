import { defineComponent, useSlots, computed, openBlock, createElementBlock, normalizeClass, unref, normalizeStyle, renderSlot, createCommentVNode, createElementVNode, Fragment, renderList, createBlock, withModifiers, withCtx, createTextVNode, toDisplayString, createVNode, withDirectives, mergeProps, isRef, vModelText, vShow, resolveDynamicComponent } from 'vue';
import { ElTooltip } from '../../tooltip/index.mjs';
import { ElIcon } from '../../icon/index.mjs';
import { ElTag } from '../../tag/index.mjs';
import { inputTagProps, inputTagEmits } from './input-tag.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useInputTag } from './composables/use-input-tag.mjs';
import { useHovering } from './composables/use-hovering.mjs';
import { useCalcInputWidth } from '../../../hooks/use-calc-input-width/index.mjs';
import { useDragTag } from './composables/use-drag-tag.mjs';
import { useInputTagDom } from './composables/use-input-tag-dom.mjs';
import { useAttrs } from '../../../hooks/use-attrs/index.mjs';
import { useFormItem, useFormItemInputId } from '../../form/src/hooks/use-form-item.mjs';
import { ValidateComponentsMap } from '../../../utils/vue/icon.mjs';
import { NOOP } from '@vue/shared';

const _hoisted_1 = ["id", "minlength", "maxlength", "disabled", "readonly", "autocomplete", "tabindex", "placeholder", "autofocus", "ariaLabel"];
const _hoisted_2 = ["textContent"];
const _sfc_main = defineComponent({
  ...{
    name: "ElInputTag",
    inheritAttrs: false
  },
  __name: "input-tag",
  props: inputTagProps,
  emits: inputTagEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const attrs = useAttrs();
    const slots = useSlots();
    const { form, formItem } = useFormItem();
    const { inputId } = useFormItemInputId(props, { formItemContext: formItem });
    const needStatusIcon = computed(() => {
      var _a;
      return (_a = form == null ? void 0 : form.statusIcon) != null ? _a : false;
    });
    const validateState = computed(() => (formItem == null ? void 0 : formItem.validateState) || "");
    const validateIcon = computed(() => {
      return validateState.value && ValidateComponentsMap[validateState.value];
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
    } = useInputTag({ props, emit, formItem });
    const { hovering, handleMouseEnter, handleMouseLeave } = useHovering();
    const { calculatorRef, inputStyle } = useCalcInputWidth();
    const {
      dropIndicatorRef,
      showDropIndicator,
      handleDragStart,
      handleDragOver,
      handleDragEnd
    } = useDragTag({ wrapperRef, handleDragged, afterDragged: focus });
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
    } = useInputTagDom({
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
      return openBlock(), createElementBlock(
        "div",
        {
          ref_key: "wrapperRef",
          ref: wrapperRef,
          class: normalizeClass(unref(containerKls)),
          style: normalizeStyle(unref(containerStyle)),
          onMouseenter: _cache[8] || (_cache[8] = (...args) => unref(handleMouseEnter) && unref(handleMouseEnter)(...args)),
          onMouseleave: _cache[9] || (_cache[9] = (...args) => unref(handleMouseLeave) && unref(handleMouseLeave)(...args))
        },
        [
          unref(slots).prefix ? (openBlock(), createElementBlock(
            "div",
            {
              key: 0,
              class: normalizeClass(unref(ns).e("prefix"))
            },
            [
              renderSlot(_ctx.$slots, "prefix")
            ],
            2
          )) : createCommentVNode("v-if", true),
          createElementVNode(
            "div",
            {
              ref_key: "innerRef",
              ref: innerRef,
              class: normalizeClass(unref(innerKls))
            },
            [
              (openBlock(true), createElementBlock(
                Fragment,
                null,
                renderList(unref(showTagList), (item, index) => {
                  return openBlock(), createBlock(unref(ElTag), {
                    key: index,
                    size: unref(tagSize),
                    closable: unref(closable),
                    type: _ctx.tagType,
                    effect: _ctx.tagEffect,
                    draggable: unref(closable) && _ctx.draggable,
                    style: normalizeStyle(unref(tagStyle)),
                    "disable-transitions": "",
                    onClose: ($event) => unref(handleRemoveTag)(index),
                    onDragstart: (event) => unref(handleDragStart)(event, index),
                    onDragover: (event) => unref(handleDragOver)(event, index),
                    onDragend: unref(handleDragEnd),
                    onDrop: _cache[0] || (_cache[0] = withModifiers(() => {
                    }, ["stop"]))
                  }, {
                    default: withCtx(() => [
                      renderSlot(_ctx.$slots, "tag", {
                        value: item,
                        index
                      }, () => [
                        createTextVNode(
                          toDisplayString(item),
                          1
                        )
                      ])
                    ]),
                    _: 2
                  }, 1032, ["size", "closable", "type", "effect", "draggable", "style", "onClose", "onDragstart", "onDragover", "onDragend"]);
                }),
                128
              )),
              _ctx.collapseTags && _ctx.modelValue && _ctx.modelValue.length > _ctx.maxCollapseTags ? (openBlock(), createBlock(unref(ElTooltip), {
                key: 0,
                ref_key: "tagTooltipRef",
                ref: tagTooltipRef,
                disabled: !_ctx.collapseTagsTooltip,
                "fallback-placements": ["bottom", "top", "right", "left"],
                effect: _ctx.tagEffect,
                placement: "bottom"
              }, {
                default: withCtx(() => [
                  createElementVNode(
                    "div",
                    {
                      ref_key: "collapseItemRef",
                      ref: collapseItemRef
                    },
                    [
                      createVNode(unref(ElTag), {
                        closable: false,
                        size: unref(tagSize),
                        type: _ctx.tagType,
                        effect: _ctx.tagEffect,
                        "disable-transitions": ""
                      }, {
                        default: withCtx(() => [
                          createTextVNode(
                            " + " + toDisplayString(_ctx.modelValue.length - _ctx.maxCollapseTags),
                            1
                          )
                        ]),
                        _: 1
                      }, 8, ["size", "type", "effect"])
                    ],
                    512
                  )
                ]),
                content: withCtx(() => [
                  createElementVNode(
                    "div",
                    {
                      class: normalizeClass(unref(ns).e("input-tag-list"))
                    },
                    [
                      (openBlock(true), createElementBlock(
                        Fragment,
                        null,
                        renderList(unref(collapseTagList), (item, index) => {
                          return openBlock(), createBlock(unref(ElTag), {
                            key: index,
                            size: unref(tagSize),
                            closable: unref(closable),
                            type: _ctx.tagType,
                            effect: _ctx.tagEffect,
                            "disable-transitions": "",
                            onClose: ($event) => unref(handleRemoveTag)(index + _ctx.maxCollapseTags)
                          }, {
                            default: withCtx(() => [
                              renderSlot(_ctx.$slots, "tag", {
                                value: item,
                                index: index + _ctx.maxCollapseTags
                              }, () => [
                                createTextVNode(
                                  toDisplayString(item),
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
              }, 8, ["disabled", "effect"])) : createCommentVNode("v-if", true),
              createElementVNode(
                "div",
                {
                  class: normalizeClass(unref(ns).e("input-wrapper"))
                },
                [
                  withDirectives(createElementVNode("input", mergeProps({
                    id: unref(inputId),
                    ref_key: "inputRef",
                    ref: inputRef,
                    "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => isRef(inputValue) ? inputValue.value = $event : null)
                  }, unref(attrs), {
                    type: "text",
                    minlength: _ctx.minlength,
                    maxlength: _ctx.maxlength,
                    disabled: unref(disabled),
                    readonly: _ctx.readonly,
                    autocomplete: _ctx.autocomplete,
                    tabindex: _ctx.tabindex,
                    placeholder: unref(placeholder),
                    autofocus: _ctx.autofocus,
                    ariaLabel: _ctx.ariaLabel,
                    class: unref(ns).e("input"),
                    style: unref(inputStyle),
                    onCompositionstart: _cache[2] || (_cache[2] = (...args) => unref(handleCompositionStart) && unref(handleCompositionStart)(...args)),
                    onCompositionupdate: _cache[3] || (_cache[3] = (...args) => unref(handleCompositionUpdate) && unref(handleCompositionUpdate)(...args)),
                    onCompositionend: _cache[4] || (_cache[4] = (...args) => unref(handleCompositionEnd) && unref(handleCompositionEnd)(...args)),
                    onInput: _cache[5] || (_cache[5] = (...args) => unref(handleInput) && unref(handleInput)(...args)),
                    onKeydown: _cache[6] || (_cache[6] = (...args) => unref(handleKeydown) && unref(handleKeydown)(...args)),
                    onKeyup: _cache[7] || (_cache[7] = (...args) => unref(handleKeyup) && unref(handleKeyup)(...args))
                  }), null, 16, _hoisted_1), [
                    [vModelText, unref(inputValue)]
                  ]),
                  createElementVNode("span", {
                    ref_key: "calculatorRef",
                    ref: calculatorRef,
                    "aria-hidden": "true",
                    class: normalizeClass(unref(ns).e("input-calculator")),
                    textContent: toDisplayString(unref(inputValue))
                  }, null, 10, _hoisted_2)
                ],
                2
              ),
              withDirectives(createElementVNode(
                "div",
                {
                  ref_key: "dropIndicatorRef",
                  ref: dropIndicatorRef,
                  class: normalizeClass(unref(ns).e("drop-indicator"))
                },
                null,
                2
              ), [
                [vShow, unref(showDropIndicator)]
              ])
            ],
            2
          ),
          unref(showSuffix) ? (openBlock(), createElementBlock(
            "div",
            {
              key: 1,
              class: normalizeClass(unref(ns).e("suffix"))
            },
            [
              renderSlot(_ctx.$slots, "suffix"),
              unref(showClear) ? (openBlock(), createBlock(unref(ElIcon), {
                key: 0,
                class: normalizeClass([unref(ns).e("icon"), unref(ns).e("clear")]),
                onMousedown: withModifiers(unref(NOOP), ["prevent"]),
                onClick: unref(handleClear)
              }, {
                default: withCtx(() => [
                  (openBlock(), createBlock(resolveDynamicComponent(_ctx.clearIcon)))
                ]),
                _: 1
              }, 8, ["class", "onMousedown", "onClick"])) : createCommentVNode("v-if", true),
              validateState.value && validateIcon.value && needStatusIcon.value ? (openBlock(), createBlock(unref(ElIcon), {
                key: 1,
                class: normalizeClass([
                  unref(nsInput).e("icon"),
                  unref(nsInput).e("validateIcon"),
                  unref(nsInput).is("loading", validateState.value === "validating")
                ])
              }, {
                default: withCtx(() => [
                  (openBlock(), createBlock(resolveDynamicComponent(validateIcon.value)))
                ]),
                _: 1
              }, 8, ["class"])) : createCommentVNode("v-if", true)
            ],
            2
          )) : createCommentVNode("v-if", true)
        ],
        38
      );
    };
  }
});
var InputTag = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/input-tag/src/input-tag.vue"]]);

export { InputTag as default };
//# sourceMappingURL=input-tag2.mjs.map
