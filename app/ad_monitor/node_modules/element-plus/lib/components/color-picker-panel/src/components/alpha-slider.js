'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var slider = require('../props/slider.js');
var useSlider = require('../composables/use-slider.js');
var pluginVue_exportHelper = require('../../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../../hooks/use-locale/index.js');

const _hoisted_1 = ["aria-label", "aria-valuenow", "aria-valuetext", "aria-orientation", "tabindex", "aria-disabled"];
const minValue = 0;
const maxValue = 100;
const _sfc_main = vue.defineComponent({
  ...{
    name: "ElColorAlphaSlider"
  },
  __name: "alpha-slider",
  props: slider.alphaSliderProps,
  setup(__props, { expose: __expose }) {
    const props = __props;
    const { currentValue, bar, thumb, handleDrag, handleClick, handleKeydown } = useSlider.useSlider(props, { key: "alpha", minValue, maxValue });
    const { rootKls, barKls, barStyle, thumbKls, thumbStyle, update } = useSlider.useSliderDOM(props, {
      namespace: "color-alpha-slider",
      maxValue,
      currentValue,
      bar,
      thumb,
      handleDrag,
      getBackground
    });
    const { t } = index.useLocale();
    const ariaLabel = vue.computed(() => t("el.colorpicker.alphaLabel"));
    const ariaValuetext = vue.computed(() => {
      return t("el.colorpicker.alphaDescription", {
        alpha: currentValue.value,
        color: props.color.value
      });
    });
    function getBackground() {
      if (props.color && props.color.value) {
        const { r, g, b } = props.color.toRgb();
        return `linear-gradient(to right, rgba(${r}, ${g}, ${b}, 0) 0%, rgba(${r}, ${g}, ${b}, 1) 100%)`;
      }
      return "";
    }
    __expose({
      update,
      bar,
      thumb
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "div",
        {
          class: vue.normalizeClass(vue.unref(rootKls))
        },
        [
          vue.createElementVNode(
            "div",
            {
              ref_key: "bar",
              ref: bar,
              class: vue.normalizeClass(vue.unref(barKls)),
              style: vue.normalizeStyle(vue.unref(barStyle)),
              onClick: _cache[0] || (_cache[0] = (...args) => vue.unref(handleClick) && vue.unref(handleClick)(...args))
            },
            null,
            6
          ),
          vue.createElementVNode("div", {
            ref_key: "thumb",
            ref: thumb,
            class: vue.normalizeClass(vue.unref(thumbKls)),
            style: vue.normalizeStyle(vue.unref(thumbStyle)),
            "aria-label": ariaLabel.value,
            "aria-valuenow": vue.unref(currentValue),
            "aria-valuetext": ariaValuetext.value,
            "aria-orientation": _ctx.vertical ? "vertical" : "horizontal",
            "aria-valuemin": minValue,
            "aria-valuemax": maxValue,
            role: "slider",
            tabindex: _ctx.disabled ? void 0 : 0,
            "aria-disabled": _ctx.disabled,
            onKeydown: _cache[1] || (_cache[1] = (...args) => vue.unref(handleKeydown) && vue.unref(handleKeydown)(...args))
          }, null, 46, _hoisted_1)
        ],
        2
      );
    };
  }
});
var AlphaSlider = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/color-picker-panel/src/components/alpha-slider.vue"]]);

exports["default"] = AlphaSlider;
//# sourceMappingURL=alpha-slider.js.map
