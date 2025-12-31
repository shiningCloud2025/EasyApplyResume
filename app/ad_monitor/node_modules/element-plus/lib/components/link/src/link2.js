'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var index$2 = require('../../icon/index.js');
var link = require('./link.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var useGlobalConfig = require('../../config-provider/src/hooks/use-global-config.js');
var index = require('../../../hooks/use-deprecated/index.js');
var types = require('../../../utils/types.js');
var index$1 = require('../../../hooks/use-namespace/index.js');

const _hoisted_1 = ["href", "target"];
const _sfc_main = vue.defineComponent({
  ...{
    name: "ElLink"
  },
  __name: "link",
  props: link.linkProps,
  emits: link.linkEmits,
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const globalConfig = useGlobalConfig.useGlobalConfig("link");
    index.useDeprecated(
      {
        scope: "el-link",
        from: "The underline option (boolean)",
        replacement: "'always' | 'hover' | 'never'",
        version: "3.0.0",
        ref: "https://element-plus.org/en-US/component/link.html#underline"
      },
      vue.computed(() => types.isBoolean(props.underline))
    );
    const ns = index$1.useNamespace("link");
    const linkKls = vue.computed(() => {
      var _a, _b, _c;
      return [
        ns.b(),
        ns.m((_c = (_b = props.type) != null ? _b : (_a = globalConfig.value) == null ? void 0 : _a.type) != null ? _c : "default"),
        ns.is("disabled", props.disabled),
        ns.is("underline", underline.value === "always"),
        ns.is("hover-underline", underline.value === "hover" && !props.disabled)
      ];
    });
    const underline = vue.computed(() => {
      var _a, _b, _c;
      if (types.isBoolean(props.underline)) {
        return props.underline ? "hover" : "never";
      } else
        return (_c = (_b = props.underline) != null ? _b : (_a = globalConfig.value) == null ? void 0 : _a.underline) != null ? _c : "hover";
    });
    function handleClick(event) {
      if (!props.disabled)
        emit("click", event);
    }
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock("a", {
        class: vue.normalizeClass(linkKls.value),
        href: _ctx.disabled || !_ctx.href ? void 0 : _ctx.href,
        target: _ctx.disabled || !_ctx.href ? void 0 : _ctx.target,
        onClick: handleClick
      }, [
        _ctx.icon ? (vue.openBlock(), vue.createBlock(vue.unref(index$2.ElIcon), { key: 0 }, {
          default: vue.withCtx(() => [
            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(_ctx.icon)))
          ]),
          _: 1
        })) : vue.createCommentVNode("v-if", true),
        _ctx.$slots.default ? (vue.openBlock(), vue.createElementBlock(
          "span",
          {
            key: 1,
            class: vue.normalizeClass(vue.unref(ns).e("inner"))
          },
          [
            vue.renderSlot(_ctx.$slots, "default")
          ],
          2
        )) : vue.createCommentVNode("v-if", true),
        _ctx.$slots.icon ? vue.renderSlot(_ctx.$slots, "icon", { key: 2 }) : vue.createCommentVNode("v-if", true)
      ], 10, _hoisted_1);
    };
  }
});
var Link = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/link/src/link.vue"]]);

exports["default"] = Link;
//# sourceMappingURL=link2.js.map
