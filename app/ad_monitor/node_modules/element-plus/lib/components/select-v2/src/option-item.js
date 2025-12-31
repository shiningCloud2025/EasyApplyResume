'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var useOption = require('./useOption.js');
var useProps = require('./useProps.js');
var defaults = require('./defaults.js');
var token = require('./token.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../hooks/use-namespace/index.js');

const _sfc_main = vue.defineComponent({
  props: defaults.optionV2Props,
  emits: defaults.optionV2Emits,
  setup(props, { emit }) {
    const select = vue.inject(token.selectV2InjectionKey);
    const ns = index.useNamespace("select");
    const { hoverItem, selectOptionClick } = useOption.useOption(props, { emit });
    const { getLabel } = useProps.useProps(select.props);
    const contentId = select.contentId;
    return {
      ns,
      contentId,
      hoverItem,
      selectOptionClick,
      getLabel
    };
  }
});
const _hoisted_1 = ["id", "aria-selected", "aria-disabled"];
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return vue.openBlock(), vue.createElementBlock("li", {
    id: `${_ctx.contentId}-${_ctx.index}`,
    role: "option",
    "aria-selected": _ctx.selected,
    "aria-disabled": _ctx.disabled || void 0,
    style: vue.normalizeStyle(_ctx.style),
    class: vue.normalizeClass([
      _ctx.ns.be("dropdown", "item"),
      _ctx.ns.is("selected", _ctx.selected),
      _ctx.ns.is("disabled", _ctx.disabled),
      _ctx.ns.is("created", _ctx.created),
      _ctx.ns.is("hovering", _ctx.hovering)
    ]),
    onMousemove: _cache[0] || (_cache[0] = (...args) => _ctx.hoverItem && _ctx.hoverItem(...args)),
    onClick: _cache[1] || (_cache[1] = vue.withModifiers((...args) => _ctx.selectOptionClick && _ctx.selectOptionClick(...args), ["stop"])),
    onMousedown: _cache[2] || (_cache[2] = vue.withModifiers(() => {
    }, ["prevent"]))
  }, [
    vue.renderSlot(_ctx.$slots, "default", {
      item: _ctx.item,
      index: _ctx.index,
      disabled: _ctx.disabled
    }, () => [
      vue.createElementVNode(
        "span",
        null,
        vue.toDisplayString(_ctx.getLabel(_ctx.item)),
        1
      )
    ])
  ], 46, _hoisted_1);
}
var OptionItem = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["render", _sfc_render], ["__file", "/home/runner/work/element-plus/element-plus/packages/components/select-v2/src/option-item.vue"]]);

exports["default"] = OptionItem;
//# sourceMappingURL=option-item.js.map
