declare var __VLS_11: {}, __VLS_13: {};
type __VLS_Slots = {} & {
    dot?: (props: typeof __VLS_11) => any;
} & {
    default?: (props: typeof __VLS_13) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly timestamp: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly hideTimestamp: BooleanConstructor;
    readonly center: BooleanConstructor;
    readonly placement: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "top" | "bottom", unknown, "bottom", boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "primary" | "success" | "warning" | "info" | "danger", unknown, "", boolean>;
    readonly color: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly size: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "large" | "normal", unknown, "normal", boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly hollow: BooleanConstructor;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly timestamp: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly hideTimestamp: BooleanConstructor;
    readonly center: BooleanConstructor;
    readonly placement: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "top" | "bottom", unknown, "bottom", boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "primary" | "success" | "warning" | "info" | "danger", unknown, "", boolean>;
    readonly color: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly size: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "large" | "normal", unknown, "normal", boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly hollow: BooleanConstructor;
}>>, {
    readonly center: boolean;
    readonly color: string;
    readonly size: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "large" | "normal", unknown>;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "primary" | "success" | "warning" | "info" | "danger", unknown>;
    readonly placement: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "top" | "bottom", unknown>;
    readonly timestamp: string;
    readonly hideTimestamp: boolean;
    readonly hollow: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
