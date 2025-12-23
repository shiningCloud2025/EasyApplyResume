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
     * @description bar element ref
     */
    bar: import("vue").ShallowRef<HTMLElement | undefined>;
    /**
     * @description thumb element ref
     */
    thumb: import("vue").ShallowRef<HTMLElement | undefined>;
    /**
     * @description thumb top position, only for vertical slider
     */
    thumbTop: import("vue").Ref<number>;
    /**
     * @description update hue slider manually
     */
    update: () => void;
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
