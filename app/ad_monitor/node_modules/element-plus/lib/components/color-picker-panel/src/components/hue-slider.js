'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var slider = require('../props/slider.js');
var useSlider = require('../composables/use-slider.js');
var pluginVue_exportHelper = require('../../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../../hooks/use-locale/index.js');

const _hoisted_1 = ["aria-label", "aria-valuenow", "aria-valuetext", "aria-orientation", "tabindex", "aria-disabled"];
const minValue = 0;
const maxValue = 360;
const _sfc_main = vue.defineComponent({
  ...{
    name: "ElColorHueSlider"
  },
  __name: "hue-slider",
  props: slider.hueSliderProps,
  setup(__props, { expose: __expose }) {
    const props = __props;
    const { currentValue, bar, thumb, handleDrag, handleClick, handleKeydown } = useSlider.useSlider(props, { key: "hue", minValue, maxValue });
    const { rootKls, barKls, thumbKls, thumbStyle, thumbTop, update } = useSlider.useSliderDOM(props, {
      namespace: "color-hue-slider",
      maxValue,
      currentValue,
      bar,
      thumb,
      handleDrag
    });
    const { t } = index.useLocale();
    const ariaLabel = vue.computed(() => t("el.colorpicker.hueLabel"));
    const ariaValuetext = vue.computed(() => {
      return t("el.colorpicker.hueDescription", {
        hue: currentValue.value,
        color: props.color.value
      });
    });
    __expose({
      bar,
      thumb,
      thumbTop,
      update
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
              onClick: _cache[0] || (_cache[0] = (...args) => vue.unref(handleClick) && vue.unref(handleClick)(...args))
            },
            null,
            2
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
var HueSlider = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/color-picker-panel/src/components/hue-slider.vue"]]);

exports["default"] = HueSlider;
//# sourceMappingURL=hue-slider.js.map
