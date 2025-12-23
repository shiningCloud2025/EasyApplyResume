declare var __VLS_25: {}, __VLS_27: {
    current: number;
    total: number;
};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_25) => any;
} & {
    indicators?: (props: typeof __VLS_27) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    modelValue: BooleanConstructor;
    current: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    showArrow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    placement: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown, string, boolean>;
    contentStyle: {
        readonly type: import("vue").PropType<import("vue").CSSProperties>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    mask: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => boolean | {
        style?: import("vue").CSSProperties;
        color?: string;
    }) | (() => import("element-plus").TourMask) | ((new (...args: any[]) => boolean | {
        style?: import("vue").CSSProperties;
        color?: string;
    }) | (() => import("element-plus").TourMask))[], unknown, unknown, boolean, boolean>;
    gap: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => import("element-plus").TourGap) | (() => import("element-plus").TourGap) | ((new (...args: any[]) => import("element-plus").TourGap) | (() => import("element-plus").TourGap))[], unknown, unknown, () => {
        offset: number;
        radius: number;
    }, boolean>;
    zIndex: {
        readonly type: import("vue").PropType<number>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    scrollIntoViewOptions: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions) | ((new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions))[], unknown, unknown, () => {
        block: string;
    }, boolean>;
    type: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "default" | "primary") | (() => "default" | "primary") | ((new (...args: any[]) => "default" | "primary") | (() => "default" | "primary"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    appendTo: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>) | ((new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>))[], unknown, unknown, string, boolean>;
    closeOnPressEscape: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    targetAreaClickable: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    "update:modelValue": (value: boolean) => void;
    change: (current: number) => void;
    close: (current: number) => void;
    finish: () => void;
    "update:current": (current: number) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    modelValue: BooleanConstructor;
    current: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, number, boolean>;
    showArrow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    placement: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown, string, boolean>;
    contentStyle: {
        readonly type: import("vue").PropType<import("vue").CSSProperties>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    mask: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => boolean | {
        style?: import("vue").CSSProperties;
        color?: string;
    }) | (() => import("element-plus").TourMask) | ((new (...args: any[]) => boolean | {
        style?: import("vue").CSSProperties;
        color?: string;
    }) | (() => import("element-plus").TourMask))[], unknown, unknown, boolean, boolean>;
    gap: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => import("element-plus").TourGap) | (() => import("element-plus").TourGap) | ((new (...args: any[]) => import("element-plus").TourGap) | (() => import("element-plus").TourGap))[], unknown, unknown, () => {
        offset: number;
        radius: number;
    }, boolean>;
    zIndex: {
        readonly type: import("vue").PropType<number>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    scrollIntoViewOptions: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions) | ((new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions))[], unknown, unknown, () => {
        block: string;
    }, boolean>;
    type: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "default" | "primary") | (() => "default" | "primary") | ((new (...args: any[]) => "default" | "primary") | (() => "default" | "primary"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    appendTo: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>) | ((new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>))[], unknown, unknown, string, boolean>;
    closeOnPressEscape: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
    targetAreaClickable: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, boolean, boolean>;
}>> & {
    "onUpdate:modelValue"?: ((value: boolean) => any) | undefined;
    onChange?: ((current: number) => any) | undefined;
    onClose?: ((current: number) => any) | undefined;
    onFinish?: (() => any) | undefined;
    "onUpdate:current"?: ((current: number) => any) | undefined;
}, {
    mask: import("element-plus").TourMask;
    gap: import("element-plus").TourGap;
    modelValue: boolean;
    placement: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown>;
    appendTo: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>) | ((new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>))[], unknown, unknown>;
    scrollIntoViewOptions: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions) | ((new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions))[], unknown, unknown>;
    showArrow: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    showClose: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    closeOnPressEscape: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    current: number;
    targetAreaClickable: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
