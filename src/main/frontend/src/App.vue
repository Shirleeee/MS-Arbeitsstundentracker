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
</script>

<template>

  <Header :projects="reversedProjects"  />
  <main>
    <div v-if="error" class="error">{{ error }}</div>
    <Main @update-projects="updateProjects" :projects="reversedProjects" :taskTimer="taskTimer" />
  </main>
</template>

<style >



@media (min-width: 1024px) {




}
</style>
