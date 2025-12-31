import { defineComponent, getCurrentInstance, useSlots, inject, ref, computed, watch, reactive, onBeforeUnmount, onBeforeUpdate, withDirectives, openBlock, createElementBlock, normalizeClass, unref, renderSlot, vShow, createCommentVNode } from 'vue';
import { tabsRootContextKey } from './constants.mjs';
import { tabPaneProps } from './tab-pane.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { throwError } from '../../../utils/error.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';

const _hoisted_1 = ["id", "aria-hidden", "aria-labelledby"];
const COMPONENT_NAME = "ElTabPane";
const _sfc_main = defineComponent({
  ...{
    name: COMPONENT_NAME
  },
  __name: "tab-pane",
  props: tabPaneProps,
  setup(__props) {
    const props = __props;
    const instance = getCurrentInstance();
    const slots = useSlots();
    const tabsRoot = inject(tabsRootContextKey);
    if (!tabsRoot)
      throwError(COMPONENT_NAME, "usage: <el-tabs><el-tab-pane /></el-tabs/>");
    const ns = useNamespace("tab-pane");
    const paneRef = ref();
    const index = ref();
    const isClosable = computed(() => {
      var _a;
      return (_a = props.closable) != null ? _a : tabsRoot.props.closable;
    });
    const active = computed(
      () => {
        var _a;
        return tabsRoot.currentName.value === ((_a = props.name) != null ? _a : index.value);
      }
    );
    const loaded = ref(active.value);
    const paneName = computed(() => {
      var _a;
      return (_a = props.name) != null ? _a : index.value;
    });
    const shouldBeRender = computed(
      () => !props.lazy || loaded.value || active.value
    );
    const isFocusInsidePane = () => {
      var _a;
      return (_a = paneRef.value) == null ? void 0 : _a.contains(document.activeElement);
    };
    watch(active, (val) => {
      if (val)
        loaded.value = true;
    });
    const pane = reactive({
      uid: instance.uid,
      getVnode: () => instance.vnode,
      slots,
      props,
      paneName,
      active,
      index,
      isClosable,
      isFocusInsidePane
    });
    tabsRoot.registerPane(pane);
    onBeforeUnmount(() => {
      tabsRoot.unregisterPane(pane);
    });
    onBeforeUpdate(() => {
      var _a;
      if (slots.label)
        (_a = tabsRoot.nav$.value) == null ? void 0 : _a.scheduleRender();
    });
    return (_ctx, _cache) => {
      return shouldBeRender.value ? withDirectives((openBlock(), createElementBlock("div", {
        key: 0,
        id: `pane-${paneName.value}`,
        ref_key: "paneRef",
        ref: paneRef,
        class: normalizeClass(unref(ns).b()),
        role: "tabpanel",
        "aria-hidden": !active.value,
        "aria-labelledby": `tab-${paneName.value}`
      }, [
        renderSlot(_ctx.$slots, "default")
      ], 10, _hoisted_1)), [
        [vShow, active.value]
      ]) : createCommentVNode("v-if", true);
    };
  }
});
var TabPane = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/tabs/src/tab-pane.vue"]]);

export { TabPane as default };
//# sourceMappingURL=tab-pane2.mjs.map
