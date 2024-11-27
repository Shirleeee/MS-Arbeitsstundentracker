
<script setup>
import { computed } from 'vue';
import { useFetchProjects } from "@/composables/useFetchProjects";
import Project from "@/components/Project.vue";

const { projects, taskTimer, fetchData, error } = useFetchProjects();
fetchData();

const reversedProjects = computed(() => {
  return projects.value.slice().reverse();
});
</script>
<template>
  <div>
    <div v-if="error" class="error">{{ error }}</div>
    <Project v-for="project in reversedProjects" :key="project.id" :project="project"/>
  </div>
</template>

