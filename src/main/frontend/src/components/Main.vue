
<script setup>
import { computed } from 'vue';
import Project from "@/components/Project.vue";
const props = defineProps({
  projects: {
    type: Array

  }
});
console.log(props.projects);

</script>
<template>
  <div>

    <Project v-for="project in props.projects" :key="project.id" :project="project" />
  </div>
</template>

