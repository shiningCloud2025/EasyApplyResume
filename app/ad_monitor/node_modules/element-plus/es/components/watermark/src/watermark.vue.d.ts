declare var __VLS_1: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly zIndex: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 9, boolean>;
    readonly rotate: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, -22, boolean>;
    readonly width: NumberConstructor;
    readonly height: NumberConstructor;
    readonly image: StringConstructor;
    readonly content: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | string[]) | (() => string | string[]) | ((new (...args: any[]) => string | string[]) | (() => string | string[]))[], unknown, unknown, "Element Plus", boolean>;
    readonly font: {
        readonly type: import("vue").PropType<import("./watermark").WatermarkFontType>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly gap: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => [number, number]) | (() => [number, number]) | ((new (...args: any[]) => [number, number]) | (() => [number, number]))[], unknown, unknown, () => number[], boolean>;
    readonly offset: {
        readonly type: import("vue").PropType<[number, number]>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly zIndex: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 9, boolean>;
    readonly rotate: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, -22, boolean>;
    readonly width: NumberConstructor;
    readonly height: NumberConstructor;
    readonly image: StringConstructor;
    readonly content: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | string[]) | (() => string | string[]) | ((new (...args: any[]) => string | string[]) | (() => string | string[]))[], unknown, unknown, "Element Plus", boolean>;
    readonly font: {
        readonly type: import("vue").PropType<import("./watermark").WatermarkFontType>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly gap: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => [number, number]) | (() => [number, number]) | ((new (...args: any[]) => [number, number]) | (() => [number, number]))[], unknown, unknown, () => number[], boolean>;
    readonly offset: {
        readonly type: import("vue").PropType<[number, number]>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>>, {
    readonly content: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | string[]) | (() => string | string[]) | ((new (...args: any[]) => string | string[]) | (() => string | string[]))[], unknown, unknown>;
    readonly rotate: number;
    readonly zIndex: number;
    readonly gap: [number, number];
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
