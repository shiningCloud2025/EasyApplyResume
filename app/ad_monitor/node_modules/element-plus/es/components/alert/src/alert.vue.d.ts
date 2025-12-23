declare var __VLS_11: {}, __VLS_18: {}, __VLS_20: {};
type __VLS_Slots = {} & {
    icon?: (props: typeof __VLS_11) => any;
} & {
    title?: (props: typeof __VLS_18) => any;
} & {
    default?: (props: typeof __VLS_20) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly description: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "error" | "primary" | "success" | "warning" | "info", unknown, "info", boolean>;
    readonly closable: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly closeText: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly showIcon: BooleanConstructor;
    readonly center: BooleanConstructor;
    readonly effect: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "dark" | "light", unknown, "light", boolean>;
    readonly showAfter: NumberConstructor;
    readonly hideAfter: NumberConstructor;
    readonly autoClose: NumberConstructor;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    close: (evt: MouseEvent) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly description: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "error" | "primary" | "success" | "warning" | "info", unknown, "info", boolean>;
    readonly closable: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly closeText: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly showIcon: BooleanConstructor;
    readonly center: BooleanConstructor;
    readonly effect: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "dark" | "light", unknown, "light", boolean>;
    readonly showAfter: NumberConstructor;
    readonly hideAfter: NumberConstructor;
    readonly autoClose: NumberConstructor;
}>> & {
    onClose?: ((evt: MouseEvent) => any) | undefined;
}, {
    readonly title: string;
    readonly center: boolean;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "error" | "primary" | "success" | "warning" | "info", unknown>;
    readonly description: string;
    readonly effect: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "dark" | "light", unknown>;
    readonly closable: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly closeText: string;
    readonly showIcon: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
