'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var splitter = require('./splitter.js');
var type = require('./type.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var useContainer = require('./hooks/useContainer.js');
var useSize = require('./hooks/useSize.js');
var useResize = require('./hooks/useResize.js');
var index = require('../../../hooks/use-namespace/index.js');
var index$1 = require('../../../hooks/use-ordered-children/index.js');

const _sfc_main = vue.defineComponent({
  ...{
    name: "ElSplitter"
  },
  __name: "splitter",
  props: splitter.splitterProps,
  emits: splitter.splitterEmits,
  setup(__props, { emit: __emit }) {
    const ns = index.useNamespace("splitter");
    const emits = __emit;
    const props = __props;
    const layout = vue.toRef(props, "layout");
    const lazy = vue.toRef(props, "lazy");
    const { containerEl, containerSize } = useContainer.useContainer(layout);
    const {
      removeChild: unregisterPanel,
      children: panels,
      addChild: registerPanel,
      ChildrenSorter: PanelsSorter
    } = index$1.useOrderedChildren(vue.getCurrentInstance(), "ElSplitterPanel");
    vue.watch(panels, () => {
      movingIndex.value = null;
      panels.value.forEach((instance, index) => {
        instance.setIndex(index);
      });
    });
    const { percentSizes, pxSizes } = useSize.useSize(panels, containerSize);
    const {
      lazyOffset,
      movingIndex,
      onMoveStart,
      onMoving,
      onMoveEnd,
      onCollapse
    } = useResize.useResize(panels, containerSize, pxSizes, lazy);
    const splitterStyles = vue.computed(() => {
      return {
        [ns.cssVarBlockName("bar-offset")]: lazy.value ? `${lazyOffset.value}px` : void 0
      };
    });
    const onResizeStart = (index) => {
      onMoveStart(index);
      emits("resizeStart", index, pxSizes.value);
    };
    const onResize = (index, offset) => {
      onMoving(index, offset);
      if (!lazy.value) {
        emits("resize", index, pxSizes.value);
      }
    };
    const onResizeEnd = async (index) => {
      onMoveEnd();
      await vue.nextTick();
      emits("resizeEnd", index, pxSizes.value);
    };
    const onCollapsible = (index, type) => {
      onCollapse(index, type);
      emits("collapse", index, type, pxSizes.value);
    };
    vue.provide(
      type.splitterRootContextKey,
      vue.reactive({
        panels,
        percentSizes,
        pxSizes,
        layout,
        lazy,
        movingIndex,
        containerSize,
        onMoveStart: onResizeStart,
        onMoving: onResize,
        onMoveEnd: onResizeEnd,
        onCollapse: onCollapsible,
        registerPanel,
        unregisterPanel
      })
    );
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "div",
        {
          ref_key: "containerEl",
          ref: containerEl,
          class: vue.normalizeClass([vue.unref(ns).b(), vue.unref(ns).e(layout.value)]),
          style: vue.normalizeStyle(splitterStyles.value)
        },
        [
          vue.renderSlot(_ctx.$slots, "default"),
          vue.createVNode(vue.unref(PanelsSorter)),
          vue.createCommentVNode(" Prevent iframe touch events from breaking "),
          vue.unref(movingIndex) ? (vue.openBlock(), vue.createElementBlock(
            "div",
            {
              key: 0,
              class: vue.normalizeClass([vue.unref(ns).e("mask"), vue.unref(ns).e(`mask-${layout.value}`)])
            },
            null,
            2
          )) : vue.createCommentVNode("v-if", true)
        ],
        6
      );
    };
  }
});
var Splitter = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/splitter/src/splitter.vue"]]);

exports["default"] = Splitter;
//# sourceMappingURL=splitter2.js.map
