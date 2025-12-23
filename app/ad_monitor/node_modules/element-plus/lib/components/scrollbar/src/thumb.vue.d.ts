declare const __VLS_export: import("vue").DefineComponent<{
    readonly vertical: BooleanConstructor;
    readonly size: StringConstructor;
    readonly move: NumberConstructor;
    readonly ratio: {
        readonly type: import("vue").PropType<number>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly always: BooleanConstructor;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly vertical: BooleanConstructor;
    readonly size: StringConstructor;
    readonly move: NumberConstructor;
    readonly ratio: {
        readonly type: import("vue").PropType<number>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly always: BooleanConstructor;
}>>, {
    readonly always: boolean;
    readonly vertical: boolean;
}, {}>;
declare const _default: typeof __VLS_export;
export default _default;
