<script setup>



import { useFetchProjects } from "@/composables/useFetchProjects";
import Header from "@/components/Header.vue";
import Main from "@/components/Main.vue";
import { ref,computed } from "vue";

const { projects,taskTimer, fetchData, error } = useFetchProjects();
fetchData();
const reversedProjects = computed(() => {
  return projects.value.slice().reverse();
});
const updateProjects = (newProject) => {

  if (!newProject.tasks) {
  newProject.tasks = [];
}
  projects.value.push(newProject);
};
const handleDeleteSuccess = (id) => {
  console.log('PROJECT handleDeleteSuccess ID', id);
  if (!id) {
    console.error('Received undefined data', id);
    return;
  }

  // Entferne das Projekt direkt aus props.projects
  const projectIndex = projects.value.findIndex(project => project.id === id);
  if (projectIndex !== -1) {
    projects.value.splice(projectIndex, 1); // Bearbeite direkt die Props-Liste
    console.log(`Projekt mit ID ${id} entfernt.`);
  } else {
    console.error(`Projekt mit ID ${id} nicht gefunden.`);
  }
};
const updateProjectData = (updatedProject) => {

  console.log("updatedProject", updatedProject);
  const index = projects.value.findIndex(project => project.id === updatedProject.id);
  if (index !== -1) {
    projects.value[index] = updatedProject;
  }
};
</script>

<template>

  <Header :projects="reversedProjects" @submit-success="updateProjects" />
  <main>
    <div v-if="error" class="error">{{ error }}</div>
    <Main :projects="reversedProjects" @delete-success="handleDeleteSuccess" @update-project-success="updateProjectData" :taskTimer="taskTimer" />
  </main>
</template>

<style >



@media (min-width: 1024px) {




}
</style>
