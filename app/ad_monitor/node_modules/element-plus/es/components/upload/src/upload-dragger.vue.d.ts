declare var __VLS_1: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    file: (file: File[]) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
}>> & {
    onFile?: ((file: File[]) => any) | undefined;
}, {
    readonly disabled: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
