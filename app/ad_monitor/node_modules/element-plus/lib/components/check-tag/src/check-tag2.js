'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var checkTag = require('./check-tag.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../hooks/use-namespace/index.js');
var event = require('../../../constants/event.js');

const _sfc_main = vue.defineComponent({
  ...{
    name: "ElCheckTag"
  },
  __name: "check-tag",
  props: checkTag.checkTagProps,
  emits: checkTag.checkTagEmits,
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const ns = index.useNamespace("check-tag");
    const containerKls = vue.computed(() => [
      ns.b(),
      ns.is("checked", props.checked),
      ns.is("disabled", props.disabled),
      ns.m(props.type || "primary")
    ]);
    const handleChange = () => {
      if (props.disabled)
        return;
      const checked = !props.checked;
      emit(event.CHANGE_EVENT, checked);
      emit("update:checked", checked);
    };
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "span",
        {
          class: vue.normalizeClass(containerKls.value),
          onClick: handleChange
        },
        [
          vue.renderSlot(_ctx.$slots, "default")
        ],
        2
      );
    };
  }
});
var CheckTag = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/check-tag/src/check-tag.vue"]]);

exports["default"] = CheckTag;
//# sourceMappingURL=check-tag2.js.map
