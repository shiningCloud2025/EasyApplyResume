declare var __VLS_1: {}, __VLS_18: {}, __VLS_50: {}, __VLS_53: {};
type __VLS_Slots = {} & {
    'decrease-icon'?: (props: typeof __VLS_1) => any;
} & {
    'increase-icon'?: (props: typeof __VLS_18) => any;
} & {
    prefix?: (props: typeof __VLS_50) => any;
} & {
    suffix?: (props: typeof __VLS_53) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly inputmode: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal") | (() => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal" | undefined) | ((new (...args: any[]) => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal") | (() => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal" | undefined))[], unknown, unknown, undefined, boolean>;
    readonly align: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "center" | "left" | "right") | (() => "center" | "left" | "right") | ((new (...args: any[]) => "center" | "left" | "right") | (() => "center" | "left" | "right"))[], unknown, unknown, "center", boolean>;
    readonly disabledScientific: BooleanConstructor;
    readonly ariaLabel: StringConstructor;
    readonly id: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, undefined, boolean>;
    readonly step: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 1, boolean>;
    readonly stepStrictly: BooleanConstructor;
    readonly max: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    readonly min: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    readonly modelValue: {
        readonly type: import("vue").PropType<any>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly readonly: BooleanConstructor;
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", never>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly controls: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly controlsPosition: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "right", unknown, "", boolean>;
    readonly valueOnClear: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => number | "min" | "max") | (() => number | "min" | "max" | null) | ((new (...args: any[]) => number | "min" | "max") | (() => number | "min" | "max" | null))[], unknown, unknown, null, boolean>;
    readonly name: StringConstructor;
    readonly placeholder: StringConstructor;
    readonly precision: {
        readonly type: import("vue").PropType<number>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly validateEvent: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
}, {
    /** @description get focus the input component */
    focus: () => void;
    /** @description remove focus the input component */
    blur: () => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    focus: (e: FocusEvent) => void;
    "update:modelValue": (val: number | undefined) => void;
    change: (cur: number | undefined, prev: number | undefined) => void;
    input: (val: number | null | undefined) => void;
    blur: (e: FocusEvent) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly inputmode: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal") | (() => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal" | undefined) | ((new (...args: any[]) => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal") | (() => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal" | undefined))[], unknown, unknown, undefined, boolean>;
    readonly align: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "center" | "left" | "right") | (() => "center" | "left" | "right") | ((new (...args: any[]) => "center" | "left" | "right") | (() => "center" | "left" | "right"))[], unknown, unknown, "center", boolean>;
    readonly disabledScientific: BooleanConstructor;
    readonly ariaLabel: StringConstructor;
    readonly id: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, undefined, boolean>;
    readonly step: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 1, boolean>;
    readonly stepStrictly: BooleanConstructor;
    readonly max: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    readonly min: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    readonly modelValue: {
        readonly type: import("vue").PropType<any>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly readonly: BooleanConstructor;
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", never>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly controls: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly controlsPosition: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "right", unknown, "", boolean>;
    readonly valueOnClear: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => number | "min" | "max") | (() => number | "min" | "max" | null) | ((new (...args: any[]) => number | "min" | "max") | (() => number | "min" | "max" | null))[], unknown, unknown, null, boolean>;
    readonly name: StringConstructor;
    readonly placeholder: StringConstructor;
    readonly precision: {
        readonly type: import("vue").PropType<number>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly validateEvent: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
}>> & {
    "onUpdate:modelValue"?: ((val: number | undefined) => any) | undefined;
    onInput?: ((val: number | null | undefined) => any) | undefined;
    onFocus?: ((e: FocusEvent) => any) | undefined;
    onChange?: ((cur: number | undefined, prev: number | undefined) => any) | undefined;
    onBlur?: ((e: FocusEvent) => any) | undefined;
}, {
    readonly disabled: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly id: string;
    readonly valueOnClear: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => number | "min" | "max") | (() => number | "min" | "max" | null) | ((new (...args: any[]) => number | "min" | "max") | (() => number | "min" | "max" | null))[], unknown, unknown>;
    readonly min: number;
    readonly max: number;
    readonly validateEvent: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly inputmode: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal") | (() => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal" | undefined) | ((new (...args: any[]) => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal") | (() => "search" | "text" | "none" | "url" | "email" | "tel" | "numeric" | "decimal" | undefined))[], unknown, unknown>;
    readonly readonly: boolean;
    readonly align: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "center" | "left" | "right") | (() => "center" | "left" | "right") | ((new (...args: any[]) => "center" | "left" | "right") | (() => "center" | "left" | "right"))[], unknown, unknown>;
    readonly step: number;
    readonly controls: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly controlsPosition: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "right", unknown>;
    readonly disabledScientific: boolean;
    readonly stepStrictly: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
