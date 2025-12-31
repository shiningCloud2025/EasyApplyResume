import type { ExtractPropTypes, ExtractPublicPropTypes } from 'vue';
export declare const breadcrumbItemProps: {
    readonly to: import("element-plus/es/utils").EpPropFinalized<(new (...args: any[]) => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | (() => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | ((new (...args: any[]) => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric) | (() => string | import("vue-router").RouteLocationAsRelativeGeneric | import("vue-router").RouteLocationAsPathGeneric))[], unknown, unknown, "", boolean>;
    readonly replace: BooleanConstructor;
};
export type BreadcrumbItemProps = ExtractPropTypes<typeof breadcrumbItemProps>;
export type BreadcrumbItemPropsPublic = ExtractPublicPropTypes<typeof breadcrumbItemProps>;
