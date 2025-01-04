<script setup>


import {useFetchProjects} from "@/composables/useFetchProjects";
import Header from "@/components/Header.vue";
import Main from "@/components/Main.vue";
import {ref, computed} from "vue";
import {formatDate, formatTime} from "@/utils/timeUtils.js";
// logged in user
const user = ref({
  id: 101,
  name: 'Mechthilda',
  email: 'ssss@wwww.de',
});

const {projects, taskTimer, fetchData, error} = useFetchProjects(user.value.id);
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
const handleDeleteProjectSuccess = (id) => {
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
const handleDeleteTaskSuccess = (task) => {
  const projectIndex = projects.value.findIndex(project => project.id === task.projectId);

  if (!task) {
    console.error('Received undefined data', task);
    return;
  }

  if (projectIndex !== -1) {
    const taskIndex = projects.value[projectIndex].tasks.findIndex(projectTask => projectTask.task_id === task.task_id);
    if (taskIndex !== -1) {
      projects.value[projectIndex].tasks.splice(taskIndex, 1);
      console.log(`Projekt mit ID ${task} entfernt.`);
    } else {
      console.error(`Projekt mit ID ${task} nicht gefunden.`);
    }
  }
};
const updateProjectData = (updatedProjectData) => {

  const index = projects.value.findIndex(project => project.id === updatedProjectData.id);
  if (index !== -1) {

    projects.value[index].deadline.deadline = updatedProjectData.deadline.deadline;
    projects.value[index].description.projectDescription = updatedProjectData.description.projectDescription;
    projects.value[index].name.projectName = updatedProjectData.name.projectName;

    projects.value[index].deadlineDate = formatDate(updatedProjectData.deadline.deadline);
    projects.value[index].deadlineTime = formatTime(updatedProjectData.deadline.deadline);


  }
};

const handleUpdateTaskSuccess = (data) => {
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
  const projectIndex = projects.value.findIndex(project => project.id === data.projectId);
  if (projectIndex !== -1) {

    taskTimer.value.push({
      id: null,
      task_id: data.task_id,
      projectId: data.projectId,
      timer: null,
      isPlaying: false,
      trackedTime: 0,
    });
    data.timeEntries = taskTimer;
    data.deadlineDate = formatDate(data.deadline.deadline);
    data.deadlineTime = formatTime(data.deadline.deadline);
    projects.value[projectIndex].tasks.push(data);
  }
};

</script>

<template>

  <Header :user=user :projects="reversedProjects" @submit-success="updateProjects"/>
  <main>
    <div v-if="error" class="error">{{ error }}</div>
    <Main :user=user :projects="reversedProjects" @delete-success="handleDeleteProjectSuccess"
          @submit-task-success="handleSubmitTaskSuccess" @update-task-success="handleUpdateTaskSuccess"
          @update-project-success="updateProjectData" :taskTimer="taskTimer"
          @delete-task-success="handleDeleteTaskSuccess"/>
  </main>
</template>

<style>


@media (min-width: 1024px) {


}
</style>
