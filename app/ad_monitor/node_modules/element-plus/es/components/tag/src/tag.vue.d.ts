declare var __VLS_1: {}, __VLS_21: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
} & {
    default?: (props: typeof __VLS_21) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "primary" | "success" | "warning" | "info" | "danger", unknown, "primary", boolean>;
    readonly closable: BooleanConstructor;
    readonly disableTransitions: BooleanConstructor;
    readonly hit: BooleanConstructor;
    readonly color: StringConstructor;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly effect: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "dark" | "light" | "plain", unknown, "light", boolean>;
    readonly round: BooleanConstructor;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    click: (evt: MouseEvent) => void;
    close: (evt: MouseEvent) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "primary" | "success" | "warning" | "info" | "danger", unknown, "primary", boolean>;
    readonly closable: BooleanConstructor;
    readonly disableTransitions: BooleanConstructor;
    readonly hit: BooleanConstructor;
    readonly color: StringConstructor;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly effect: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "dark" | "light" | "plain", unknown, "light", boolean>;
    readonly round: BooleanConstructor;
}>> & {
    onClick?: ((evt: MouseEvent) => any) | undefined;
    onClose?: ((evt: MouseEvent) => any) | undefined;
}, {
    readonly round: boolean;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "primary" | "success" | "warning" | "info" | "danger", unknown>;
    readonly effect: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "dark" | "light" | "plain", unknown>;
    readonly closable: boolean;
    readonly disableTransitions: boolean;
    readonly hit: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
