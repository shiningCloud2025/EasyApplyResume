'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var badge = require('./badge2.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../hooks/use-namespace/index.js');
var types = require('../../../utils/types.js');
var style = require('../../../utils/dom/style.js');

const _sfc_main = vue.defineComponent({
  ...{
    name: "ElBadge"
  },
  __name: "badge",
  props: badge.badgeProps,
  setup(__props, { expose: __expose }) {
    const props = __props;
    const ns = index.useNamespace("badge");
    const content = vue.computed(() => {
      if (props.isDot)
        return "";
      if (types.isNumber(props.value) && types.isNumber(props.max)) {
        return props.max < props.value ? `${props.max}+` : `${props.value}`;
      }
      return `${props.value}`;
    });
    const style$1 = vue.computed(() => {
      var _a;
      return [
        {
          backgroundColor: props.color,
          marginRight: style.addUnit(-props.offset[0]),
          marginTop: style.addUnit(props.offset[1])
        },
        (_a = props.badgeStyle) != null ? _a : {}
      ];
    });
    __expose({
      content
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "div",
        {
          class: vue.normalizeClass(vue.unref(ns).b())
        },
        [
          vue.renderSlot(_ctx.$slots, "default"),
          vue.createVNode(vue.Transition, {
            name: `${vue.unref(ns).namespace.value}-zoom-in-center`,
            persisted: ""
          }, {
            default: vue.withCtx(() => [
              vue.withDirectives(vue.createElementVNode(
                "sup",
                {
                  class: vue.normalizeClass([
                    vue.unref(ns).e("content"),
                    vue.unref(ns).em("content", _ctx.type),
                    vue.unref(ns).is("fixed", !!_ctx.$slots.default),
                    vue.unref(ns).is("dot", _ctx.isDot),
                    vue.unref(ns).is("hide-zero", !_ctx.showZero && _ctx.value === 0),
                    _ctx.badgeClass
                  ]),
                  style: vue.normalizeStyle(style$1.value)
                },
                [
                  vue.renderSlot(_ctx.$slots, "content", { value: content.value }, () => [
                    vue.createTextVNode(
                      vue.toDisplayString(content.value),
                      1
                    )
                  ])
                ],
                6
              ), [
                [vue.vShow, !_ctx.hidden && (content.value || _ctx.isDot || _ctx.$slots.content)]
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
var Badge = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/badge/src/badge.vue"]]);

exports["default"] = Badge;
//# sourceMappingURL=badge.js.map
