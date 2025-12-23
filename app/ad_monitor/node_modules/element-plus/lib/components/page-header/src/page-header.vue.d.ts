declare var __VLS_1: {}, __VLS_3: {}, __VLS_15: {}, __VLS_22: {}, __VLS_24: {}, __VLS_26: {};
type __VLS_Slots = {} & {
    breadcrumb?: (props: typeof __VLS_1) => any;
} & {
    icon?: (props: typeof __VLS_3) => any;
} & {
    title?: (props: typeof __VLS_15) => any;
} & {
    content?: (props: typeof __VLS_22) => any;
} & {
    extra?: (props: typeof __VLS_24) => any;
} & {
    default?: (props: typeof __VLS_26) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly icon: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown, () => any, boolean>;
    readonly title: StringConstructor;
    readonly content: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    back: () => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly icon: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown, () => any, boolean>;
    readonly title: StringConstructor;
    readonly content: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
}>> & {
    onBack?: (() => any) | undefined;
}, {
    readonly content: string;
    readonly icon: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
