<template>
  <div class="rich-text-editor">
    <div class="editor-shell" :style="{ height }">
      <Toolbar
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        mode="default"
        class="toolbar"
      />
      <Editor
        v-model="model"
        :defaultConfig="editorConfig"
        mode="default"
        class="editor"
        @onCreated="handleEditorCreated"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, shallowRef, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import '@wangeditor/editor/dist/css/style.css'

interface Props {
  modelValue: string
  height?: string
  placeholder?: string
  readonly?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  height: '420px',
  placeholder: '请输入内容，支持富文本格式...',
  readonly: false
})

const emit = defineEmits<{
  (e: 'update:modelValue', v: string): void
  (e: 'html-change', html: string): void
}>()

const editorRef = shallowRef<any>()

const editorConfig: Partial<IEditorConfig> = {
  placeholder: props.placeholder,
  MENU_CONF: {}
}

const toolbarConfig: Partial<IToolbarConfig> = {
  toolbarKeys: [
    'headerSelect',
    'bold',
    'italic',
    'underline',
    'color',
    'bgColor',
    '|',
    'fontSize',
    'fontFamily',
    '|',
    'bulletedList',
    'numberedList',
    '|',
    'justifyLeft',
    'justifyCenter',
    'justifyRight',
    '|',
    'emotion',
    'insertLink',
    'insertImage',
    '|',
    'undo',
    'redo'
  ]
}

const model = computed({
  get: () => props.modelValue,
  set: (v: string) => {
    emit('update:modelValue', v)
    emit('html-change', v)
  }
})

const syncReadonlyState = (readonly: boolean) => {
  const editor = editorRef.value
  if (!editor) {
    return
  }

  if (readonly) {
    editor.disable?.()
    return
  }

  editor.enable?.()
}

const handleEditorCreated = (editor: any) => {
  editorRef.value = editor
  syncReadonlyState(props.readonly)
}

watch(() => props.readonly, (readonly) => {
  syncReadonlyState(readonly)
})

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})
</script>

<style scoped lang="scss">
.rich-text-editor {
  width: 100%;
}

.editor-shell {
  display: flex;
  flex-direction: column;
  height: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  background: #fff;
  overflow: hidden;
}

.toolbar {
  border-bottom: 1px solid #dcdfe6;
  flex-shrink: 0;
}

.editor {
  flex: 1;
  min-height: 0;
  overflow-y: hidden;
}

:deep(.w-e-text-container) {
  background-color: #fff;
}

:deep(.w-e-toolbar) {
  background-color: #f8f9fa !important;
}

:deep(.w-e-text-placeholder) {
  font-style: normal;
  color: #c0c4cc;
}
</style>
