import type { ExtractPropTypes, ExtractPublicPropTypes } from 'vue';
export declare const anchorLinkProps: {
    title: StringConstructor;
    href: StringConstructor;
};
export type AnchorLinkProps = ExtractPropTypes<typeof anchorLinkProps>;
export type AnchorLinkPropsPublic = ExtractPublicPropTypes<typeof anchorLinkProps>;
