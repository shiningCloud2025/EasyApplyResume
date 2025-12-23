import type { ExtractPropTypes, ExtractPublicPropTypes } from 'vue';
import type Color from '../utils/color';
export declare const svPanelProps: {
    readonly color: {
        readonly type: import("vue").PropType<Color>;
        readonly required: true;
        readonly validator: ((val: unknown) => boolean) | undefined;
        __epPropKey: true;
    };
    readonly disabled: BooleanConstructor;
};
export type SvPanelProps = ExtractPropTypes<typeof svPanelProps>;
export type SvPanelPropsPublic = ExtractPublicPropTypes<typeof svPanelProps>;
