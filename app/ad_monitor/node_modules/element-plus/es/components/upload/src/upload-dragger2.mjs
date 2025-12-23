import { defineComponent, inject, ref, openBlock, createElementBlock, normalizeClass, unref, withModifiers, renderSlot } from 'vue';
import { throwError } from '../../../utils/error.mjs';
import { uploadContextKey } from './constants.mjs';
import { uploadDraggerProps, uploadDraggerEmits } from './upload-dragger.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { useFormDisabled } from '../../form/src/hooks/use-form-common-props.mjs';

const COMPONENT_NAME = "ElUploadDrag";
const _sfc_main = defineComponent({
  ...{
    name: COMPONENT_NAME
  },
  __name: "upload-dragger",
  props: uploadDraggerProps,
  emits: uploadDraggerEmits,
  setup(__props, { emit: __emit }) {
    const emit = __emit;
    const uploaderContext = inject(uploadContextKey);
    if (!uploaderContext) {
      throwError(
        COMPONENT_NAME,
        "usage: <el-upload><el-upload-dragger /></el-upload>"
      );
    }
    const ns = useNamespace("upload");
    const dragover = ref(false);
    const disabled = useFormDisabled();
    const onDrop = (e) => {
      if (disabled.value)
        return;
      dragover.value = false;
      e.stopPropagation();
      const files = Array.from(e.dataTransfer.files);
      const items = e.dataTransfer.items || [];
      files.forEach((file, index) => {
        var _a;
        const item = items[index];
        const entry = (_a = item == null ? void 0 : item.webkitGetAsEntry) == null ? void 0 : _a.call(item);
        if (entry) {
          file.isDirectory = entry.isDirectory;
        }
      });
      emit("file", files);
    };
    const onDragover = () => {
      if (!disabled.value)
        dragover.value = true;
    };
    const onDragleave = (e) => {
      if (!e.currentTarget.contains(e.relatedTarget))
        dragover.value = false;
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "div",
        {
          class: normalizeClass([unref(ns).b("dragger"), unref(ns).is("dragover", dragover.value)]),
          onDrop: withModifiers(onDrop, ["prevent"]),
          onDragover: withModifiers(onDragover, ["prevent"]),
          onDragleave: withModifiers(onDragleave, ["prevent"])
        },
        [
          renderSlot(_ctx.$slots, "default")
        ],
        34
      );
    };
  }
});
var UploadDragger = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/upload/src/upload-dragger.vue"]]);

export { UploadDragger as default };
//# sourceMappingURL=upload-dragger2.mjs.map
