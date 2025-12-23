declare var __VLS_1: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_1) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly accordion: BooleanConstructor;
    readonly modelValue: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | number | import("./collapse").CollapseActiveName[]) | (() => import("./collapse").CollapseModelValue) | ((new (...args: any[]) => string | number | import("./collapse").CollapseActiveName[]) | (() => import("./collapse").CollapseModelValue))[], unknown, unknown, () => [], boolean>;
    readonly expandIconPosition: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "left" | "right") | (() => import("./collapse").CollapseIconPositionType) | ((new (...args: any[]) => "left" | "right") | (() => import("./collapse").CollapseIconPositionType))[], unknown, unknown, "right", boolean>;
    readonly beforeCollapse: {
        readonly type: import("vue").PropType<(name: import("./collapse").CollapseActiveName) => import("element-plus/es/utils").Awaitable<boolean>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, {
    /** @description active names */
    activeNames: import("vue").Ref<(string | number)[]>;
    /** @description set active names */
    setActiveNames: (_activeNames: import("./collapse").CollapseActiveName[]) => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    "update:modelValue": (value: import("./collapse").CollapseModelValue) => void;
    change: (value: import("./collapse").CollapseModelValue) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly accordion: BooleanConstructor;
    readonly modelValue: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | number | import("./collapse").CollapseActiveName[]) | (() => import("./collapse").CollapseModelValue) | ((new (...args: any[]) => string | number | import("./collapse").CollapseActiveName[]) | (() => import("./collapse").CollapseModelValue))[], unknown, unknown, () => [], boolean>;
    readonly expandIconPosition: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "left" | "right") | (() => import("./collapse").CollapseIconPositionType) | ((new (...args: any[]) => "left" | "right") | (() => import("./collapse").CollapseIconPositionType))[], unknown, unknown, "right", boolean>;
    readonly beforeCollapse: {
        readonly type: import("vue").PropType<(name: import("./collapse").CollapseActiveName) => import("element-plus/es/utils").Awaitable<boolean>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    "onUpdate:modelValue"?: ((value: import("./collapse").CollapseModelValue) => any) | undefined;
    onChange?: ((value: import("./collapse").CollapseModelValue) => any) | undefined;
}, {
    readonly modelValue: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | number | import("./collapse").CollapseActiveName[]) | (() => import("./collapse").CollapseModelValue) | ((new (...args: any[]) => string | number | import("./collapse").CollapseActiveName[]) | (() => import("./collapse").CollapseModelValue))[], unknown, unknown>;
    readonly expandIconPosition: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "left" | "right") | (() => import("./collapse").CollapseIconPositionType) | ((new (...args: any[]) => "left" | "right") | (() => import("./collapse").CollapseIconPositionType))[], unknown, unknown>;
    readonly accordion: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
