import type { ScrollbarDirection } from './scrollbar';
import type { CSSProperties, StyleValue } from 'vue';
declare function scrollTo(xCord: number, yCord?: number): void;
declare function scrollTo(options: ScrollToOptions): void;
declare var __VLS_8: {};
type __VLS_Slots = {} & {
    default?: (props: typeof __VLS_8) => any;
};
declare const __VLS_base: import("vue").DefineComponent<{
    readonly ariaLabel: StringConstructor;
    readonly ariaOrientation: {
        readonly type: import("vue").PropType<string>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly distance: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly height: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, "", boolean>;
    readonly maxHeight: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, "", boolean>;
    readonly native: BooleanConstructor;
    readonly wrapStyle: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | false | CSSProperties | StyleValue[]) | (() => StyleValue) | ((new (...args: any[]) => string | false | CSSProperties | StyleValue[]) | (() => StyleValue))[], unknown, unknown, "", boolean>;
    readonly wrapClass: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, ArrayConstructor], unknown, unknown, "", boolean>;
    readonly viewClass: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, ArrayConstructor], unknown, unknown, "", boolean>;
    readonly viewStyle: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, ArrayConstructor, ObjectConstructor], unknown, unknown, "", boolean>;
    readonly noresize: BooleanConstructor;
    readonly tag: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "div", boolean>;
    readonly always: BooleanConstructor;
    readonly minSize: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 20, boolean>;
    readonly tabindex: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, undefined, boolean>;
    readonly id: StringConstructor;
    readonly role: StringConstructor;
}, {
    /** @description scrollbar wrap ref */
    wrapRef: import("vue").Ref<HTMLDivElement | undefined>;
    /** @description update scrollbar state manually */
    update: () => void;
    /** @description scrolls to a particular set of coordinates */
    scrollTo: typeof scrollTo;
    /** @description set distance to scroll top */
    setScrollTop: (value: number) => void;
    /** @description set distance to scroll left */
    setScrollLeft: (value: number) => void;
    /** @description handle scroll event */
    handleScroll: () => void;
}, unknown, {}, {}, import("vue").ComponentOptionsMixin, import("vue").ComponentOptionsMixin, {
    scroll: (args_0: {
        scrollTop: number;
        scrollLeft: number;
    }) => void;
    "end-reached": (direction: ScrollbarDirection) => void;
}, string, import("vue").PublicProps, Readonly<import("vue").ExtractPropTypes<{
    readonly ariaLabel: StringConstructor;
    readonly ariaOrientation: {
        readonly type: import("vue").PropType<string>;
        readonly required: false;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly distance: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly height: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, "", boolean>;
    readonly maxHeight: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, "", boolean>;
    readonly native: BooleanConstructor;
    readonly wrapStyle: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | false | CSSProperties | StyleValue[]) | (() => StyleValue) | ((new (...args: any[]) => string | false | CSSProperties | StyleValue[]) | (() => StyleValue))[], unknown, unknown, "", boolean>;
    readonly wrapClass: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, ArrayConstructor], unknown, unknown, "", boolean>;
    readonly viewClass: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, ArrayConstructor], unknown, unknown, "", boolean>;
    readonly viewStyle: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, ArrayConstructor, ObjectConstructor], unknown, unknown, "", boolean>;
    readonly noresize: BooleanConstructor;
    readonly tag: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "div", boolean>;
    readonly always: BooleanConstructor;
    readonly minSize: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 20, boolean>;
    readonly tabindex: import("element-plus/es/utils").EpPropFinalized<readonly [StringConstructor, NumberConstructor], unknown, unknown, undefined, boolean>;
    readonly id: StringConstructor;
    readonly role: StringConstructor;
}>> & {
    onScroll?: ((args_0: {
        scrollTop: number;
        scrollLeft: number;
    }) => any) | undefined;
    "onEnd-reached"?: ((direction: ScrollbarDirection) => any) | undefined;
}, {
    readonly tabindex: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>;
    readonly height: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>;
    readonly maxHeight: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, NumberConstructor], unknown, unknown>;
    readonly always: boolean;
    readonly tag: string;
    readonly minSize: number;
    readonly distance: number;
    readonly wrapStyle: StyleValue;
    readonly wrapClass: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, ArrayConstructor], unknown, unknown>;
    readonly viewClass: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, ArrayConstructor], unknown, unknown>;
    readonly viewStyle: import("element-plus/es/utils").EpPropMergeType<readonly [StringConstructor, ArrayConstructor, ObjectConstructor], unknown, unknown>;
    readonly native: boolean;
    readonly noresize: boolean;
}, {}>;
declare const __VLS_export: __VLS_WithSlots<typeof __VLS_base, __VLS_Slots>;
declare const _default: typeof __VLS_export;
export default _default;
type __VLS_WithSlots<T, S> = T & {
    new (): {
        $slots: S;
    };
};
