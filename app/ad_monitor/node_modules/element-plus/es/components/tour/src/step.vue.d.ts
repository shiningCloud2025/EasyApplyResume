import type { TourBtnProps } from './types';
declare var __VLS_11: {}, __VLS_13: {};
type __VLS_Slots = {} & {
    header?: (props: typeof __VLS_11) => any;
} & {
    default?: (props: typeof __VLS_13) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    target: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement | (() => HTMLElement | null)) | (() => string | HTMLElement | (() => HTMLElement | null) | null) | ((new (...args: any[]) => string | HTMLElement | (() => HTMLElement | null)) | (() => string | HTMLElement | (() => HTMLElement | null) | null))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    title: StringConstructor;
    description: StringConstructor;
    showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    showArrow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    placement: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown, string, boolean>;
    mask: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => boolean | {
        style?: import("vue").CSSProperties;
        color?: string;
    }) | (() => import("./types").TourMask) | ((new (...args: any[]) => boolean | {
        style?: import("vue").CSSProperties;
        color?: string;
    }) | (() => import("./types").TourMask))[], unknown, unknown, undefined, boolean>;
    contentStyle: {
        readonly type: import("vue").PropType<import("vue").CSSProperties>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    prevButtonProps: {
        readonly type: import("vue").PropType<TourBtnProps>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    nextButtonProps: {
        readonly type: import("vue").PropType<TourBtnProps>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    scrollIntoViewOptions: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions) | ((new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions))[], unknown, unknown, undefined, boolean>;
    type: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "default" | "primary") | (() => "default" | "primary") | ((new (...args: any[]) => "default" | "primary") | (() => "default" | "primary"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    close: () => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    target: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement | (() => HTMLElement | null)) | (() => string | HTMLElement | (() => HTMLElement | null) | null) | ((new (...args: any[]) => string | HTMLElement | (() => HTMLElement | null)) | (() => string | HTMLElement | (() => HTMLElement | null) | null))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    title: StringConstructor;
    description: StringConstructor;
    showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    showArrow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    placement: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown, string, boolean>;
    mask: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => boolean | {
        style?: import("vue").CSSProperties;
        color?: string;
    }) | (() => import("./types").TourMask) | ((new (...args: any[]) => boolean | {
        style?: import("vue").CSSProperties;
        color?: string;
    }) | (() => import("./types").TourMask))[], unknown, unknown, undefined, boolean>;
    contentStyle: {
        readonly type: import("vue").PropType<import("vue").CSSProperties>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    prevButtonProps: {
        readonly type: import("vue").PropType<TourBtnProps>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    nextButtonProps: {
        readonly type: import("vue").PropType<TourBtnProps>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    scrollIntoViewOptions: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions) | ((new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions))[], unknown, unknown, undefined, boolean>;
    type: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "default" | "primary") | (() => "default" | "primary") | ((new (...args: any[]) => "default" | "primary") | (() => "default" | "primary"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    onClose?: (() => any) | undefined;
}, {
    mask: import("./types").TourMask;
    placement: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement) | ((new (...args: any[]) => "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end") | (() => import("@floating-ui/utils").Placement))[], "left" | "right" | "top" | "bottom" | "top-start" | "top-end" | "bottom-start" | "bottom-end" | "right-start" | "right-end" | "left-start" | "left-end", unknown>;
    scrollIntoViewOptions: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions) | ((new (...args: any[]) => boolean | ScrollIntoViewOptions) | (() => boolean | ScrollIntoViewOptions))[], unknown, unknown>;
    showArrow: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    showClose: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
