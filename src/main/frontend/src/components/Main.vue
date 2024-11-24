<template>
  <div>
    <div v-if="error" class="error">{{ error }}</div>
    <Project v-for="project in projects" :key="project.id" :project="project" />
  </div>
</template>

<script>
import { useFetchProjects } from "@/composables/useFetchProjects";
import Project from "@/components/Project.vue";

export default {
  components: { Project },
  setup() {

    const { projects, taskTimer, fetchData, error } = useFetchProjects();


    fetchData();

    return { projects, taskTimer, error };
  },
};
</script>
