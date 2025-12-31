declare const __VLS_export: import("vue").DefineComponent<{
    readonly color: {
        readonly type: import("vue").PropType<import("../utils/color").default>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly vertical: BooleanConstructor;
    readonly disabled: BooleanConstructor;
}, {
    /**
     * @description update alpha slider manually
     * @type {Function}
     */
    update: () => void;
    /**
     * @description bar element ref
     * @type {HTMLElement}
     */
    bar: import("vue").ShallowRef<HTMLElement | undefined>;
    /**
     * @description thumb element ref
     * @type {HTMLElement}
     */
    thumb: import("vue").ShallowRef<HTMLElement | undefined>;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly color: {
        readonly type: import("vue").PropType<import("../utils/color").default>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly vertical: BooleanConstructor;
    readonly disabled: BooleanConstructor;
}>>, {
    readonly disabled: boolean;
    readonly vertical: boolean;
}, {}>;
declare const _default: typeof __VLS_export;
export default _default;
