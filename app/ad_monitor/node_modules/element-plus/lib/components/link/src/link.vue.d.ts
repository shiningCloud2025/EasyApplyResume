declare var __VLS_11: {}, __VLS_13: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_11) => any;
} & {
    icon?: (props: typeof __VLS_13) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "default" | "primary" | "success" | "warning" | "info" | "danger", unknown, undefined, boolean>;
    readonly underline: import("element-plus/es/utils").EpPropFinalized<readonly [BooleanConstructor, StringConstructor], boolean | "always" | "never" | "hover", unknown, undefined, boolean>;
    readonly disabled: BooleanConstructor;
    readonly href: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly target: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string) | (() => string) | ((new (...args: any[]) => string) | (() => string))[], unknown, unknown, "_self", boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    click: (evt: MouseEvent) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "default" | "primary" | "success" | "warning" | "info" | "danger", unknown, undefined, boolean>;
    readonly underline: import("element-plus/es/utils").EpPropFinalized<readonly [BooleanConstructor, StringConstructor], boolean | "always" | "never" | "hover", unknown, undefined, boolean>;
    readonly disabled: BooleanConstructor;
    readonly href: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly target: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string) | (() => string) | ((new (...args: any[]) => string) | (() => string))[], unknown, unknown, "_self", boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    onClick?: ((evt: MouseEvent) => any) | undefined;
}, {
    readonly disabled: boolean;
    readonly underline: import("element-plus/es/utils").EpPropMergeType<readonly [BooleanConstructor, StringConstructor], boolean | "always" | "never" | "hover", unknown>;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "default" | "primary" | "success" | "warning" | "info" | "danger", unknown>;
    readonly target: string;
    readonly href: string;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
