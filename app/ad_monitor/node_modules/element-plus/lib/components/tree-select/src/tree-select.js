'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var lodashUnified = require('lodash-unified');
var index = require('../../select/index.js');
var index$1 = require('../../tree/index.js');
var select$1 = require('./select.js');
var tree$1 = require('./tree.js');
var cacheOptions = require('./cache-options.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var select = require('../../select/src/select.js');
var tree = require('../../tree/src/tree.js');

const _sfc_main = vue.defineComponent({
  name: "ElTreeSelect",
  inheritAttrs: false,
  props: {
    ...select.selectProps,
    ...tree.treeProps,
    cacheData: {
      type: Array,
      default: () => []
    }
  },
  setup(props, context) {
    const { slots, expose } = context;
    const select = vue.ref();
    const tree = vue.ref();
    const key = vue.computed(() => props.nodeKey || props.valueKey || "value");
    const selectProps2 = select$1.useSelect(props, context, { select, tree, key });
    const { cacheOptions: cacheOptions$1, ...treeProps2 } = tree$1.useTree(props, context, {
      select,
      tree,
      key
    });
    const methods = vue.reactive({});
    expose(methods);
    vue.onMounted(() => {
      Object.assign(methods, {
        ...lodashUnified.pick(tree.value, [
          "filter",
          "updateKeyChildren",
          "getCheckedNodes",
          "setCheckedNodes",
          "getCheckedKeys",
          "setCheckedKeys",
          "setChecked",
          "getHalfCheckedNodes",
          "getHalfCheckedKeys",
          "getCurrentKey",
          "getCurrentNode",
          "setCurrentKey",
          "setCurrentNode",
          "getNode",
          "remove",
          "append",
          "insertBefore",
          "insertAfter"
        ]),
        ...lodashUnified.pick(select.value, ["focus", "blur", "selectedLabel"]),
        treeRef: tree.value,
        selectRef: select.value
      });
    });
    return () => vue.h(
      index.ElSelect,
      vue.reactive({
        ...selectProps2,
        ref: (ref2) => select.value = ref2
      }),
      {
        ...slots,
        default: () => [
          vue.h(cacheOptions["default"], { data: cacheOptions$1.value }),
          vue.h(
            index$1.ElTree,
            vue.reactive({
              ...treeProps2,
              ref: (ref2) => tree.value = ref2
            })
          )
        ]
      }
    );
  }
});
var TreeSelect = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/tree-select/src/tree-select.vue"]]);

exports["default"] = TreeSelect;
//# sourceMappingURL=tree-select.js.map
