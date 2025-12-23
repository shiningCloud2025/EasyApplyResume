declare var __VLS_1: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly to: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | (() => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | ((new (...args: any[]) => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | (() => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric))[], unknown, unknown, "", boolean>;
    readonly replace: BooleanConstructor;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly to: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | (() => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | ((new (...args: any[]) => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | (() => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric))[], unknown, unknown, "", boolean>;
    readonly replace: BooleanConstructor;
}>>, {
    readonly replace: boolean;
    readonly to: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | (() => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | ((new (...args: any[]) => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | (() => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric))[], unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
