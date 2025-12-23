import type { MentionOption } from './types';
declare var __VLS_1: {}, __VLS_10: {
    item: MentionOption;
    index: number;
}, __VLS_12: {}, __VLS_14: {};
type __VLS_Slots = {} & {
    header?: (props: typeof __VLS_1) => any;
} & {
    label?: (props: typeof __VLS_10) => any;
} & {
    loading?: (props: typeof __VLS_12) => any;
} & {
    footer?: (props: typeof __VLS_14) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    options: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => MentionOption[]) | (() => MentionOption[]) | ((new (...args: any[]) => MentionOption[]) | (() => MentionOption[]))[], unknown, unknown, () => never[], boolean>;
    loading: BooleanConstructor;
    disabled: BooleanConstructor;
    contentId: StringConstructor;
    ariaLabel: StringConstructor;
}, {
    hoveringIndex: import("vue").Ref<number>;
    navigateOptions: (direction: "next" | "prev") => void;
    selectHoverOption: () => void;
    hoverOption: import("vue").ComputedRef<MentionOption>;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    select: (option: MentionOption) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    options: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => MentionOption[]) | (() => MentionOption[]) | ((new (...args: any[]) => MentionOption[]) | (() => MentionOption[]))[], unknown, unknown, () => never[], boolean>;
    loading: BooleanConstructor;
    disabled: BooleanConstructor;
    contentId: StringConstructor;
    ariaLabel: StringConstructor;
}>> & {
    onSelect?: ((option: MentionOption) => any) | undefined;
}, {
    disabled: boolean;
    loading: boolean;
    options: MentionOption[];
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
