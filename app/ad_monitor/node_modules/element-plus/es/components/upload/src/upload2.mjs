import { defineComponent, shallowRef, computed, onBeforeUnmount, provide, toRef, openBlock, createElementBlock, createBlock, unref, createSlots, withCtx, createVNode, mergeProps, renderSlot, createCommentVNode } from 'vue';
import { uploadContextKey } from './constants.mjs';
import UploadList from './upload-list2.mjs';
import UploadContent from './upload-content2.mjs';
import { useHandlers } from './use-handlers.mjs';
import { uploadProps } from './upload.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useFormDisabled } from '../../form/src/hooks/use-form-common-props.mjs';

const _sfc_main = defineComponent({
  ...{
    name: "ElUpload"
  },
  __name: "upload",
  props: uploadProps,
  setup(__props, { expose: __expose }) {
    const props = __props;
    const disabled = useFormDisabled();
    const uploadRef = shallowRef();
    const {
      abort,
      submit,
      clearFiles,
      uploadFiles,
      handleStart,
      handleError,
      handleRemove,
      handleSuccess,
      handleProgress,
      revokeFileObjectURL
    } = useHandlers(props, uploadRef);
    const isPictureCard = computed(() => props.listType === "picture-card");
    const uploadContentProps = computed(() => ({
      ...props,
      fileList: uploadFiles.value,
      onStart: handleStart,
      onProgress: handleProgress,
      onSuccess: handleSuccess,
      onError: handleError,
      onRemove: handleRemove
    }));
    onBeforeUnmount(() => {
      uploadFiles.value.forEach(revokeFileObjectURL);
    });
    provide(uploadContextKey, {
      accept: toRef(props, "accept")
    });
    __expose({
      abort,
      submit,
      clearFiles,
      handleStart,
      handleRemove
    });
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock("div", null, [
        isPictureCard.value && _ctx.showFileList ? (openBlock(), createBlock(UploadList, {
          key: 0,
          disabled: unref(disabled),
          "list-type": _ctx.listType,
          files: unref(uploadFiles),
          crossorigin: _ctx.crossorigin,
          "handle-preview": _ctx.onPreview,
          onRemove: unref(handleRemove)
        }, createSlots({
          append: withCtx(() => [
            createVNode(
              UploadContent,
              mergeProps({
                ref_key: "uploadRef",
                ref: uploadRef
              }, uploadContentProps.value),
              {
                default: withCtx(() => [
                  _ctx.$slots.trigger ? renderSlot(_ctx.$slots, "trigger", { key: 0 }) : createCommentVNode("v-if", true),
                  !_ctx.$slots.trigger && _ctx.$slots.default ? renderSlot(_ctx.$slots, "default", { key: 1 }) : createCommentVNode("v-if", true)
                ]),
                _: 3
              },
              16
            )
          ]),
          _: 2
        }, [
          _ctx.$slots.file ? {
            name: "default",
            fn: withCtx(({ file, index }) => [
              renderSlot(_ctx.$slots, "file", {
                file,
                index
              })
            ]),
            key: "0"
          } : void 0
        ]), 1032, ["disabled", "list-type", "files", "crossorigin", "handle-preview", "onRemove"])) : createCommentVNode("v-if", true),
        !isPictureCard.value || isPictureCard.value && !_ctx.showFileList ? (openBlock(), createBlock(
          UploadContent,
          mergeProps({
            key: 1,
            ref_key: "uploadRef",
            ref: uploadRef
          }, uploadContentProps.value),
          {
            default: withCtx(() => [
              _ctx.$slots.trigger ? renderSlot(_ctx.$slots, "trigger", { key: 0 }) : createCommentVNode("v-if", true),
              !_ctx.$slots.trigger && _ctx.$slots.default ? renderSlot(_ctx.$slots, "default", { key: 1 }) : createCommentVNode("v-if", true)
            ]),
            _: 3
          },
          16
        )) : createCommentVNode("v-if", true),
        _ctx.$slots.trigger ? renderSlot(_ctx.$slots, "default", { key: 2 }) : createCommentVNode("v-if", true),
        renderSlot(_ctx.$slots, "tip"),
        !isPictureCard.value && _ctx.showFileList ? (openBlock(), createBlock(UploadList, {
          key: 3,
          disabled: unref(disabled),
          "list-type": _ctx.listType,
          files: unref(uploadFiles),
          crossorigin: _ctx.crossorigin,
          "handle-preview": _ctx.onPreview,
          onRemove: unref(handleRemove)
        }, createSlots({
          _: 2
        }, [
          _ctx.$slots.file ? {
            name: "default",
            fn: withCtx(({ file, index }) => [
              renderSlot(_ctx.$slots, "file", {
                file,
                index
              })
            ]),
            key: "0"
          } : void 0
        ]), 1032, ["disabled", "list-type", "files", "crossorigin", "handle-preview", "onRemove"])) : createCommentVNode("v-if", true)
      ]);
    };
  }
});
var Upload = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/upload/src/upload.vue"]]);

export { Upload as default };
//# sourceMappingURL=upload2.mjs.map
