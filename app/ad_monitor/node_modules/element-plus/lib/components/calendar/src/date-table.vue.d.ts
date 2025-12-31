declare var __VLS_1: {
    data: {
        isSelected: boolean;
        type: string;
        day: string;
        date: Date;
    };
};
type __VLS_Slots = {} & {
    'date-cell'?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly selectedDay: {
        readonly type: import("vue").PropType<import("dayjs").Dayjs>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly range: {
        readonly type: import("vue").PropType<[import("dayjs").Dayjs, import("dayjs").Dayjs]>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly date: {
        readonly type: import("vue").PropType<import("dayjs").Dayjs>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly hideHeader: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, {
    /** @description toggle date panel */
    getFormattedDate: (day: number, type: import("./date-table").CalendarDateCellType) => import("dayjs").Dayjs;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    pick: (value: import("dayjs").Dayjs) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly selectedDay: {
        readonly type: import("vue").PropType<import("dayjs").Dayjs>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly range: {
        readonly type: import("vue").PropType<[import("dayjs").Dayjs, import("dayjs").Dayjs]>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly date: {
        readonly type: import("vue").PropType<import("dayjs").Dayjs>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly hideHeader: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    onPick?: ((value: import("dayjs").Dayjs) => any) | undefined;
}, {}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
