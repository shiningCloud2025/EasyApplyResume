import type { UploadFile } from './upload';
declare var __VLS_7: {
    file: UploadFile;
    index: number;
}, __VLS_77: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_7) => any;
} & {
    append?: (props: typeof __VLS_77) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly files: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => import("./upload").UploadFiles) | (() => import("./upload").UploadFiles) | ((new (...args: any[]) => import("./upload").UploadFiles) | (() => import("./upload").UploadFiles))[], unknown, unknown, () => never[], boolean>;
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly handlePreview: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => (uploadFile: UploadFile) => void) | (() => (uploadFile: UploadFile) => void) | {
        (): (uploadFile: UploadFile) => void;
        new (): any;
        readonly prototype: any;
    } | ((new (...args: any[]) => (uploadFile: UploadFile) => void) | (() => (uploadFile: UploadFile) => void) | {
        (): (uploadFile: UploadFile) => void;
        new (): any;
        readonly prototype: any;
    })[], unknown, unknown, () => void, boolean>;
    readonly listType: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "picture" | "text" | "picture-card", unknown, "text", boolean>;
    readonly crossorigin: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "" | "anonymous" | "use-credentials") | (() => "" | "anonymous" | "use-credentials") | ((new (...args: any[]) => "" | "anonymous" | "use-credentials") | (() => "" | "anonymous" | "use-credentials"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, unknown, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    remove: (file: UploadFile) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly files: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => import("./upload").UploadFiles) | (() => import("./upload").UploadFiles) | ((new (...args: any[]) => import("./upload").UploadFiles) | (() => import("./upload").UploadFiles))[], unknown, unknown, () => never[], boolean>;
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly handlePreview: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => (uploadFile: UploadFile) => void) | (() => (uploadFile: UploadFile) => void) | {
        (): (uploadFile: UploadFile) => void;
        new (): any;
        readonly prototype: any;
    } | ((new (...args: any[]) => (uploadFile: UploadFile) => void) | (() => (uploadFile: UploadFile) => void) | {
        (): (uploadFile: UploadFile) => void;
        new (): any;
        readonly prototype: any;
    })[], unknown, unknown, () => void, boolean>;
    readonly listType: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "picture" | "text" | "picture-card", unknown, "text", boolean>;
    readonly crossorigin: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => "" | "anonymous" | "use-credentials") | (() => "" | "anonymous" | "use-credentials") | ((new (...args: any[]) => "" | "anonymous" | "use-credentials") | (() => "" | "anonymous" | "use-credentials"))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>> & {
    onRemove?: ((file: UploadFile) => any) | undefined;
}, {
    readonly disabled: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly listType: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "picture" | "text" | "picture-card", unknown>;
    readonly files: import("./upload").UploadFiles;
    readonly handlePreview: (uploadFile: UploadFile) => void;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
