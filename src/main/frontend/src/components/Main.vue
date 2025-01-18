<script setup>
import Project from "@/components/Project.vue";

const props = defineProps({
  projects: {
    type: Array
  },
  taskTimer: {
    type: Array
  },
  user: {
    type: Object,

  }
});
const emit = defineEmits(['submit-success', 'delete-success']);

const handleDeleteSuccess = (id) => {
  emit('delete-success', id);

};

const handleUpdateSuccess = (data) => {
  emit('update-project-success', data);
};

const handleUpdateTaskSuccess = (data) => {
  emit('update-task-success', data);
};

const handleSubmitTaskSuccess = (data) => {
  emit('submit-task-success', data);
};
const handleDeleteTaskSuccess = (task) => {
  emit('delete-task-success', task);
};
</script>
<template>
  <div>

    <Project :user=user @delete-task-success="handleDeleteTaskSuccess" @update-project-success="handleUpdateSuccess"
             @submit-task-success="handleSubmitTaskSuccess" @update-task-success="handleUpdateTaskSuccess"
             @delete-success="handleDeleteSuccess" v-for="project in props.projects" :key="project.id.value"
             :project="project" :taskTimer="taskTimer"/>
  </div>
</template>

