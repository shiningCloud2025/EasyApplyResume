declare var __VLS_8: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_8) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "primary" | "success" | "warning" | "info" | "danger", unknown, "", boolean>;
    readonly size: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "small" | "default" | "large", unknown, "", boolean>;
    readonly truncated: BooleanConstructor;
    readonly lineClamp: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly tag: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "span", boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "primary" | "success" | "warning" | "info" | "danger", unknown, "", boolean>;
    readonly size: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "small" | "default" | "large", unknown, "", boolean>;
    readonly truncated: BooleanConstructor;
    readonly lineClamp: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly tag: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "span", boolean>;
}>>, {
    readonly size: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", unknown>;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "primary" | "success" | "warning" | "info" | "danger", unknown>;
    readonly tag: string;
    readonly truncated: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
