import * as React from 'react';
import type { TinyMCE, Editor as TinyMCEEditor } from 'tinymce';
import { IEvents } from '../Events';
import { ScriptItem } from '../ScriptLoader2';
import { IEditorPropTypes } from './EditorPropTypes';
type OmitStringIndexSignature<T> = {
    [K in keyof T as string extends K ? never : K]: T[K];
};
interface DoNotUse<T extends string> {
    __brand: T;
}
type OmittedInitProps = 'selector' | 'target' | 'readonly' | 'disabled' | 'license_key';
type EditorOptions = Parameters<TinyMCE['init']>[0];
export type InitOptions = Omit<OmitStringIndexSignature<EditorOptions>, OmittedInitProps> & {
    selector?: DoNotUse<'selector prop is handled internally by the component'>;
    target?: DoNotUse<'target prop is handled internally by the component'>;
    readonly?: DoNotUse<'readonly prop is overridden by the component'>;
    disabled?: DoNotUse<'disabled prop is overridden by the component'>;
    license_key?: DoNotUse<'license_key prop is overridden by the integration, use the `licenseKey` prop instead'>;
} & {
    [key: string]: unknown;
};
export type Version = `${'4' | '5' | '6' | '7' | '8'}${'' | '-dev' | '-testing' | `.${number}` | `.${number}.${number}`}`;
export interface IProps {
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#apikey React Tech Ref - apiKey}
     * @description TinyMCE API key for deployments using Tiny Cloud.
     */
    apiKey: string;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#id React Tech Ref - id}
     * @description The ID of the element to render the editor into.
     */
    id: string;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#inline React Tech Ref - inline}
     * @description Whether the editor should be rendered inline. Equivalent to the `inline` option in TinyMCE.
     */
    inline: boolean;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#initialvalue React Tech Ref - initialValue}
     * @description The initial HTML content of the editor.
     *
     * IMPORTANT: Ensure that this is **not** updated by `onEditorChange` or the editor will be unusable.
     */
    initialValue: string;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#oneditorchange React Tech Ref - onEditorChange}
     * @description Used to store the state of the editor outside the component. Typically used for controlled components.
     * @param a The current HTML content of the editor.
     * @param editor The TinyMCE editor instance.
     * @returns void
     */
    onEditorChange: (a: string, editor: TinyMCEEditor) => void;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#value React Tech Ref - value}
     * @description The current HTML content of the editor. Typically used for controlled components.
     */
    value: string;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#init React Tech Ref - init}
     * @description Additional settings passed to `tinymce.init()` when initializing the editor.
     */
    init: InitOptions;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#tagname React Tech Ref - tagName}
     * @description The tag name of the element to render the editor into. Only valid when `inline` is `true`.
     */
    tagName: string;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#tabIndex React Tech Ref - tabIndex}
     * @description The tab index of the element that the editor wraps.
     */
    tabIndex: number;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#cloudchannel React Tech Ref - cloudChannel}
     * @description The TinyMCE build to use when loading from Tiny Cloud.
     */
    cloudChannel: Version;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#plugins React Tech Ref - plugins}
     * @description The plugins to load into the editor. Equivalent to the `plugins` option in TinyMCE.
     */
    plugins: NonNullable<EditorOptions['plugins']>;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#toolbar React Tech Ref - toolbar}
     * @description The toolbar to load into the editor. Equivalent to the `toolbar` option in TinyMCE.
     */
    toolbar: NonNullable<EditorOptions['toolbar']>;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#disabled React Tech Ref - disabled}
     * @description Whether the editor should be disabled.
     */
    disabled: boolean;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#readonly React Tech Ref - readonly}
     * @description Whether the editor should be readonly.
     */
    readonly: boolean;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#textareaname React Tech Ref - textareaName}
     * @description Set the `name` attribute of the `textarea` element used for the editor in forms. Only valid in iframe mode.
     */
    textareaName: string;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#tinymcescriptsrc React Tech Ref - tinymceScriptSrc}
     * @description The URL of the TinyMCE script to lazy load.
     */
    tinymceScriptSrc: string | string[] | ScriptItem[];
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#rollback React Tech Ref - rollback}
     * @description The number of milliseconds to wait before reverting to the previous value when the editor's content changes.
     */
    rollback: number | false;
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#scriptloading React Tech Ref - scriptLoading}
     * @description Options for how the TinyMCE script should be loaded.
     * @property async Whether the script should be loaded with the `async` attribute.
     * @property defer Whether the script should be loaded with the `defer` attribute.
     * @property delay The number of milliseconds to wait before loading the script.
     */
    scriptLoading: {
        async?: boolean;
        defer?: boolean;
        delay?: number;
    };
    /**
     * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/#licenseKey React Tech Ref - licenseKey}
     * @description Tiny Cloud License Key for when self-hosting TinyMCE.
     */
    licenseKey: string;
}
export interface IAllProps extends Partial<IProps>, Partial<IEvents> {
}
/**
 * @see {@link https://www.tiny.cloud/docs/tinymce/7/react-ref/ TinyMCE React Technical Reference}
 */
export declare class Editor extends React.Component<IAllProps> {
    static propTypes: IEditorPropTypes;
    static defaultProps: Partial<IAllProps>;
    editor?: TinyMCEEditor;
    private id;
    private elementRef;
    private inline;
    private currentContent?;
    private boundHandlers;
    private rollbackTimer;
    private valueCursor;
    constructor(props: Partial<IAllProps>);
    private get view();
    componentDidUpdate(prevProps: Partial<IAllProps>): void;
    componentDidMount(): void;
    componentWillUnmount(): void;
    render(): React.ReactElement<{
        ref: React.RefObject<HTMLElement | null>;
        id: string;
        tabIndex: number | undefined;
    }, string | React.JSXElementConstructor<any>>;
    private beforeInputEvent;
    private renderInline;
    private renderIframe;
    private getScriptSources;
    private getInitialValue;
    private bindHandlers;
    private rollbackChange;
    private handleBeforeInput;
    private handleBeforeInputSpecial;
    private handleEditorChange;
    private handleEditorChangeSpecial;
    private initialise;
}
export {};
