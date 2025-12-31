import { defineComponent, ref, onMounted, computed, provide, reactive, toRefs, watch, openBlock, createElementBlock, unref, normalizeClass, renderSlot, Fragment, renderList, createBlock, resolveDynamicComponent, mergeProps, nextTick } from 'vue';
import { radioGroupProps, radioGroupEmits, radioDefaultProps } from './radio-group.mjs';
import { radioGroupKey } from './constants.mjs';
import { isEqual, omit } from 'lodash-unified';
import Radio from './radio2.mjs';
import RadioButton from './radio-button2.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { useId } from '../../../hooks/use-id/index.mjs';
import { useFormItem, useFormItemInputId } from '../../form/src/hooks/use-form-item.mjs';
import { debugWarn } from '../../../utils/error.mjs';
import { UPDATE_MODEL_EVENT, CHANGE_EVENT } from '../../../constants/event.mjs';

const _hoisted_1 = ["id", "aria-label", "aria-labelledby"];
const _sfc_main = defineComponent({
  ...{
    name: "ElRadioGroup"
  },
  __name: "radio-group",
  props: radioGroupProps,
  emits: radioGroupEmits,
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const ns = useNamespace("radio");
    const radioId = useId();
    const radioGroupRef = ref();
    const { formItem } = useFormItem();
    const { inputId: groupId, isLabeledByFormItem } = useFormItemInputId(props, {
      formItemContext: formItem
    });
    const changeEvent = (value) => {
      emit(UPDATE_MODEL_EVENT, value);
      nextTick(() => emit(CHANGE_EVENT, value));
    };
    onMounted(() => {
      const radios = radioGroupRef.value.querySelectorAll("[type=radio]");
      const firstLabel = radios[0];
      if (!Array.from(radios).some((radio) => radio.checked) && firstLabel) {
        firstLabel.tabIndex = 0;
      }
    });
    const name = computed(() => {
      return props.name || radioId.value;
    });
    const aliasProps = computed(() => ({
      ...radioDefaultProps,
      ...props.props
    }));
    const getOptionProps = (option) => {
      const { label, value, disabled } = aliasProps.value;
      const base = {
        label: option[label],
        value: option[value],
        disabled: option[disabled]
      };
      return { ...omit(option, [label, value, disabled]), ...base };
    };
    const optionComponent = computed(
      () => props.type === "button" ? RadioButton : Radio
    );
    provide(
      radioGroupKey,
      reactive({
        ...toRefs(props),
        changeEvent,
        name
      })
    );
    watch(
      () => props.modelValue,
      (newVal, oldValue) => {
        if (props.validateEvent && !isEqual(newVal, oldValue)) {
          formItem == null ? void 0 : formItem.validate("change").catch((err) => debugWarn(err));
        }
      }
    );
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock("div", {
        id: unref(groupId),
        ref_key: "radioGroupRef",
        ref: radioGroupRef,
        class: normalizeClass(unref(ns).b("group")),
        role: "radiogroup",
        "aria-label": !unref(isLabeledByFormItem) ? _ctx.ariaLabel || "radio-group" : void 0,
        "aria-labelledby": unref(isLabeledByFormItem) ? unref(formItem).labelId : void 0
      }, [
        renderSlot(_ctx.$slots, "default", {}, () => [
          (openBlock(true), createElementBlock(
            Fragment,
            null,
            renderList(_ctx.options, (item, index) => {
              return openBlock(), createBlock(
                resolveDynamicComponent(optionComponent.value),
                mergeProps({ key: index }, { ref_for: true }, getOptionProps(item)),
                null,
                16
              );
            }),
            128
          ))
        ])
      ], 10, _hoisted_1);
    };
  }
});
var RadioGroup = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/radio/src/radio-group.vue"]]);

export { RadioGroup as default };
//# sourceMappingURL=radio-group2.mjs.map
