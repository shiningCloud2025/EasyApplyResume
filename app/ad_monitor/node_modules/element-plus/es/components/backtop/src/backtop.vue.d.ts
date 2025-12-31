declare var __VLS_6: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_6) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly visibilityHeight: {
        readonly type: NumberConstructor;
        readonly default: 200;
    };
    readonly target: {
        readonly type: StringConstructor;
        readonly default: "";
    };
    readonly right: {
        readonly type: NumberConstructor;
        readonly default: 40;
    };
    readonly bottom: {
        readonly type: NumberConstructor;
        readonly default: 40;
    };
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    click: (evt: MouseEvent) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly visibilityHeight: {
        readonly type: NumberConstructor;
        readonly default: 200;
    };
    readonly target: {
        readonly type: StringConstructor;
        readonly default: "";
    };
    readonly right: {
        readonly type: NumberConstructor;
        readonly default: 40;
    };
    readonly bottom: {
        readonly type: NumberConstructor;
        readonly default: 40;
    };
}>> & {
    onClick?: ((evt: MouseEvent) => any) | undefined;
}, {
    readonly right: number;
    readonly bottom: number;
    readonly target: string;
    readonly visibilityHeight: number;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
