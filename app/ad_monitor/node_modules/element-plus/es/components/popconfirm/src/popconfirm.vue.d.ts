declare var __VLS_22: {
    confirm: (e: MouseEvent) => void;
    cancel: (e: MouseEvent) => void;
}, __VLS_40: {};
type __VLS_Slots = {} & {
    actions?: (props: typeof __VLS_22) => any;
} & {
    reference?: (props: typeof __VLS_40) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly title: StringConstructor;
    readonly confirmButtonText: StringConstructor;
    readonly cancelButtonText: StringConstructor;
    readonly confirmButtonType: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "text" | "default" | "primary" | "success" | "warning" | "info" | "danger", unknown, "primary", boolean>;
    readonly cancelButtonType: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "text" | "default" | "primary" | "success" | "warning" | "info" | "danger", unknown, "text", boolean>;
    readonly icon: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown, () => any, boolean>;
    readonly iconColor: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "#f90", boolean>;
    readonly hideIcon: BooleanConstructor;
    readonly hideAfter: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 200, boolean>;
    readonly effect: {
        readonly default: "light";
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string) | (() => import("element-plus").PopperEffect) | ((new (...args: any[]) => string) | (() => import("element-plus").PopperEffect))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        readonly __epPropKey: true;
    };
    readonly teleported: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly persistent: BooleanConstructor;
    readonly width: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, 150, boolean>;
    readonly virtualTriggering: BooleanConstructor;
    readonly virtualRef: {
        readonly type: import("vue").PropType<import("element-plus").Measurable>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, {
    popperRef: import("vue").ComputedRef<import("element-plus").PopperInstance | undefined>;
    hide: () => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    cancel: (e: MouseEvent) => void;
    confirm: (e: MouseEvent) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly title: StringConstructor;
    readonly confirmButtonText: StringConstructor;
    readonly cancelButtonText: StringConstructor;
    readonly confirmButtonType: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "text" | "default" | "primary" | "success" | "warning" | "info" | "danger", unknown, "primary", boolean>;
    readonly cancelButtonType: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "text" | "default" | "primary" | "success" | "warning" | "info" | "danger", unknown, "text", boolean>;
    readonly icon: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown, () => any, boolean>;
    readonly iconColor: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "#f90", boolean>;
    readonly hideIcon: BooleanConstructor;
    readonly hideAfter: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 200, boolean>;
    readonly effect: {
        readonly default: "light";
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string) | (() => import("element-plus").PopperEffect) | ((new (...args: any[]) => string) | (() => import("element-plus").PopperEffect))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        readonly __epPropKey: true;
    };
    readonly teleported: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly persistent: BooleanConstructor;
    readonly width: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, 150, boolean>;
    readonly virtualTriggering: BooleanConstructor;
    readonly virtualRef: {
        readonly type: import("vue").PropType<import("element-plus").Measurable>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    onCancel?: ((e: MouseEvent) => any) | undefined;
    onConfirm?: ((e: MouseEvent) => any) | undefined;
}, {
    readonly width: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>;
    readonly icon: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>;
    readonly effect: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string) | (() => import("element-plus").PopperEffect) | ((new (...args: any[]) => string) | (() => import("element-plus").PopperEffect))[], unknown, unknown>;
    readonly hideAfter: number;
    readonly teleported: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly virtualTriggering: boolean;
    readonly persistent: boolean;
    readonly confirmButtonType: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "text" | "default" | "primary" | "success" | "warning" | "info" | "danger", unknown>;
    readonly cancelButtonType: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "text" | "default" | "primary" | "success" | "warning" | "info" | "danger", unknown>;
    readonly iconColor: string;
    readonly hideIcon: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
