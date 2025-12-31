declare var __VLS_1: {}, __VLS_3: {}, __VLS_5: {};
type __VLS_Slots = {} & {
    header?: (props: typeof __VLS_1) => any;
} & {
    default?: (props: typeof __VLS_3) => any;
} & {
    footer?: (props: typeof __VLS_5) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly header: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly footer: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly bodyStyle: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | false | import("vue").CSSProperties | import("vue").StyleValue[]) | (() => import("vue").StyleValue) | ((new (...args: any[]) => string | false | import("vue").CSSProperties | import("vue").StyleValue[]) | (() => import("vue").StyleValue))[], unknown, unknown, "", boolean>;
    readonly headerClass: StringConstructor;
    readonly bodyClass: StringConstructor;
    readonly footerClass: StringConstructor;
    readonly shadow: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "always" | "never" | "hover", unknown, undefined, boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly header: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly footer: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly bodyStyle: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | false | import("vue").CSSProperties | import("vue").StyleValue[]) | (() => import("vue").StyleValue) | ((new (...args: any[]) => string | false | import("vue").CSSProperties | import("vue").StyleValue[]) | (() => import("vue").StyleValue))[], unknown, unknown, "", boolean>;
    readonly headerClass: StringConstructor;
    readonly bodyClass: StringConstructor;
    readonly footerClass: StringConstructor;
    readonly shadow: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "always" | "never" | "hover", unknown, undefined, boolean>;
}>>, {
    readonly footer: string;
    readonly header: string;
    readonly bodyStyle: import("vue").StyleValue;
    readonly shadow: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "always" | "never" | "hover", unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
