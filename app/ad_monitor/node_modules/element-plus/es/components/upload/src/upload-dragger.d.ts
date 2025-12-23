import type { ExtractPropTypes, ExtractPublicPropTypes } from 'vue';
import type UploadDragger from './upload-dragger.vue';
export declare const uploadDraggerProps: {
    readonly disabled: import("element-plus/es/utils").EpPropFinalized<BooleanConstructor, unknown, unknown, undefined, boolean>;
};
export type UploadDraggerProps = ExtractPropTypes<typeof uploadDraggerProps>;
export type UploadDraggerPropsPublic = ExtractPublicPropTypes<typeof uploadDraggerProps>;
export declare const uploadDraggerEmits: {
    file: (file: File[]) => boolean;
};
export type UploadDraggerEmits = typeof uploadDraggerEmits;
export type UploadDraggerInstance = InstanceType<typeof UploadDragger> & unknown;
