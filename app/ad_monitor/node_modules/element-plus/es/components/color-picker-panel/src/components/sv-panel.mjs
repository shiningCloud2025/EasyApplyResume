import { defineComponent, computed, openBlock, createElementBlock, normalizeClass, unref, normalizeStyle, createElementVNode } from 'vue';
import { useLocale } from '../../../../hooks/use-locale/index.mjs';
import { svPanelProps } from '../props/sv-panel.mjs';
import { useSvPanel, useSvPanelDOM } from '../composables/use-sv-panel.mjs';
import _export_sfc from '../../../../_virtual/plugin-vue_export-helper.mjs';

const _hoisted_1 = ["tabindex", "aria-disabled", "aria-label", "aria-valuenow", "aria-valuetext"];
const _sfc_main = defineComponent({
  ...{
    name: "ElSvPanel"
  },
  __name: "sv-panel",
  props: svPanelProps,
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
    } = useSvPanel(props);
    const { rootKls, cursorKls, rootStyle, cursorStyle, update } = useSvPanelDOM(
      props,
      {
        cursorTop,
        cursorLeft,
        background,
        handleDrag
      }
    );
    const { t } = useLocale();
    const ariaLabel = computed(() => t("el.colorpicker.svLabel"));
    const ariaValuetext = computed(() => {
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
      return openBlock(), createElementBlock(
        "div",
        {
          class: normalizeClass(unref(rootKls)),
          style: normalizeStyle(unref(rootStyle)),
          onClick: _cache[1] || (_cache[1] = (...args) => unref(handleClick) && unref(handleClick)(...args))
        },
        [
          createElementVNode("div", {
            ref_key: "cursorRef",
            ref: cursorRef,
            class: normalizeClass(unref(cursorKls)),
            style: normalizeStyle(unref(cursorStyle)),
            tabindex: _ctx.disabled ? void 0 : 0,
            "aria-disabled": _ctx.disabled,
            role: "slider",
            "aria-valuemin": "0,0",
            "aria-valuemax": "100,100",
            "aria-label": ariaLabel.value,
            "aria-valuenow": `${unref(saturation)},${unref(brightness)}`,
            "aria-valuetext": ariaValuetext.value,
            onKeydown: _cache[0] || (_cache[0] = (...args) => unref(handleKeydown) && unref(handleKeydown)(...args))
          }, null, 46, _hoisted_1)
        ],
        6
      );
    };
  }
});
var SvPanel = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/color-picker-panel/src/components/sv-panel.vue"]]);

export { SvPanel as default };
//# sourceMappingURL=sv-panel.mjs.map
