declare var __VLS_1: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly layout: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "horizontal" | "vertical", unknown, "horizontal", boolean>;
    readonly lazy: BooleanConstructor;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    resize: (index: number, sizes: number[]) => void;
    collapse: (index: number, type: "end" | "start", sizes: number[]) => void;
    resizeStart: (index: number, sizes: number[]) => void;
    resizeEnd: (index: number, sizes: number[]) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly layout: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "horizontal" | "vertical", unknown, "horizontal", boolean>;
    readonly lazy: BooleanConstructor;
}>> & {
    onResize?: ((index: number, sizes: number[]) => any) | undefined;
    onCollapse?: ((index: number, type: "end" | "start", sizes: number[]) => any) | undefined;
    onResizeStart?: ((index: number, sizes: number[]) => any) | undefined;
    onResizeEnd?: ((index: number, sizes: number[]) => any) | undefined;
}, {
    readonly layout: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "horizontal" | "vertical", unknown>;
    readonly lazy: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
