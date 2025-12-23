declare var __VLS_1: {}, __VLS_13: {}, __VLS_15: {};
type __VLS_Slots = {} & {
    header?: (props: typeof __VLS_1) => any;
} & {
    default?: (props: typeof __VLS_13) => any;
} & {
    footer?: (props: typeof __VLS_15) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly center: BooleanConstructor;
    readonly alignCenter: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly draggable: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly overflow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly fullscreen: BooleanConstructor;
    readonly headerClass: StringConstructor;
    readonly bodyClass: StringConstructor;
    readonly footerClass: StringConstructor;
    readonly showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly ariaLevel: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "2", boolean>;
}, {
    resetPosition: () => void;
    updatePosition: () => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    close: () => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly center: BooleanConstructor;
    readonly alignCenter: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly draggable: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly overflow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly fullscreen: BooleanConstructor;
    readonly headerClass: StringConstructor;
    readonly bodyClass: StringConstructor;
    readonly footerClass: StringConstructor;
    readonly showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly ariaLevel: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "2", boolean>;
}>> & {
    onClose?: (() => any) | undefined;
}, {
    readonly title: string;
    readonly center: boolean;
    readonly overflow: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly ariaLevel: string;
    readonly alignCenter: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly draggable: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly showClose: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly fullscreen: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
