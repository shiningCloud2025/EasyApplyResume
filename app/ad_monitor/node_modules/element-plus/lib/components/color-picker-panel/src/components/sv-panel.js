'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var index = require('../../../../hooks/use-locale/index.js');
var svPanel = require('../props/sv-panel.js');
var useSvPanel = require('../composables/use-sv-panel.js');
var pluginVue_exportHelper = require('../../../../_virtual/plugin-vue_export-helper.js');

const _hoisted_1 = ["tabindex", "aria-disabled", "aria-label", "aria-valuenow", "aria-valuetext"];
const _sfc_main = vue.defineComponent({
  ...{
    name: "ElSvPanel"
  },
  __name: "sv-panel",
  props: svPanel.svPanelProps,
  setup(__props, { expose: __expose }) {
    const props = __props;
    const {
      cursorRef,
      cursorTop,
      cursorLeft,
      background,
      saturation,
      brightness,
      handleClick,
      handleDrag,
      handleKeydown
    } = useSvPanel.useSvPanel(props);
    const { rootKls, cursorKls, rootStyle, cursorStyle, update } = useSvPanel.useSvPanelDOM(
      props,
      {
        cursorTop,
        cursorLeft,
        background,
        handleDrag
      }
    );
    const { t } = index.useLocale();
    const ariaLabel = vue.computed(() => t("el.colorpicker.svLabel"));
    const ariaValuetext = vue.computed(() => {
      return t("el.colorpicker.svDescription", {
        saturation: saturation.value,
        brightness: brightness.value,
        color: props.color.value
      });
    });
    __expose({
      update
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "div",
        {
          class: vue.normalizeClass(vue.unref(rootKls)),
          style: vue.normalizeStyle(vue.unref(rootStyle)),
          onClick: _cache[1] || (_cache[1] = (...args) => vue.unref(handleClick) && vue.unref(handleClick)(...args))
        },
        [
          vue.createElementVNode("div", {
            ref_key: "cursorRef",
            ref: cursorRef,
            class: vue.normalizeClass(vue.unref(cursorKls)),
            style: vue.normalizeStyle(vue.unref(cursorStyle)),
            tabindex: _ctx.disabled ? void 0 : 0,
            "aria-disabled": _ctx.disabled,
            role: "slider",
            "aria-valuemin": "0,0",
            "aria-valuemax": "100,100",
            "aria-label": ariaLabel.value,
            "aria-valuenow": `${vue.unref(saturation)},${vue.unref(brightness)}`,
            "aria-valuetext": ariaValuetext.value,
            onKeydown: _cache[0] || (_cache[0] = (...args) => vue.unref(handleKeydown) && vue.unref(handleKeydown)(...args))
          }, null, 46, _hoisted_1)
        ],
        6
      );
    };
  }
});
var SvPanel = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/color-picker-panel/src/components/sv-panel.vue"]]);

exports["default"] = SvPanel;
//# sourceMappingURL=sv-panel.js.map
