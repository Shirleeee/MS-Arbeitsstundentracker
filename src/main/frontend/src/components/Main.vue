
<script setup>
import Project from "@/components/Project.vue";
const props = defineProps({
  projects: {
    type: Array
  },
  taskTimer: {
    type: Array
  }

});
const emit = defineEmits(['submit-success', 'delete-success']);

const handleDeleteSuccess = (id) => {
  emit('delete-success', id);

};

const handleUpdateSuccess = (data) => {
  console.log('PROJECT MAIN handleUpdateSuccess', data);
  emit('update-project-success', data);
};


</script>
<template>
  <div>

    <Project @submit-success="updateProjectData" @update-project-success="handleUpdateSuccess" @delete-success="handleDeleteSuccess" v-for="project in props.projects" :key="project.id.value" :project="project" :taskTimer="taskTimer"/>
  </div>
</template>

