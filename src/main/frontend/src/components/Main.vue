
<script setup>
import { computed,ref } from 'vue';
import Project from "@/components/Project.vue";
const props = defineProps({
  projects: {
    type: Array
  },
  taskTimer: {
    type: Array
  }

});
// const currentDateTimeLocal = new Date().toISOString().slice(0, 16);
const projects = ref([]);

const updateProject = (updatedProject) => {
  // Finde das Projekt in der Liste und aktualisiere es
  const index = projects.value.findIndex(project => project.id === updatedProject.id);
  if (index !== -1) {
    projects.value[index] = updatedProject;
  }
};
</script>
<template>
  <div>

    <Project  v-for="project in props.projects" :key="project.id.value" :project="project" :taskTimer="taskTimer"/>
  </div>
</template>

