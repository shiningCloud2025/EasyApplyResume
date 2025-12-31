import { defineComponent, computed, openBlock, createElementBlock, normalizeClass, unref, renderSlot, createVNode, Transition, withCtx, withDirectives, createElementVNode, normalizeStyle, createTextVNode, toDisplayString, vShow } from 'vue';
import { badgeProps } from './badge2.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { isNumber } from '../../../utils/types.mjs';
import { addUnit } from '../../../utils/dom/style.mjs';

const _sfc_main = defineComponent({
  ...{
    name: "ElBadge"
  },
  __name: "badge",
  props: badgeProps,
  setup(__props, { expose: __expose }) {
    const props = __props;
    const ns = useNamespace("badge");
    const content = computed(() => {
      if (props.isDot)
        return "";
      if (isNumber(props.value) && isNumber(props.max)) {
        return props.max < props.value ? `${props.max}+` : `${props.value}`;
      }
      return `${props.value}`;
    });
    const style = computed(() => {
      var _a;
      return [
        {
          backgroundColor: props.color,
          marginRight: addUnit(-props.offset[0]),
          marginTop: addUnit(props.offset[1])
        },
        (_a = props.badgeStyle) != null ? _a : {}
      ];
    });
    __expose({
      content
    });
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "div",
        {
          class: normalizeClass(unref(ns).b())
        },
        [
          renderSlot(_ctx.$slots, "default"),
          createVNode(Transition, {
            name: `${unref(ns).namespace.value}-zoom-in-center`,
            persisted: ""
          }, {
            default: withCtx(() => [
              withDirectives(createElementVNode(
                "sup",
                {
                  class: normalizeClass([
                    unref(ns).e("content"),
                    unref(ns).em("content", _ctx.type),
                    unref(ns).is("fixed", !!_ctx.$slots.default),
                    unref(ns).is("dot", _ctx.isDot),
                    unref(ns).is("hide-zero", !_ctx.showZero && _ctx.value === 0),
                    _ctx.badgeClass
                  ]),
                  style: normalizeStyle(style.value)
                },
                [
                  renderSlot(_ctx.$slots, "content", { value: content.value }, () => [
                    createTextVNode(
                      toDisplayString(content.value),
                      1
                    )
                  ])
                ],
                6
              ), [
                [vShow, !_ctx.hidden && (content.value || _ctx.isDot || _ctx.$slots.content)]
              ])
            ]),
            _: 3
          }, 8, ["name"])
        ],
        2
      );
    };
  }
});
var Badge = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/badge/src/badge.vue"]]);

export { Badge as default };
//# sourceMappingURL=badge.mjs.map
