declare var __VLS_1: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    ariaControls: StringConstructor;
    modelValue: {
        type: (NumberConstructor | StringConstructor | BooleanConstructor)[];
        default: undefined;
    };
    label: {
        type: (ObjectConstructor | NumberConstructor | StringConstructor | BooleanConstructor)[];
        default: undefined;
    };
    value: {
        type: (ObjectConstructor | NumberConstructor | StringConstructor | BooleanConstructor)[];
        default: undefined;
    };
    indeterminate: BooleanConstructor;
    disabled: {
        type: BooleanConstructor;
        default: undefined;
    };
    checked: BooleanConstructor;
    name: {
        type: StringConstructor;
        default: undefined;
    };
    trueValue: {
        type: (NumberConstructor | StringConstructor)[];
        default: undefined;
    };
    falseValue: {
        type: (NumberConstructor | StringConstructor)[];
        default: undefined;
    };
    trueLabel: {
        type: (NumberConstructor | StringConstructor)[];
        default: undefined;
    };
    falseLabel: {
        type: (NumberConstructor | StringConstructor)[];
        default: undefined;
    };
    id: {
        type: StringConstructor;
        default: undefined;
    };
    border: BooleanConstructor;
    size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", never>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    tabindex: (NumberConstructor | StringConstructor)[];
    validateEvent: {
        type: BooleanConstructor;
        default: boolean;
    };
    ariaLabel: StringConstructor;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    "update:modelValue": (val: import("./checkbox").CheckboxValueType) => void;
    change: (val: import("./checkbox").CheckboxValueType) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    ariaControls: StringConstructor;
    modelValue: {
        type: (NumberConstructor | StringConstructor | BooleanConstructor)[];
        default: undefined;
    };
    label: {
        type: (ObjectConstructor | NumberConstructor | StringConstructor | BooleanConstructor)[];
        default: undefined;
    };
    value: {
        type: (ObjectConstructor | NumberConstructor | StringConstructor | BooleanConstructor)[];
        default: undefined;
    };
    indeterminate: BooleanConstructor;
    disabled: {
        type: BooleanConstructor;
        default: undefined;
    };
    checked: BooleanConstructor;
    name: {
        type: StringConstructor;
        default: undefined;
    };
    trueValue: {
        type: (NumberConstructor | StringConstructor)[];
        default: undefined;
    };
    falseValue: {
        type: (NumberConstructor | StringConstructor)[];
        default: undefined;
    };
    trueLabel: {
        type: (NumberConstructor | StringConstructor)[];
        default: undefined;
    };
    falseLabel: {
        type: (NumberConstructor | StringConstructor)[];
        default: undefined;
    };
    id: {
        type: StringConstructor;
        default: undefined;
    };
    border: BooleanConstructor;
    size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", never>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    tabindex: (NumberConstructor | StringConstructor)[];
    validateEvent: {
        type: BooleanConstructor;
        default: boolean;
    };
    ariaLabel: StringConstructor;
}>> & {
    "onUpdate:modelValue"?: ((val: import("./checkbox").CheckboxValueType) => any) | undefined;
    onChange?: ((val: import("./checkbox").CheckboxValueType) => any) | undefined;
}, {
    label: string | number | boolean | Record<string, any>;
    disabled: boolean;
    border: boolean;
    value: string | number | boolean | Record<string, any>;
    id: string;
    name: string;
    modelValue: string | number | boolean;
    validateEvent: boolean;
    indeterminate: boolean;
    checked: boolean;
    trueValue: string | number;
    falseValue: string | number;
    trueLabel: string | number;
    falseLabel: string | number;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
