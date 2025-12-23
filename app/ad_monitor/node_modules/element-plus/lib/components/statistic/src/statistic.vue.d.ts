declare var __VLS_1: {}, __VLS_3: {}, __VLS_5: {};
type __VLS_Slots = {} & {
    title?: (props: typeof __VLS_1) => any;
} & {
    prefix?: (props: typeof __VLS_3) => any;
} & {
    suffix?: (props: typeof __VLS_5) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly decimalSeparator: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, ".", boolean>;
    readonly groupSeparator: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, ",", boolean>;
    readonly precision: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly formatter: FunctionConstructor;
    readonly value: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => number | import("dayjs").Dayjs) | (() => number | import("dayjs").Dayjs) | ((new (...args: any[]) => number | import("dayjs").Dayjs) | (() => number | import("dayjs").Dayjs))[], unknown, unknown, 0, boolean>;
    readonly prefix: StringConstructor;
    readonly suffix: StringConstructor;
    readonly title: StringConstructor;
    readonly valueStyle: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | false | import("vue").CSSProperties | import("vue").StyleValue[]) | (() => import("vue").StyleValue) | ((new (...args: any[]) => string | false | import("vue").CSSProperties | import("vue").StyleValue[]) | (() => import("vue").StyleValue))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, {
    /**
     * @description current display value
     */
    displayValue: import("vue").ComputedRef<any>;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly decimalSeparator: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, ".", boolean>;
    readonly groupSeparator: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, ",", boolean>;
    readonly precision: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly formatter: FunctionConstructor;
    readonly value: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => number | import("dayjs").Dayjs) | (() => number | import("dayjs").Dayjs) | ((new (...args: any[]) => number | import("dayjs").Dayjs) | (() => number | import("dayjs").Dayjs))[], unknown, unknown, 0, boolean>;
    readonly prefix: StringConstructor;
    readonly suffix: StringConstructor;
    readonly title: StringConstructor;
    readonly valueStyle: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | false | import("vue").CSSProperties | import("vue").StyleValue[]) | (() => import("vue").StyleValue) | ((new (...args: any[]) => string | false | import("vue").CSSProperties | import("vue").StyleValue[]) | (() => import("vue").StyleValue))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>>, {
    readonly value: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => number | import("dayjs").Dayjs) | (() => number | import("dayjs").Dayjs) | ((new (...args: any[]) => number | import("dayjs").Dayjs) | (() => number | import("dayjs").Dayjs))[], unknown, unknown>;
    readonly decimalSeparator: string;
    readonly groupSeparator: string;
    readonly precision: number;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
