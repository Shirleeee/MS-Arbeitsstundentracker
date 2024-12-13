
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

const handleUpdateTaskSuccess = (data) => {
  console.log('PROJECT MAIN handleUpdateTaskSuccess', data);
  emit('update-task-success', data);
};

const handleSubmitTaskSuccess = (data) => {
  // isModalOpen.value = false;
  emit('submit-task-success', data);
};
</script>
<template>
  <div>

    <Project @update-project-success="handleUpdateSuccess" @submit-task-success="handleSubmitTaskSuccess"  @update-task-success="handleUpdateTaskSuccess" @delete-success="handleDeleteSuccess" v-for="project in props.projects" :key="project.id.value" :project="project" :taskTimer="taskTimer"/>
  </div>
</template>

