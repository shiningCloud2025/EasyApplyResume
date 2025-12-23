import { defineComponent, computed, unref, reactive, toRefs, getCurrentInstance, onBeforeUnmount, nextTick, withDirectives, openBlock, createElementBlock, normalizeClass, withModifiers, renderSlot, createElementVNode, toDisplayString, vShow } from 'vue';
import { useOption } from './useOption.mjs';
import { COMPONENT_NAME, optionProps } from './option.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { useId } from '../../../hooks/use-id/index.mjs';

const _sfc_main = defineComponent({
  name: COMPONENT_NAME,
  componentName: COMPONENT_NAME,
  props: optionProps,
  setup(props) {
    const ns = useNamespace("select");
    const id = useId();
    const containerKls = computed(() => [
      ns.be("dropdown", "item"),
      ns.is("disabled", unref(isDisabled)),
      ns.is("selected", unref(itemSelected)),
      ns.is("hovering", unref(hover))
    ]);
    const states = reactive({
      index: -1,
      groupDisabled: false,
      visible: true,
      hover: false
    });
    const {
      currentLabel,
      itemSelected,
      isDisabled,
      select,
      hoverItem,
      updateOption
    } = useOption(props, states);
    const { visible, hover } = toRefs(states);
    const vm = getCurrentInstance().proxy;
    select.onOptionCreate(vm);
    onBeforeUnmount(() => {
      const key = vm.value;
      nextTick(() => {
        const { selected: selectedOptions } = select.states;
        const doesSelected = selectedOptions.some((item) => {
          return item.value === vm.value;
        });
        if (select.states.cachedOptions.get(key) === vm && !doesSelected) {
          select.states.cachedOptions.delete(key);
        }
      });
      select.onOptionDestroy(key, vm);
    });
    function selectOptionClick() {
      if (!isDisabled.value) {
        select.handleOptionSelect(vm);
      }
    }
    return {
      ns,
      id,
      containerKls,
      currentLabel,
      itemSelected,
      isDisabled,
      select,
      visible,
      hover,
      states,
      hoverItem,
      updateOption,
      selectOptionClick
    };
  }
});
const _hoisted_1 = ["id", "aria-disabled", "aria-selected"];
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return withDirectives((openBlock(), createElementBlock("li", {
    id: _ctx.id,
    class: normalizeClass(_ctx.containerKls),
    role: "option",
    "aria-disabled": _ctx.isDisabled || void 0,
    "aria-selected": _ctx.itemSelected,
    onMousemove: _cache[0] || (_cache[0] = (...args) => _ctx.hoverItem && _ctx.hoverItem(...args)),
    onClick: _cache[1] || (_cache[1] = withModifiers((...args) => _ctx.selectOptionClick && _ctx.selectOptionClick(...args), ["stop"])),
    onMousedown: _cache[2] || (_cache[2] = withModifiers(() => {
    }, ["prevent"]))
  }, [
    renderSlot(_ctx.$slots, "default", {}, () => [
      createElementVNode(
        "span",
        null,
        toDisplayString(_ctx.currentLabel),
        1
      )
    ])
  ], 42, _hoisted_1)), [
    [vShow, _ctx.visible]
  ]);
}
var Option = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "/home/runner/work/element-plus/element-plus/packages/components/select/src/option.vue"]]);

export { Option as default };
//# sourceMappingURL=option2.mjs.map
