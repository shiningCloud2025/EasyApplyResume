<template>
  <MdEditor
    v-model="model"
    :theme="theme"
    :preview-theme="previewTheme"
    :code-theme="codeTheme"
    :style="{ height }"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

interface Props {
  modelValue: string
  height?: string
  theme?: 'light' | 'dark'
  previewTheme?: string
  codeTheme?: string
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  height: '420px',
  theme: 'light',
  previewTheme: 'github',
  codeTheme: 'github'
})

const emit = defineEmits<{ (e: 'update:modelValue', v: string): void }>()

const model = computed({
  get: () => props.modelValue,
  set: (v: string) => emit('update:modelValue', v)
})
</script>
