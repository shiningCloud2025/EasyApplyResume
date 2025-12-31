import type { ExtractPropTypes, ExtractPublicPropTypes } from 'vue';
import type Affix from './affix.vue';
export declare const affixProps: {
    readonly zIndex: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | number) | (() => import("csstype").Property.ZIndex | undefined) | ((new (...args: any[]) => string | number) | (() => import("csstype").Property.ZIndex | undefined))[], unknown, unknown, 100, boolean>;
    readonly target: import("element-plus/es/utils").EpPropFinalized<StringConstructor, unknown, unknown, "", boolean>;
    readonly offset: import("element-plus/es/utils").EpPropFinalized<NumberConstructor, unknown, unknown, 0, boolean>;
    readonly position: import("element-plus/es/utils").EpPropFinalized<StringConstructor, "top" | "bottom", unknown, "top", boolean>;
    readonly teleported: BooleanConstructor;
    readonly appendTo: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>) | ((new (...args: any[]) => string | HTMLElement) | (() => import("element-plus/es/utils").EpPropMergeType<(new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement) | ((new (...args: any[]) => string | HTMLElement) | (() => string | HTMLElement))[], unknown, unknown>))[], unknown, unknown, "body", boolean>;
};
export type AffixProps = ExtractPropTypes<typeof affixProps>;
export type AffixPropsPublic = ExtractPublicPropTypes<typeof affixProps>;
export declare const affixEmits: {
    scroll: ({ scrollTop, fixed }: {
        scrollTop: number;
        fixed: boolean;
    }) => boolean;
    change: (fixed: boolean) => boolean;
};
export type AffixEmits = typeof affixEmits;
export type AffixInstance = InstanceType<typeof Affix> & unknown;
