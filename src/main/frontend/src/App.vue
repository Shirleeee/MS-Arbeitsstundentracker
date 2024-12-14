<script setup>


import {useFetchProjects} from "@/composables/useFetchProjects";
import Header from "@/components/Header.vue";
import Main from "@/components/Main.vue";
import {ref, computed} from "vue";
import {formatDate, formatTime, parseDurationToSeconds} from "@/utils/timeUtils.js";

const {projects, taskTimer, timeEntries, fetchData, error} = useFetchProjects();
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

const handleUpdateTaskSuccess = (data) => {
  console.log('PROJECT handleUpdateTaskSuccess', data);
  const projectIndex = projects.value.findIndex(project => project.id === data.projectId);
  if (projectIndex !== -1) {
    const taskIndex = projects.value[projectIndex].tasks.findIndex(task => task.task_id === data.task_id);
    if (taskIndex !== -1) {

      data.deadlineDate = formatDate(data.deadline.deadline);
      data.deadlineTime = formatTime(data.deadline.deadline);
      projects.value[projectIndex].tasks[taskIndex] = data;
    }
  }
};
const handleSubmitTaskSuccess = (data) => {
  console.log('PROJECT handleSubmitTaskSuccess', data);
  const projectIndex = projects.value.findIndex(project => project.id === data.projectId);
  if (projectIndex !== -1) {

    taskTimer.value.push({
      id: null,
      task_id: data.task_id,
      projectId: data.projectId,
      timer: null,
      isPlaying: false,
      trackedTime:  0,
    });
    data.timeEntries= taskTimer;
    data.deadlineDate = formatDate(data.deadline.deadline);
    data.deadlineTime = formatTime(data.deadline.deadline);
    projects.value[projectIndex].tasks.push(data);
  }
};

</script>

<template>

  <Header :projects="reversedProjects" @submit-success="updateProjects"/>
  <main>
    <div v-if="error" class="error">{{ error }}</div>
    <Main :projects="reversedProjects" @delete-success="handleDeleteSuccess"
          @submit-task-success="handleSubmitTaskSuccess" @update-task-success="handleUpdateTaskSuccess"
          @update-project-success="updateProjectData" :taskTimer="taskTimer" @delete-task-success="updateProjects"/>
  </main>
</template>

<style>


@media (min-width: 1024px) {


}
</style>
