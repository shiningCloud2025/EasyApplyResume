<template>
  <QuestionFirstCategoryManager v-if="currentKey === 'first-category'" />
  <QuestionSecondCategoryManager v-else-if="currentKey === 'second-category'" />
  <QuestionBankManager v-else-if="currentKey === 'question-bank'" />
  <UserAnswerManager v-else-if="currentKey === 'user-answer'" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import QuestionFirstCategoryManager from './components/QuestionFirstCategoryManager.vue'
import QuestionSecondCategoryManager from './components/QuestionSecondCategoryManager.vue'
import QuestionBankManager from './components/QuestionBankManager.vue'
import UserAnswerManager from './components/UserAnswerManager.vue'

const route = useRoute()

const currentKey = computed(() => route.path.split('/').pop() || 'first-category')
</script>
