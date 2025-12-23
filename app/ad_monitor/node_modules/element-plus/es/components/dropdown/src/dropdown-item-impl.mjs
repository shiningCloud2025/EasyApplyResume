import { defineComponent, inject, computed, resolveComponent, openBlock, createElementBlock, Fragment, normalizeClass, createCommentVNode, createElementVNode, mergeProps, withModifiers, createBlock, withCtx, resolveDynamicComponent, renderSlot } from 'vue';
import { ElIcon } from '../../icon/index.mjs';
import { dropdownItemProps } from './dropdown.mjs';
import { DROPDOWN_INJECTION_KEY } from './tokens.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { ROVING_FOCUS_ITEM_COLLECTION_INJECTION_KEY as COLLECTION_ITEM_INJECTION_KEY } from '../../roving-focus-group/src/roving-focus-group.mjs';
import { ROVING_FOCUS_GROUP_ITEM_INJECTION_KEY } from '../../roving-focus-group/src/tokens.mjs';
import { COLLECTION_ITEM_SIGN } from '../../collection/src/collection.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { composeRefs } from '../../../utils/vue/refs.mjs';
import { composeEventHandlers, getEventCode } from '../../../utils/dom/event.mjs';
import { EVENT_CODE } from '../../../constants/aria.mjs';

const _sfc_main = defineComponent({
  name: "DropdownItemImpl",
  components: {
    ElIcon
  },
  props: dropdownItemProps,
  emits: ["pointermove", "pointerleave", "click", "clickimpl"],
  setup(_, { emit }) {
    const ns = useNamespace("dropdown");
    const { role: menuRole } = inject(DROPDOWN_INJECTION_KEY, void 0);
    const { collectionItemRef: rovingFocusCollectionItemRef } = inject(
      COLLECTION_ITEM_INJECTION_KEY,
      void 0
    );
    const {
      rovingFocusGroupItemRef,
      tabIndex,
      handleFocus,
      handleKeydown: handleItemKeydown,
      handleMousedown
    } = inject(ROVING_FOCUS_GROUP_ITEM_INJECTION_KEY, void 0);
    const itemRef = composeRefs(
      rovingFocusCollectionItemRef,
      rovingFocusGroupItemRef
    );
    const role = computed(() => {
      if (menuRole.value === "menu") {
        return "menuitem";
      } else if (menuRole.value === "navigation") {
        return "link";
      }
      return "button";
    });
    const handleKeydown = composeEventHandlers((e) => {
      const code = getEventCode(e);
      if ([EVENT_CODE.enter, EVENT_CODE.numpadEnter, EVENT_CODE.space].includes(
        code
      )) {
        e.preventDefault();
        e.stopImmediatePropagation();
        emit("clickimpl", e);
        return true;
      }
    }, handleItemKeydown);
    return {
      ns,
      itemRef,
      dataset: {
        [COLLECTION_ITEM_SIGN]: ""
      },
      role,
      tabIndex,
      handleFocus,
      handleKeydown,
      handleMousedown
    };
  }
});
const _hoisted_1 = ["aria-disabled", "tabindex", "role"];
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_el_icon = resolveComponent("el-icon");
  return openBlock(), createElementBlock(
    Fragment,
    null,
    [
      _ctx.divided ? (openBlock(), createElementBlock(
        "li",
        {
          key: 0,
          role: "separator",
          class: normalizeClass(_ctx.ns.bem("menu", "item", "divided"))
        },
        null,
        2
      )) : createCommentVNode("v-if", true),
      createElementVNode("li", mergeProps({ ref: _ctx.itemRef }, { ..._ctx.dataset, ..._ctx.$attrs }, {
        "aria-disabled": _ctx.disabled,
        class: [_ctx.ns.be("menu", "item"), _ctx.ns.is("disabled", _ctx.disabled)],
        tabindex: _ctx.tabIndex,
        role: _ctx.role,
        onClick: _cache[0] || (_cache[0] = (e) => _ctx.$emit("clickimpl", e)),
        onFocus: _cache[1] || (_cache[1] = (...args) => _ctx.handleFocus && _ctx.handleFocus(...args)),
        onKeydown: _cache[2] || (_cache[2] = withModifiers((...args) => _ctx.handleKeydown && _ctx.handleKeydown(...args), ["self"])),
        onMousedown: _cache[3] || (_cache[3] = (...args) => _ctx.handleMousedown && _ctx.handleMousedown(...args)),
        onPointermove: _cache[4] || (_cache[4] = (e) => _ctx.$emit("pointermove", e)),
        onPointerleave: _cache[5] || (_cache[5] = (e) => _ctx.$emit("pointerleave", e))
      }), [
        _ctx.icon ? (openBlock(), createBlock(_component_el_icon, { key: 0 }, {
          default: withCtx(() => [
            (openBlock(), createBlock(resolveDynamicComponent(_ctx.icon)))
          ]),
          _: 1
        })) : createCommentVNode("v-if", true),
        renderSlot(_ctx.$slots, "default")
      ], 16, _hoisted_1)
    ],
    64
  );
}
var ElDropdownItemImpl = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "/home/runner/work/element-plus/element-plus/packages/components/dropdown/src/dropdown-item-impl.vue"]]);

export { ElDropdownItemImpl as default };
//# sourceMappingURL=dropdown-item-impl.mjs.map
