'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var index$1 = require('../../icon/index.js');
var iconsVue = require('@element-plus/icons-vue');
var index$2 = require('../../checkbox/index.js');
var treeNodeContent = require('./tree-node-content.js');
var virtualTree = require('./virtual-tree.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../hooks/use-namespace/index.js');
var shared = require('@vue/shared');

const _hoisted_1 = ["aria-expanded", "aria-disabled", "aria-checked", "data-key"];
const _sfc_main = vue.defineComponent({
  ...{
    name: "ElTreeNode"
  },
  __name: "tree-node",
  props: virtualTree.treeNodeProps,
  emits: virtualTree.treeNodeEmits,
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const tree = vue.inject(virtualTree.ROOT_TREE_INJECTION_KEY);
    const ns = index.useNamespace("tree");
    const indent = vue.computed(() => {
      var _a;
      return (_a = tree == null ? void 0 : tree.props.indent) != null ? _a : 16;
    });
    const icon = vue.computed(() => {
      var _a;
      return (_a = tree == null ? void 0 : tree.props.icon) != null ? _a : iconsVue.CaretRight;
    });
    const getNodeClass = (node) => {
      const nodeClassFunc = tree == null ? void 0 : tree.props.props.class;
      if (!nodeClassFunc)
        return {};
      let className;
      if (shared.isFunction(nodeClassFunc)) {
        const { data } = node;
        className = nodeClassFunc(data, node);
      } else {
        className = nodeClassFunc;
      }
      return shared.isString(className) ? { [className]: true } : className;
    };
    const handleClick = (e) => {
      emit("click", props.node, e);
    };
    const handleDrop = (e) => {
      emit("drop", props.node, e);
    };
    const handleExpandIconClick = () => {
      emit("toggle", props.node);
    };
    const handleCheckChange = (value) => {
      emit("check", props.node, value);
    };
    const handleContextMenu = (event) => {
      var _a, _b, _c, _d;
      if ((_c = (_b = (_a = tree == null ? void 0 : tree.instance) == null ? void 0 : _a.vnode) == null ? void 0 : _b.props) == null ? void 0 : _c["onNodeContextmenu"]) {
        event.stopPropagation();
        event.preventDefault();
      }
      tree == null ? void 0 : tree.ctx.emit(virtualTree.NODE_CONTEXTMENU, event, (_d = props.node) == null ? void 0 : _d.data, props.node);
    };
    return (_ctx, _cache) => {
      var _a, _b, _c;
      return vue.openBlock(), vue.createElementBlock("div", {
        ref: "node$",
        class: vue.normalizeClass([
          vue.unref(ns).b("node"),
          vue.unref(ns).is("expanded", _ctx.expanded),
          vue.unref(ns).is("current", _ctx.current),
          vue.unref(ns).is("focusable", !_ctx.disabled),
          vue.unref(ns).is("checked", !_ctx.disabled && _ctx.checked),
          getNodeClass(_ctx.node)
        ]),
        role: "treeitem",
        tabindex: "-1",
        "aria-expanded": _ctx.expanded,
        "aria-disabled": _ctx.disabled,
        "aria-checked": _ctx.checked,
        "data-key": (_a = _ctx.node) == null ? void 0 : _a.key,
        onClick: vue.withModifiers(handleClick, ["stop"]),
        onContextmenu: handleContextMenu,
        onDragover: _cache[1] || (_cache[1] = vue.withModifiers(() => {
        }, ["prevent"])),
        onDragenter: _cache[2] || (_cache[2] = vue.withModifiers(() => {
        }, ["prevent"])),
        onDrop: vue.withModifiers(handleDrop, ["stop"])
      }, [
        vue.createElementVNode(
          "div",
          {
            class: vue.normalizeClass(vue.unref(ns).be("node", "content")),
            style: vue.normalizeStyle({
              paddingLeft: `${(_ctx.node.level - 1) * indent.value}px`,
              height: _ctx.itemSize + "px"
            })
          },
          [
            icon.value ? (vue.openBlock(), vue.createBlock(vue.unref(index$1.ElIcon), {
              key: 0,
              class: vue.normalizeClass([
                vue.unref(ns).is("leaf", !!((_b = _ctx.node) == null ? void 0 : _b.isLeaf)),
                vue.unref(ns).is("hidden", _ctx.hiddenExpandIcon),
                {
                  expanded: !((_c = _ctx.node) == null ? void 0 : _c.isLeaf) && _ctx.expanded
                },
                vue.unref(ns).be("node", "expand-icon")
              ]),
              onClick: vue.withModifiers(handleExpandIconClick, ["stop"])
            }, {
              default: vue.withCtx(() => [
                (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(icon.value)))
              ]),
              _: 1
            }, 8, ["class"])) : vue.createCommentVNode("v-if", true),
            _ctx.showCheckbox ? (vue.openBlock(), vue.createBlock(vue.unref(index$2.ElCheckbox), {
              key: 1,
              "model-value": _ctx.checked,
              indeterminate: _ctx.indeterminate,
              disabled: _ctx.disabled,
              onChange: handleCheckChange,
              onClick: _cache[0] || (_cache[0] = vue.withModifiers(() => {
              }, ["stop"]))
            }, null, 8, ["model-value", "indeterminate", "disabled"])) : vue.createCommentVNode("v-if", true),
            vue.createVNode(vue.unref(treeNodeContent["default"]), {
              node: { ..._ctx.node, expanded: _ctx.expanded }
            }, null, 8, ["node"])
          ],
          6
        )
      ], 42, _hoisted_1);
    };
  }
});
var ElTreeNode = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/tree-v2/src/tree-node.vue"]]);

exports["default"] = ElTreeNode;
//# sourceMappingURL=tree-node.js.map
