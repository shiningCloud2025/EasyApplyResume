import { defineComponent, openBlock, createElementBlock, normalizeClass, unref, createElementVNode, Fragment, createTextVNode, toDisplayString, renderSlot } from 'vue';
import { menuItemGroupProps } from './menu-item-group.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';

const _sfc_main = defineComponent({
  ...{
    name: "ElMenuItemGroup"
  },
  __name: "menu-item-group",
  props: menuItemGroupProps,
  setup(__props) {
    const ns = useNamespace("menu-item-group");
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "li",
        {
          class: normalizeClass(unref(ns).b())
        },
        [
          createElementVNode(
            "div",
            {
              class: normalizeClass(unref(ns).e("title"))
            },
            [
              !_ctx.$slots.title ? (openBlock(), createElementBlock(
                Fragment,
                { key: 0 },
                [
                  createTextVNode(
                    toDisplayString(_ctx.title),
                    1
                  )
                ],
                64
              )) : renderSlot(_ctx.$slots, "title", { key: 1 })
            ],
            2
          ),
          createElementVNode("ul", null, [
            renderSlot(_ctx.$slots, "default")
          ])
        ],
        2
      );
    };
  }
});
var MenuItemGroup = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/menu/src/menu-item-group.vue"]]);

export { MenuItemGroup as default };
//# sourceMappingURL=menu-item-group2.mjs.map
