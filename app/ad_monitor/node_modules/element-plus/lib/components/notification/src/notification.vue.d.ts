declare function close(): void;
declare var __VLS_20: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_20) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly customClass: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly dangerouslyUseHTMLString: BooleanConstructor;
    readonly duration: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 4500, boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly id: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly message: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | (() => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | ((new (...args: any[]) => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | (() => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)))[], unknown, unknown, "", boolean>;
    readonly offset: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly onClick: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => () => void) | (() => () => void) | {
        (): () => void;
        new (): any;
        readonly prototype: any;
    } | ((new (...args: any[]) => () => void) | (() => () => void) | {
        (): () => void;
        new (): any;
        readonly prototype: any;
    })[], unknown, unknown, () => undefined, boolean>;
    readonly onClose: {
        readonly type: import("vue").PropType<() => void>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly position: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "top-left" | "top-right" | "bottom-left" | "bottom-right", unknown, "top-right", boolean>;
    readonly showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "error" | "primary" | "success" | "warning" | "info", unknown, "", boolean>;
    readonly zIndex: NumberConstructor;
    readonly closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, {
    visible: import("vue").Ref<boolean>;
    /** @description close notification */
    close: typeof close;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    destroy: () => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly customClass: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly dangerouslyUseHTMLString: BooleanConstructor;
    readonly duration: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 4500, boolean>;
    readonly icon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly id: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly message: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | (() => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | ((new (...args: any[]) => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | (() => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)))[], unknown, unknown, "", boolean>;
    readonly offset: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly onClick: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => () => void) | (() => () => void) | {
        (): () => void;
        new (): any;
        readonly prototype: any;
    } | ((new (...args: any[]) => () => void) | (() => () => void) | {
        (): () => void;
        new (): any;
        readonly prototype: any;
    })[], unknown, unknown, () => undefined, boolean>;
    readonly onClose: {
        readonly type: import("vue").PropType<() => void>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly position: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "top-left" | "top-right" | "bottom-left" | "bottom-right", unknown, "top-right", boolean>;
    readonly showClose: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly title: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly type: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "error" | "primary" | "success" | "warning" | "info", unknown, "", boolean>;
    readonly zIndex: NumberConstructor;
    readonly closeIcon: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component) | ((new (...args: any[]) => (string | import("vue").Component) & {}) | (() => string | import("vue").Component))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    onDestroy?: (() => any) | undefined;
}, {
    readonly title: string;
    readonly position: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "top-left" | "top-right" | "bottom-left" | "bottom-right", unknown>;
    readonly offset: number;
    readonly id: string;
    readonly type: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "error" | "primary" | "success" | "warning" | "info", unknown>;
    readonly message: import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | (() => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | ((new (...args: any[]) => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)) | (() => string | import("vue").VNode<import("vue").RendererNode, import("vue").RendererElement, {
        [key: string]: any;
    }> | (() => import("vue").VNode)))[], unknown, unknown>;
    readonly onClick: () => void;
    readonly showClose: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly duration: number;
    readonly customClass: string;
    readonly dangerouslyUseHTMLString: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
