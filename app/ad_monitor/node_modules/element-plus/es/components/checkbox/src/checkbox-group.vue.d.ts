import type { CheckboxGroupValueType } from './checkbox-group';
declare var __VLS_7: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_7) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly ariaLabel: StringConstructor;
    readonly modelValue: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => CheckboxGroupValueType) | (() => CheckboxGroupValueType) | ((new (...args: any[]) => CheckboxGroupValueType) | (() => CheckboxGroupValueType))[], unknown, unknown, () => never[], boolean>;
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly min: NumberConstructor;
    readonly max: NumberConstructor;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", never>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly fill: StringConstructor;
    readonly textColor: StringConstructor;
    readonly tag: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "div", boolean>;
    readonly validateEvent: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly options: {
        readonly type: import("vue").PropType<import("./checkbox-group").CheckboxOption[]>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly props: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => {
        value?: string;
        label?: string;
        disabled?: string;
    }) | (() => {
        value?: string;
        label?: string;
        disabled?: string;
    }) | ((new (...args: any[]) => {
        value?: string;
        label?: string;
        disabled?: string;
    }) | (() => {
        value?: string;
        label?: string;
        disabled?: string;
    }))[], unknown, unknown, () => Required<{
        value?: string;
        label?: string;
        disabled?: string;
    }>, boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "button" | "checkbox", unknown, "checkbox", boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    "update:modelValue": (val: CheckboxGroupValueType) => void;
    change: (val: import("element-plus").CheckboxValueType[]) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly ariaLabel: StringConstructor;
    readonly modelValue: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => CheckboxGroupValueType) | (() => CheckboxGroupValueType) | ((new (...args: any[]) => CheckboxGroupValueType) | (() => CheckboxGroupValueType))[], unknown, unknown, () => never[], boolean>;
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly min: NumberConstructor;
    readonly max: NumberConstructor;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", never>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly fill: StringConstructor;
    readonly textColor: StringConstructor;
    readonly tag: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "div", boolean>;
    readonly validateEvent: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly options: {
        readonly type: import("vue").PropType<import("./checkbox-group").CheckboxOption[]>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly props: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => {
        value?: string;
        label?: string;
        disabled?: string;
    }) | (() => {
        value?: string;
        label?: string;
        disabled?: string;
    }) | ((new (...args: any[]) => {
        value?: string;
        label?: string;
        disabled?: string;
    }) | (() => {
        value?: string;
        label?: string;
        disabled?: string;
    }))[], unknown, unknown, () => Required<{
        value?: string;
        label?: string;
        disabled?: string;
    }>, boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "button" | "checkbox", unknown, "checkbox", boolean>;
}>> & {
    "onUpdate:modelValue"?: ((val: CheckboxGroupValueType) => any) | undefined;
    onChange?: ((val: import("element-plus").CheckboxValueType[]) => any) | undefined;
}, {
    readonly disabled: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "button" | "checkbox", unknown>;
    readonly props: {
        value?: string;
        label?: string;
        disabled?: string;
    };
    readonly tag: string;
    readonly modelValue: CheckboxGroupValueType;
    readonly validateEvent: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
