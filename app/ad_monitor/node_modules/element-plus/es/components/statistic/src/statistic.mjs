import { defineComponent, computed, openBlock, createElementBlock, normalizeClass, unref, renderSlot, createTextVNode, toDisplayString, createCommentVNode, createElementVNode, normalizeStyle } from 'vue';
import { statisticProps } from './statistic2.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { isFunction } from '@vue/shared';
import { isNumber } from '../../../utils/types.mjs';

const _sfc_main = defineComponent({
  ...{
    name: "ElStatistic"
  },
  __name: "statistic",
  props: statisticProps,
  setup(__props, { expose: __expose }) {
    const props = __props;
    const ns = useNamespace("statistic");
    const displayValue = computed(() => {
      const { value, formatter, precision, decimalSeparator, groupSeparator } = props;
      if (isFunction(formatter))
        return formatter(value);
      if (!isNumber(value) || Number.isNaN(value))
        return value;
      let [integer, decimal = ""] = String(value).split(".");
      decimal = decimal.padEnd(precision, "0").slice(0, precision > 0 ? precision : 0);
      integer = integer.replace(/\B(?=(\d{3})+(?!\d))/g, groupSeparator);
      return [integer, decimal].join(decimal ? decimalSeparator : "");
    });
    __expose({
      displayValue
    });
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "div",
        {
          class: normalizeClass(unref(ns).b())
        },
        [
          _ctx.$slots.title || _ctx.title ? (openBlock(), createElementBlock(
            "div",
            {
              key: 0,
              class: normalizeClass(unref(ns).e("head"))
            },
            [
              renderSlot(_ctx.$slots, "title", {}, () => [
                createTextVNode(
                  toDisplayString(_ctx.title),
                  1
                )
              ])
            ],
            2
          )) : createCommentVNode("v-if", true),
          createElementVNode(
            "div",
            {
              class: normalizeClass(unref(ns).e("content"))
            },
            [
              _ctx.$slots.prefix || _ctx.prefix ? (openBlock(), createElementBlock(
                "div",
                {
                  key: 0,
                  class: normalizeClass(unref(ns).e("prefix"))
                },
                [
                  renderSlot(_ctx.$slots, "prefix", {}, () => [
                    createElementVNode(
                      "span",
                      null,
                      toDisplayString(_ctx.prefix),
                      1
                    )
                  ])
                ],
                2
              )) : createCommentVNode("v-if", true),
              createElementVNode(
                "span",
                {
                  class: normalizeClass(unref(ns).e("number")),
                  style: normalizeStyle(_ctx.valueStyle)
                },
                toDisplayString(displayValue.value),
                7
              ),
              _ctx.$slots.suffix || _ctx.suffix ? (openBlock(), createElementBlock(
                "div",
                {
                  key: 1,
                  class: normalizeClass(unref(ns).e("suffix"))
                },
                [
                  renderSlot(_ctx.$slots, "suffix", {}, () => [
                    createElementVNode(
                      "span",
                      null,
                      toDisplayString(_ctx.suffix),
                      1
                    )
                  ])
                ],
                2
              )) : createCommentVNode("v-if", true)
            ],
            2
          )
        ],
        2
      );
    };
  }
});
var Statistic = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/statistic/src/statistic.vue"]]);

export { Statistic as default };
//# sourceMappingURL=statistic.mjs.map
