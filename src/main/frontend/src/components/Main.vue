
<script setup>
import { computed } from 'vue';
import Project from "@/components/Project.vue";
const props = defineProps({
  projects: {
    type: Array

  }
});

</script>
<template>
  <div>

    <Project v-for="project in props.projects" :key="project.id.value" :project="project" />
  </div>
</template>

