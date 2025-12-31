declare var __VLS_16: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_16) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly disabled: BooleanConstructor;
    readonly trigger: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "click" | "focus" | "contextmenu" | "hover" | import("./trigger").TooltipTriggerType[]) | (() => import("element-plus/es/utils").Arrayable<import("./trigger").TooltipTriggerType>) | ((new (...args: any[]) => "click" | "focus" | "contextmenu" | "hover" | import("./trigger").TooltipTriggerType[]) | (() => import("element-plus/es/utils").Arrayable<import("./trigger").TooltipTriggerType>))[], unknown, unknown, "hover", boolean>;
    readonly triggerKeys: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string[]) | (() => string[]) | ((new (...args: any[]) => string[]) | (() => string[]))[], unknown, unknown, () => string[], boolean>;
    readonly focusOnTarget: BooleanConstructor;
    readonly virtualRef: {
        readonly type: import("vue").PropType<import("element-plus/es/components/popper").Measurable>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly virtualTriggering: BooleanConstructor;
    readonly onMouseenter: {
        readonly type: import("vue").PropType<(e: MouseEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onMouseleave: {
        readonly type: import("vue").PropType<(e: MouseEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onClick: {
        readonly type: import("vue").PropType<(e: PointerEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onKeydown: {
        readonly type: import("vue").PropType<(e: KeyboardEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onFocus: {
        readonly type: import("vue").PropType<(e: FocusEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onBlur: {
        readonly type: import("vue").PropType<(e: FocusEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onContextmenu: {
        readonly type: import("vue").PropType<(e: PointerEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly id: StringConstructor;
    readonly open: BooleanConstructor;
}, {
    /**
     * @description trigger element
     */
    triggerRef: import("vue").Ref<{
        forwardRef: HTMLElement;
    } | null>;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly disabled: BooleanConstructor;
    readonly trigger: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => "click" | "focus" | "contextmenu" | "hover" | import("./trigger").TooltipTriggerType[]) | (() => import("element-plus/es/utils").Arrayable<import("./trigger").TooltipTriggerType>) | ((new (...args: any[]) => "click" | "focus" | "contextmenu" | "hover" | import("./trigger").TooltipTriggerType[]) | (() => import("element-plus/es/utils").Arrayable<import("./trigger").TooltipTriggerType>))[], unknown, unknown, "hover", boolean>;
    readonly triggerKeys: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string[]) | (() => string[]) | ((new (...args: any[]) => string[]) | (() => string[]))[], unknown, unknown, () => string[], boolean>;
    readonly focusOnTarget: BooleanConstructor;
    readonly virtualRef: {
        readonly type: import("vue").PropType<import("element-plus/es/components/popper").Measurable>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly virtualTriggering: BooleanConstructor;
    readonly onMouseenter: {
        readonly type: import("vue").PropType<(e: MouseEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onMouseleave: {
        readonly type: import("vue").PropType<(e: MouseEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onClick: {
        readonly type: import("vue").PropType<(e: PointerEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onKeydown: {
        readonly type: import("vue").PropType<(e: KeyboardEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onFocus: {
        readonly type: import("vue").PropType<(e: FocusEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onBlur: {
        readonly type: import("vue").PropType<(e: FocusEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly onContextmenu: {
        readonly type: import("vue").PropType<(e: PointerEvent) => void>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly id: StringConstructor;
    readonly open: BooleanConstructor;
}>>, {
    readonly disabled: boolean;
    readonly open: boolean;
    readonly trigger: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "click" | "focus" | "contextmenu" | "hover" | import("./trigger").TooltipTriggerType[]) | (() => import("element-plus/es/utils").Arrayable<import("./trigger").TooltipTriggerType>) | ((new (...args: any[]) => "click" | "focus" | "contextmenu" | "hover" | import("./trigger").TooltipTriggerType[]) | (() => import("element-plus/es/utils").Arrayable<import("./trigger").TooltipTriggerType>))[], unknown, unknown>;
    readonly virtualTriggering: boolean;
    readonly triggerKeys: string[];
    readonly focusOnTarget: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
