declare function clickHandler(): void;
declare var __VLS_1: {}, __VLS_3: {}, __VLS_14: {}, __VLS_17: {
    activeIndex: number;
    total: number;
}, __VLS_20: {
    actions: (action: import("element-plus/es/components/image-viewer").ImageViewerAction, options?: {}) => void;
    prev: () => void;
    next: () => void;
    reset: () => void;
    activeIndex: number;
    setActiveItem: (index: number) => void;
}, __VLS_23: {
    activeIndex: number;
    src: string;
};
type __VLS_Slots = {} & {
    error?: (props: typeof __VLS_1) => any;
} & {
    placeholder?: (props: typeof __VLS_3) => any;
} & {
    viewer?: (props: typeof __VLS_14) => any;
} & {
    progress?: (props: typeof __VLS_17) => any;
} & {
    toolbar?: (props: typeof __VLS_20) => any;
} & {
    'viewer-error'?: (props: typeof __VLS_23) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly hideOnClickModal: BooleanConstructor;
    readonly src: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly fit: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "fill" | "contain" | "none" | "cover" | "scale-down", unknown, "", boolean>;
    readonly loading: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "lazy" | "eager", unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly lazy: BooleanConstructor;
    readonly scrollContainer: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement | undefined) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement | undefined))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly previewSrcList: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string[]) | (() => string[]) | ((new (...args: any[]) => string[]) | (() => string[]))[], unknown, unknown, () => [], boolean>;
    readonly previewTeleported: BooleanConstructor;
    readonly zIndex: {
        readonly type: import("vue").PropType<number>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly initialIndex: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly infinite: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly closeOnPressEscape: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly zoomRate: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 1.2, boolean>;
    readonly scale: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 1, boolean>;
    readonly minScale: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0.2, boolean>;
    readonly maxScale: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 7, boolean>;
    readonly showProgress: BooleanConstructor;
    readonly crossorigin: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "" | "anonymous" | "use-credentials") | (() => "" | "anonymous" | "use-credentials") | ((new (...args: any[]) => "" | "anonymous" | "use-credentials") | (() => "" | "anonymous" | "use-credentials"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, {
    /** @description manually open preview */
    showPreview: typeof clickHandler;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    switch: (val: number) => void;
    show: () => void;
    close: () => void;
    error: (evt: Event) => void;
    load: (evt: Event) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly hideOnClickModal: BooleanConstructor;
    readonly src: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly fit: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "fill" | "contain" | "none" | "cover" | "scale-down", unknown, "", boolean>;
    readonly loading: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "lazy" | "eager", unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly lazy: BooleanConstructor;
    readonly scrollContainer: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement | undefined) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement | undefined))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly previewSrcList: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string[]) | (() => string[]) | ((new (...args: any[]) => string[]) | (() => string[]))[], unknown, unknown, () => [], boolean>;
    readonly previewTeleported: BooleanConstructor;
    readonly zIndex: {
        readonly type: import("vue").PropType<number>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly initialIndex: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly infinite: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly closeOnPressEscape: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly zoomRate: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 1.2, boolean>;
    readonly scale: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 1, boolean>;
    readonly minScale: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0.2, boolean>;
    readonly maxScale: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 7, boolean>;
    readonly showProgress: BooleanConstructor;
    readonly crossorigin: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "" | "anonymous" | "use-credentials") | (() => "" | "anonymous" | "use-credentials") | ((new (...args: any[]) => "" | "anonymous" | "use-credentials") | (() => "" | "anonymous" | "use-credentials"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    onShow?: (() => any) | undefined;
    onClose?: (() => any) | undefined;
    onError?: ((evt: Event) => any) | undefined;
    onLoad?: ((evt: Event) => any) | undefined;
    onSwitch?: ((val: number) => any) | undefined;
}, {
    readonly scale: number;
    readonly infinite: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly lazy: boolean;
    readonly src: string;
    readonly fit: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "fill" | "contain" | "none" | "cover" | "scale-down", unknown>;
    readonly closeOnPressEscape: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly initialIndex: number;
    readonly zoomRate: number;
    readonly minScale: number;
    readonly maxScale: number;
    readonly hideOnClickModal: boolean;
    readonly showProgress: boolean;
    readonly previewSrcList: string[];
    readonly previewTeleported: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
