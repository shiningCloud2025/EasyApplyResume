import { defineComponent, computed, openBlock, createElementBlock, normalizeClass, normalizeStyle, createElementVNode, unref, renderSlot, withModifiers, createVNode, withCtx, createCommentVNode, createBlock, Transition } from 'vue';
import { ElIcon } from '../../icon/index.mjs';
import { Close } from '@element-plus/icons-vue';
import { tagProps, tagEmits } from './tag.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useFormSize } from '../../form/src/hooks/use-form-common-props.mjs';
import { useLocale } from '../../../hooks/use-locale/index.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';

const _hoisted_1 = ["aria-label"];
const _hoisted_2 = ["aria-label"];
const _sfc_main = defineComponent({
  ...{
    name: "ElTag"
  },
  __name: "tag",
  props: tagProps,
  emits: tagEmits,
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const tagSize = useFormSize();
    const { t } = useLocale();
    const ns = useNamespace("tag");
    const containerKls = computed(() => {
      const { type, hit, effect, closable, round } = props;
      return [
        ns.b(),
        ns.is("closable", closable),
        ns.m(type || "primary"),
        ns.m(tagSize.value),
        ns.m(effect),
        ns.is("hit", hit),
        ns.is("round", round)
      ];
    });
    const handleClose = (event) => {
      emit("close", event);
    };
    const handleClick = (event) => {
      emit("click", event);
    };
    const handleVNodeMounted = (vnode) => {
      var _a, _b, _c;
      if ((_c = (_b = (_a = vnode == null ? void 0 : vnode.component) == null ? void 0 : _a.subTree) == null ? void 0 : _b.component) == null ? void 0 : _c.bum) {
        vnode.component.subTree.component.bum = null;
      }
    };
    return (_ctx, _cache) => {
      return _ctx.disableTransitions ? (openBlock(), createElementBlock(
        "span",
        {
          key: 0,
          class: normalizeClass(containerKls.value),
          style: normalizeStyle({ backgroundColor: _ctx.color }),
          onClick: handleClick
        },
        [
          createElementVNode(
            "span",
            {
              class: normalizeClass(unref(ns).e("content"))
            },
            [
              renderSlot(_ctx.$slots, "default")
            ],
            2
          ),
          _ctx.closable ? (openBlock(), createElementBlock("button", {
            key: 0,
            "aria-label": unref(t)("el.tag.close"),
            class: normalizeClass(unref(ns).e("close")),
            type: "button",
            onClick: withModifiers(handleClose, ["stop"])
          }, [
            createVNode(unref(ElIcon), null, {
              default: withCtx(() => [
                createVNode(unref(Close))
              ]),
              _: 1
            })
          ], 10, _hoisted_1)) : createCommentVNode("v-if", true)
        ],
        6
      )) : (openBlock(), createBlock(Transition, {
        key: 1,
        name: `${unref(ns).namespace.value}-zoom-in-center`,
        appear: "",
        onVnodeMounted: handleVNodeMounted
      }, {
        default: withCtx(() => [
          createElementVNode(
            "span",
            {
              class: normalizeClass(containerKls.value),
              style: normalizeStyle({ backgroundColor: _ctx.color }),
              onClick: handleClick
            },
            [
              createElementVNode(
                "span",
                {
                  class: normalizeClass(unref(ns).e("content"))
                },
                [
                  renderSlot(_ctx.$slots, "default")
                ],
                2
              ),
              _ctx.closable ? (openBlock(), createElementBlock("button", {
                key: 0,
                "aria-label": unref(t)("el.tag.close"),
                class: normalizeClass(unref(ns).e("close")),
                type: "button",
                onClick: withModifiers(handleClose, ["stop"])
              }, [
                createVNode(unref(ElIcon), null, {
                  default: withCtx(() => [
                    createVNode(unref(Close))
                  ]),
                  _: 1
                })
              ], 10, _hoisted_2)) : createCommentVNode("v-if", true)
            ],
            6
          )
        ]),
        _: 3
      }, 8, ["name"]));
    };
  }
});
var Tag = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/tag/src/tag.vue"]]);

export { Tag as default };
//# sourceMappingURL=tag2.mjs.map
