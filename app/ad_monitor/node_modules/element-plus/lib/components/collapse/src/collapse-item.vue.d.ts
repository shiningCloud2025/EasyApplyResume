declare var __VLS_1: {
    isActive: boolean | undefined;
}, __VLS_3: {
    isActive: boolean | undefined;
}, __VLS_20: {};
type __VLS_Slots = {} & {
    title?: (props: typeof __VLS_1) => any;
} & {
    icon?: (props: typeof __VLS_3) => any;
} & {
    default?: (props: typeof __VLS_20) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly name: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | number) | (() => import("element-plus").CollapseActiveName) | ((new (...args: any[]) => string | number) | (() => import("element-plus").CollapseActiveName))[], unknown, unknown, undefined, boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly disabled: BooleanConstructor;
}, {
    /** @description current collapse-item whether active */
    isActive: import("vue").ComputedRef<boolean | undefined>;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly name: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | number) | (() => import("element-plus").CollapseActiveName) | ((new (...args: any[]) => string | number) | (() => import("element-plus").CollapseActiveName))[], unknown, unknown, undefined, boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly disabled: BooleanConstructor;
}>>, {
    readonly title: string;
    readonly disabled: boolean;
    readonly name: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | number) | (() => import("element-plus").CollapseActiveName) | ((new (...args: any[]) => string | number) | (() => import("element-plus").CollapseActiveName))[], unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
