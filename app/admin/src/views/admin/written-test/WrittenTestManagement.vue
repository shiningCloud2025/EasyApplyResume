<template>
  <QuestionFirstCategoryManager v-if="currentKey === 'first-category'" />
  <QuestionSecondCategoryManager v-else-if="currentKey === 'second-category'" />
  <QuestionBankManager v-else-if="currentKey === 'question-bank'" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import QuestionFirstCategoryManager from './components/QuestionFirstCategoryManager.vue'
import QuestionSecondCategoryManager from './components/QuestionSecondCategoryManager.vue'
import QuestionBankManager from './components/QuestionBankManager.vue'

const route = useRoute()

const currentKey = computed(() => route.path.split('/').pop() || 'first-category')
</script>
