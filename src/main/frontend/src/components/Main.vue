
<script setup>
import { computed } from 'vue';
import Project from "@/components/Project.vue";
const props = defineProps({
  projects: {
    type: Array
  },
  taskTimer: {
    type: Array
  }

});
const currentDateTimeLocal = new Date().toISOString().slice(0, 16);

</script>
<template>
  <div>

    <Project v-for="project in props.projects" :key="project.id.value" :project="project" :taskTimer="taskTimer"/>
  </div>
</template>

