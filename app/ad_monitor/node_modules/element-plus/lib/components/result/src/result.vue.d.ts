declare var __VLS_1: {}, __VLS_8: {}, __VLS_10: {}, __VLS_12: {};
type __VLS_Slots = {} & {
    icon?: (props: typeof __VLS_1) => any;
} & {
    title?: (props: typeof __VLS_8) => any;
} & {
    'sub-title'?: (props: typeof __VLS_10) => any;
} & {
    extra?: (props: typeof __VLS_12) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly subTitle: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly icon: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "error" | "primary" | "success" | "warning" | "info", unknown, "info", boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly subTitle: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly icon: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "error" | "primary" | "success" | "warning" | "info", unknown, "info", boolean>;
}>>, {
    readonly title: string;
    readonly icon: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "error" | "primary" | "success" | "warning" | "info", unknown>;
    readonly subTitle: string;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
