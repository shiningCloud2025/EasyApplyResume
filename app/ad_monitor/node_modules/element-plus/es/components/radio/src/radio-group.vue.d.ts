declare var __VLS_1: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly ariaLabel: StringConstructor;
    readonly id: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, undefined, boolean>;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", never>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly modelValue: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor, BooleanConstructor], unknown, unknown, undefined, boolean>;
    readonly fill: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly textColor: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly name: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, undefined, boolean>;
    readonly validateEvent: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly options: {
        readonly type: import("vue").PropType<import("./radio-group").radioOption[]>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly props: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => import("./radio-group").radioOptionProp) | (() => import("./radio-group").radioOptionProp) | ((new (...args: any[]) => import("./radio-group").radioOptionProp) | (() => import("./radio-group").radioOptionProp))[], unknown, unknown, () => Required<import("./radio-group").radioOptionProp>, boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "button" | "radio", unknown, "radio", boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    "update:modelValue": (val: string | number | boolean | undefined) => void;
    change: (val: string | number | boolean | undefined) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly ariaLabel: StringConstructor;
    readonly id: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, undefined, boolean>;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", never>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly modelValue: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor, BooleanConstructor], unknown, unknown, undefined, boolean>;
    readonly fill: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly textColor: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly name: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, undefined, boolean>;
    readonly validateEvent: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly options: {
        readonly type: import("vue").PropType<import("./radio-group").radioOption[]>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly props: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => import("./radio-group").radioOptionProp) | (() => import("./radio-group").radioOptionProp) | ((new (...args: any[]) => import("./radio-group").radioOptionProp) | (() => import("./radio-group").radioOptionProp))[], unknown, unknown, () => Required<import("./radio-group").radioOptionProp>, boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "button" | "radio", unknown, "radio", boolean>;
}>> & {
    "onUpdate:modelValue"?: ((val: string | number | boolean | undefined) => any) | undefined;
    onChange?: ((val: string | number | boolean | undefined) => any) | undefined;
}, {
    readonly disabled: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly fill: string;
    readonly id: string;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "button" | "radio", unknown>;
    readonly props: import("./radio-group").radioOptionProp;
    readonly name: string;
    readonly modelValue: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor, BooleanConstructor], unknown, unknown>;
    readonly validateEvent: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly textColor: string;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
