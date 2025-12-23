declare var __VLS_10: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_10) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    placement: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown, string, boolean>;
    reference: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => HTMLElement | import("@floating-ui/dom").VirtualElement) | (() => HTMLElement | import("@floating-ui/dom").VirtualElement | null) | ((new (...args: any[]) => HTMLElement | import("@floating-ui/dom").VirtualElement) | (() => HTMLElement | import("@floating-ui/dom").VirtualElement | null))[], unknown, unknown, null, boolean>;
    strategy: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "fixed" | "absolute") | (() => import("@floating-ui/utils").Strategy) | ((new (...args: any[]) => "fixed" | "absolute") | (() => import("@floating-ui/utils").Strategy))[], "fixed" | "absolute", unknown, string, boolean>;
    offset: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    showArrow: BooleanConstructor;
    zIndex: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    close: () => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    placement: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown, string, boolean>;
    reference: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => HTMLElement | import("@floating-ui/dom").VirtualElement) | (() => HTMLElement | import("@floating-ui/dom").VirtualElement | null) | ((new (...args: any[]) => HTMLElement | import("@floating-ui/dom").VirtualElement) | (() => HTMLElement | import("@floating-ui/dom").VirtualElement | null))[], unknown, unknown, null, boolean>;
    strategy: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "fixed" | "absolute") | (() => import("@floating-ui/utils").Strategy) | ((new (...args: any[]) => "fixed" | "absolute") | (() => import("@floating-ui/utils").Strategy))[], "fixed" | "absolute", unknown, string, boolean>;
    offset: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    showArrow: BooleanConstructor;
    zIndex: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
}>> & {
    onClose?: (() => any) | undefined;
}, {
    zIndex: number;
    offset: number;
    placement: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown>;
    strategy: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "fixed" | "absolute") | (() => import("@floating-ui/utils").Strategy) | ((new (...args: any[]) => "fixed" | "absolute") | (() => import("@floating-ui/utils").Strategy))[], "fixed" | "absolute", unknown>;
    reference: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => HTMLElement | import("@floating-ui/dom").VirtualElement) | (() => HTMLElement | import("@floating-ui/dom").VirtualElement | null) | ((new (...args: any[]) => HTMLElement | import("@floating-ui/dom").VirtualElement) | (() => HTMLElement | import("@floating-ui/dom").VirtualElement | null))[], unknown, unknown>;
    showArrow: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
