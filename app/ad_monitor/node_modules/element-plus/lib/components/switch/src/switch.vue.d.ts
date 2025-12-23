declare var __VLS_1: {}, __VLS_13: {}, __VLS_25: {}, __VLS_47: {}, __VLS_59: {}, __VLS_71: {};
type __VLS_Slots = {} & {
    inactive?: (props: typeof __VLS_1) => any;
} & {
    inactive?: (props: typeof __VLS_13) => any;
} & {
    active?: (props: typeof __VLS_25) => any;
} & {
    'active-action'?: (props: typeof __VLS_47) => any;
} & {
    'inactive-action'?: (props: typeof __VLS_59) => any;
} & {
    active?: (props: typeof __VLS_71) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly ariaLabel: StringConstructor;
    readonly modelValue: import("element-plus/es/utils").EpPropFinalized<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown, false, boolean>;
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly loading: BooleanConstructor;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "" | "small" | "default" | "large") | (() => "" | "small" | "default" | "large") | ((new (...args: any[]) => "" | "small" | "default" | "large") | (() => "" | "small" | "default" | "large"))[], unknown, "" | "small" | "default" | "large">>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly width: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, "", boolean>;
    readonly inlinePrompt: BooleanConstructor;
    readonly inactiveActionIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly activeActionIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly activeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly inactiveIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly activeText: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly inactiveText: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly activeValue: import("element-plus/es/utils").EpPropFinalized<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown, true, boolean>;
    readonly inactiveValue: import("element-plus/es/utils").EpPropFinalized<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown, false, boolean>;
    readonly name: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly validateEvent: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly beforeChange: {
        readonly type: import("vue").PropType<() => Promise<boolean> | boolean>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly id: StringConstructor;
    readonly tabindex: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, {
    /**
     *  @description manual focus to the switch component
     **/
    focus: () => void;
    /**
     * @description whether Switch is checked
     */
    checked: import("vue").ComputedRef<boolean>;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    "update:modelValue": (val: string | number | boolean) => void;
    change: (val: string | number | boolean) => void;
    input: (val: string | number | boolean) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly ariaLabel: StringConstructor;
    readonly modelValue: import("element-plus/es/utils").EpPropFinalized<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown, false, boolean>;
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly loading: BooleanConstructor;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "" | "small" | "default" | "large") | (() => "" | "small" | "default" | "large") | ((new (...args: any[]) => "" | "small" | "default" | "large") | (() => "" | "small" | "default" | "large"))[], unknown, "" | "small" | "default" | "large">>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly width: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, "", boolean>;
    readonly inlinePrompt: BooleanConstructor;
    readonly inactiveActionIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly activeActionIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly activeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly inactiveIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly activeText: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly inactiveText: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly activeValue: import("element-plus/es/utils").EpPropFinalized<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown, true, boolean>;
    readonly inactiveValue: import("element-plus/es/utils").EpPropFinalized<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown, false, boolean>;
    readonly name: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly validateEvent: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly beforeChange: {
        readonly type: import("vue").PropType<() => Promise<boolean> | boolean>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly id: StringConstructor;
    readonly tabindex: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    "onUpdate:modelValue"?: ((val: string | number | boolean) => any) | undefined;
    onInput?: ((val: string | number | boolean) => any) | undefined;
    onChange?: ((val: string | number | boolean) => any) | undefined;
}, {
    readonly disabled: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly width: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>;
    readonly name: string;
    readonly loading: boolean;
    readonly modelValue: import("element-plus/es/utils").EpPropMergeType<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown>;
    readonly validateEvent: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly activeText: string;
    readonly inactiveText: string;
    readonly activeValue: import("element-plus/es/utils").EpPropMergeType<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown>;
    readonly inactiveValue: import("element-plus/es/utils").EpPropMergeType<readonly [BooleanConstructor, StringConstructor, NumberConstructor], unknown, unknown>;
    readonly inlinePrompt: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
