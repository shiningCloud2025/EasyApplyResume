import type { Arrayable } from 'element-plus/es/utils';
import type { FormItemRule } from './types';
declare var __VLS_11: {
    label: string;
}, __VLS_13: {}, __VLS_20: {
    error: string;
};
type __VLS_Slots = {} & {
    label?: (props: typeof __VLS_11) => any;
} & {
    default?: (props: typeof __VLS_13) => any;
} & {
    error?: (props: typeof __VLS_20) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly label: StringConstructor;
    readonly labelWidth: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly labelPosition: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "left" | "right" | "top", unknown, "", boolean>;
    readonly prop: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | string[]) | (() => import("./form-item").FormItemProp) | ((new (...args: any[]) => string | string[]) | (() => import("./form-item").FormItemProp))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly required: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly rules: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => FormItemRule | FormItemRule[]) | (() => Arrayable<FormItemRule>) | ((new (...args: any[]) => FormItemRule | FormItemRule[]) | (() => Arrayable<FormItemRule>))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly error: StringConstructor;
    readonly validateStatus: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "error" | "success" | "validating", unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly for: StringConstructor;
    readonly inlineMessage: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly showMessage: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}, {
    /**
     * @description Form item size.
     */
    size: import("vue").ComputedRef<"" | "small" | "default" | "large">;
    /**
     * @description Validation message.
     */
    validateMessage: import("vue").Ref<string>;
    /**
     * @description Validation state.
     */
    validateState: import("vue").Ref<"" | "error" | "success" | "validating">;
    /**
     * @description Validate form item.
     */
    validate: (trigger: string, callback?: import("./types").FormValidateCallback) => import("./types").FormValidationResult;
    /**
     * @description Remove validation status of the field.
     */
    clearValidate: () => void;
    /**
     * @description Reset current field and remove validation result.
     */
    resetField: () => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly label: StringConstructor;
    readonly labelWidth: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly labelPosition: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "" | "left" | "right" | "top", unknown, "", boolean>;
    readonly prop: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | string[]) | (() => import("./form-item").FormItemProp) | ((new (...args: any[]) => string | string[]) | (() => import("./form-item").FormItemProp))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly required: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly rules: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => FormItemRule | FormItemRule[]) | (() => Arrayable<FormItemRule>) | ((new (...args: any[]) => FormItemRule | FormItemRule[]) | (() => Arrayable<FormItemRule>))[], unknown, unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly error: StringConstructor;
    readonly validateStatus: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "error" | "success" | "validating", unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly for: StringConstructor;
    readonly inlineMessage: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
    readonly showMessage: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, true, boolean>;
    readonly size: {
        readonly type: import("vue").PropType<import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "small" | "default" | "large", unknown>>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
}>>, {
    readonly required: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly labelPosition: import("element-plus/es/utils").EpPropMergeType<StringConstructor, "" | "left" | "right" | "top", unknown>;
    readonly inlineMessage: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
    readonly showMessage: import("element-plus/es/utils").EpPropMergeType<BooleanConstructor, unknown, unknown>;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
