declare var __VLS_11: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_11) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly size: import("element-plus/es/utils").EpPropFinalized<readonly [NumberConstructor, StringConstructor], "" | "small" | "default" | "large", number, "", boolean>;
    readonly shape: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "circle" | "square", unknown, "circle", boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly src: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly alt: StringConstructor;
    readonly srcSet: StringConstructor;
    readonly fit: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "fill" | "contain" | "-moz-initial" | "inherit" | "initial" | "revert" | "revert-layer" | "unset" | "none" | "cover" | "scale-down") | (() => import("csstype").Property.ObjectFit | undefined) | ((new (...args: any[]) => "fill" | "contain" | "-moz-initial" | "inherit" | "initial" | "revert" | "revert-layer" | "unset" | "none" | "cover" | "scale-down") | (() => import("csstype").Property.ObjectFit | undefined))[], unknown, unknown, "cover", boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    error: (evt: Event) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly size: import("element-plus/es/utils").EpPropFinalized<readonly [NumberConstructor, StringConstructor], "" | "small" | "default" | "large", number, "", boolean>;
    readonly shape: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "circle" | "square", unknown, "circle", boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly src: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly alt: StringConstructor;
    readonly srcSet: StringConstructor;
    readonly fit: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "fill" | "contain" | "-moz-initial" | "inherit" | "initial" | "revert" | "revert-layer" | "unset" | "none" | "cover" | "scale-down") | (() => import("csstype").Property.ObjectFit | undefined) | ((new (...args: any[]) => "fill" | "contain" | "-moz-initial" | "inherit" | "initial" | "revert" | "revert-layer" | "unset" | "none" | "cover" | "scale-down") | (() => import("csstype").Property.ObjectFit | undefined))[], unknown, unknown, "cover", boolean>;
}>> & {
    onError?: ((evt: Event) => any) | undefined;
}, {
    readonly size: import("element-plus/es/utils").EpPropMergeType<readonly [NumberConstructor, StringConstructor], "" | "small" | "default" | "large", number>;
    readonly shape: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "circle" | "square", unknown>;
    readonly src: string;
    readonly fit: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "fill" | "contain" | "-moz-initial" | "inherit" | "initial" | "revert" | "revert-layer" | "unset" | "none" | "cover" | "scale-down") | (() => import("csstype").Property.ObjectFit | undefined) | ((new (...args: any[]) => "fill" | "contain" | "-moz-initial" | "inherit" | "initial" | "revert" | "revert-layer" | "unset" | "none" | "cover" | "scale-down") | (() => import("csstype").Property.ObjectFit | undefined))[], unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
