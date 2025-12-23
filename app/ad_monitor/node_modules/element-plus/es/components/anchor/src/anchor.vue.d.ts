declare var __VLS_1: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    container: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement | Window) | (() => string | HTMLElement | Window | null) | ((new (...args: any[]) => string | HTMLElement | Window) | (() => string | HTMLElement | Window | null))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    offset: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    bound: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    duration: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    marker: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    type: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "default" | "underline") | (() => "default" | "underline") | ((new (...args: any[]) => "default" | "underline") | (() => "default" | "underline"))[], unknown, unknown, string, boolean>;
    direction: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "horizontal" | "vertical") | (() => "horizontal" | "vertical") | ((new (...args: any[]) => "horizontal" | "vertical") | (() => "horizontal" | "vertical"))[], unknown, unknown, string, boolean>;
    selectScrollTop: BooleanConstructor;
}, {
    scrollTo: (href?: string) => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    click: (e: MouseEvent, href?: string | undefined) => void;
    change: (href: string) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    container: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement | Window) | (() => string | HTMLElement | Window | null) | ((new (...args: any[]) => string | HTMLElement | Window) | (() => string | HTMLElement | Window | null))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    offset: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    bound: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    duration: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    marker: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    type: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "default" | "underline") | (() => "default" | "underline") | ((new (...args: any[]) => "default" | "underline") | (() => "default" | "underline"))[], unknown, unknown, string, boolean>;
    direction: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "horizontal" | "vertical") | (() => "horizontal" | "vertical") | ((new (...args: any[]) => "horizontal" | "vertical") | (() => "horizontal" | "vertical"))[], unknown, unknown, string, boolean>;
    selectScrollTop: BooleanConstructor;
}>> & {
    onClick?: ((e: MouseEvent, href?: string | undefined) => any) | undefined;
    onChange?: ((href: string) => any) | undefined;
}, {
    marker: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    direction: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "horizontal" | "vertical") | (() => "horizontal" | "vertical") | ((new (...args: any[]) => "horizontal" | "vertical") | (() => "horizontal" | "vertical"))[], unknown, unknown>;
    offset: number;
    type: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "default" | "underline") | (() => "default" | "underline") | ((new (...args: any[]) => "default" | "underline") | (() => "default" | "underline"))[], unknown, unknown>;
    duration: number;
    bound: number;
    selectScrollTop: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
