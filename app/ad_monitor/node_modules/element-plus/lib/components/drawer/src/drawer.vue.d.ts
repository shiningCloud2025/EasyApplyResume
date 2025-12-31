declare var __VLS_36: {
    close: () => void;
    titleId: string;
    titleClass: string;
}, __VLS_38: {}, __VLS_50: {}, __VLS_52: {};
type __VLS_Slots = {} & {
    header?: (props: typeof __VLS_36) => any;
} & {
    title?: (props: typeof __VLS_38) => any;
} & {
    default?: (props: typeof __VLS_50) => any;
} & {
    footer?: (props: typeof __VLS_52) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly direction: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "ltr" | "rtl" | "ttb" | "btt", unknown, "rtl", boolean>;
    readonly resizable: BooleanConstructor;
    readonly size: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, "30%", boolean>;
    readonly withHeader: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly modalFade: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly headerAriaLevel: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "2", boolean>;
    readonly appendToBody: BooleanConstructor;
    readonly appendTo: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>) | ((new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>))[], unknown, unknown, "body", boolean>;
    readonly beforeClose: {
        readonly type: import("vue").PropType<import("element-plus/es/components/dialog").DialogBeforeCloseFn>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly destroyOnClose: BooleanConstructor;
    readonly closeOnClickModal: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly closeOnPressEscape: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly lockScroll: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly modal: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly modalPenetrable: BooleanConstructor;
    readonly openDelay: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly closeDelay: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly top: {
        readonly type: import("vue").PropType<string>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly modelValue: BooleanConstructor;
    readonly modalClass: StringConstructor;
    readonly headerClass: StringConstructor;
    readonly bodyClass: StringConstructor;
    readonly footerClass: StringConstructor;
    readonly width: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly zIndex: {
        readonly type: import("vue").PropType<number>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly trapFocus: BooleanConstructor;
    readonly transition: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | import("vue").TransitionProps) | (() => import("element-plus/es/components/dialog").DialogTransition) | ((new (...args: any[]) => string | import("vue").TransitionProps) | (() => import("element-plus/es/components/dialog").DialogTransition))[], unknown, unknown, undefined, boolean>;
    readonly center: BooleanConstructor;
    readonly alignCenter: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly draggable: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly overflow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly fullscreen: BooleanConstructor;
    readonly showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly ariaLevel: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "2", boolean>;
}, {
    handleClose: () => void;
    afterEnter: () => void;
    afterLeave: () => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    "update:modelValue": (value: boolean) => void;
    resize: (evt: MouseEvent, size: number) => void;
    open: () => void;
    close: () => void;
    opened: () => void;
    closed: () => void;
    openAutoFocus: () => void;
    closeAutoFocus: () => void;
    "resize-start": (evt: MouseEvent, size: number) => void;
    "resize-end": (evt: MouseEvent, size: number) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly direction: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "ltr" | "rtl" | "ttb" | "btt", unknown, "rtl", boolean>;
    readonly resizable: BooleanConstructor;
    readonly size: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, "30%", boolean>;
    readonly withHeader: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly modalFade: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly headerAriaLevel: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "2", boolean>;
    readonly appendToBody: BooleanConstructor;
    readonly appendTo: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>) | ((new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>))[], unknown, unknown, "body", boolean>;
    readonly beforeClose: {
        readonly type: import("vue").PropType<import("element-plus/es/components/dialog").DialogBeforeCloseFn>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly destroyOnClose: BooleanConstructor;
    readonly closeOnClickModal: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly closeOnPressEscape: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly lockScroll: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly modal: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly modalPenetrable: BooleanConstructor;
    readonly openDelay: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly closeDelay: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly top: {
        readonly type: import("vue").PropType<string>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly modelValue: BooleanConstructor;
    readonly modalClass: StringConstructor;
    readonly headerClass: StringConstructor;
    readonly bodyClass: StringConstructor;
    readonly footerClass: StringConstructor;
    readonly width: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly zIndex: {
        readonly type: import("vue").PropType<number>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly trapFocus: BooleanConstructor;
    readonly transition: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | import("vue").TransitionProps) | (() => import("element-plus/es/components/dialog").DialogTransition) | ((new (...args: any[]) => string | import("vue").TransitionProps) | (() => import("element-plus/es/components/dialog").DialogTransition))[], unknown, unknown, undefined, boolean>;
    readonly center: BooleanConstructor;
    readonly alignCenter: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly draggable: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly overflow: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly fullscreen: BooleanConstructor;
    readonly showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly ariaLevel: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "2", boolean>;
}>> & {
    "onUpdate:modelValue"?: ((value: boolean) => any) | undefined;
    onResize?: ((evt: MouseEvent, size: number) => any) | undefined;
    onClose?: (() => any) | undefined;
    onOpen?: (() => any) | undefined;
    onOpened?: (() => any) | undefined;
    onClosed?: (() => any) | undefined;
    onOpenAutoFocus?: (() => any) | undefined;
    onCloseAutoFocus?: (() => any) | undefined;
    "onResize-start"?: ((evt: MouseEvent, size: number) => any) | undefined;
    "onResize-end"?: ((evt: MouseEvent, size: number) => any) | undefined;
}, {
    readonly title: string;
    readonly center: boolean;
    readonly direction: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "ltr" | "rtl" | "ttb" | "btt", unknown>;
    readonly overflow: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly transition: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | import("vue").TransitionProps) | (() => import("element-plus/es/components/dialog").DialogTransition) | ((new (...args: any[]) => string | import("vue").TransitionProps) | (() => import("element-plus/es/components/dialog").DialogTransition))[], unknown, unknown>;
    readonly size: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>;
    readonly modelValue: boolean;
    readonly ariaLevel: string;
    readonly appendTo: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>) | ((new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>))[], unknown, unknown>;
    readonly alignCenter: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly draggable: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly showClose: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly fullscreen: boolean;
    readonly closeOnClickModal: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly closeOnPressEscape: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly lockScroll: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly modal: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly openDelay: number;
    readonly closeDelay: number;
    readonly headerAriaLevel: string;
    readonly appendToBody: boolean;
    readonly destroyOnClose: boolean;
    readonly modalPenetrable: boolean;
    readonly trapFocus: boolean;
    readonly withHeader: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly modalFade: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly resizable: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
