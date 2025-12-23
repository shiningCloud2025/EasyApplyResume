declare var __VLS_36: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_36) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly initialIndex: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly height: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly trigger: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "click" | "hover", unknown, "hover", boolean>;
    readonly autoplay: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly interval: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 3000, boolean>;
    readonly indicatorPosition: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "none" | "outside", unknown, "", boolean>;
    readonly arrow: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "always" | "never" | "hover", unknown, "hover", boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "card", unknown, "", boolean>;
    readonly cardScale: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0.83, boolean>;
    readonly loop: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly direction: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "horizontal" | "vertical", unknown, "horizontal", boolean>;
    readonly pauseOnHover: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly motionBlur: BooleanConstructor;
}, {
    /** @description active slide index */
    activeIndex: import("vue").WritableComputedRef<number>;
    /** @description manually switch slide, index of the slide to be switched to, starting from 0; or the `name` of corresponding `el-carousel-item` */
    setActiveItem: (index: number | string) => void;
    /** @description switch to the previous slide */
    prev: () => void;
    /** @description switch to the next slide */
    next: () => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    change: (current: number, prev: number) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly initialIndex: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly height: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly trigger: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "click" | "hover", unknown, "hover", boolean>;
    readonly autoplay: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly interval: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 3000, boolean>;
    readonly indicatorPosition: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "none" | "outside", unknown, "", boolean>;
    readonly arrow: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "always" | "never" | "hover", unknown, "hover", boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "card", unknown, "", boolean>;
    readonly cardScale: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0.83, boolean>;
    readonly loop: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly direction: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "horizontal" | "vertical", unknown, "horizontal", boolean>;
    readonly pauseOnHover: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly motionBlur: BooleanConstructor;
}>> & {
    onChange?: ((current: number, prev: number) => any) | undefined;
}, {
    readonly height: string;
    readonly direction: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "horizontal" | "vertical", unknown>;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "card", unknown>;
    readonly arrow: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "always" | "never" | "hover", unknown>;
    readonly trigger: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "click" | "hover", unknown>;
    readonly loop: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly interval: number;
    readonly initialIndex: number;
    readonly autoplay: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly indicatorPosition: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "none" | "outside", unknown>;
    readonly cardScale: number;
    readonly pauseOnHover: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly motionBlur: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
