<script setup>
import {ref} from 'vue';
import {convertDurationToDHMS} from "@/utils/timeUtils";
import Task from "./Task.vue";
import Buttons from "@/components/Buttons.vue";
import {handleNewDateTime} from "@/composables/handleNewDate.js";
import PencilUpdate from "@/components/icons/PencilButtonSVG.vue";
import PlayButtonSVG from "@/components/icons/PlayButtonSVG.vue";

const props = defineProps({
  project: {
    type: Object,
    required: true,
    default: () => ({tasks: []})
  },
  taskTimer: Array
});

const text = ref('Task');
const handleNewData = (data) => {

  if (!data) {
    console.error('Received undefined data', data);
    return;
  }
  if (data.projectId) {
    data = handleNewDateTime(data);
    props.project.tasks.push(data);
    console.log("data", data);
    props.taskTimer.push({
      task_id: data.task_id, projectId: data.projectId, timer: null,
      isPlaying: false, trackedTime: 0
    });
  }
};

const currentProjectData = (name, description, deadline) => {
  return {
    name: name,
    description: description,
    deadline: deadline
  }
};

const handleUpdatedData = (data) => {
  if (!data) {
    console.error('Received undefined data', data);
    return;
  }
  const index = props.project.tasks.findIndex(task => task.task_id === data.task_id);
  if (index !== -1) {
    props.project.tasks[index] = data;
  }
};

</script>

<template>
  <div class="proj-wrapper">
    <div class="proj-head">

      <div class="title-container">
        <PencilUpdate :projectId="project.id" text="Project" @update-success="handleUpdatedData"
                      :currentProjectData="currentProjectData(project.name.projectName,project.description.projectDescription,project.deadline.deadline)"/>

        <h2 class="title"> {{ project.id }}-{{ project.name.projectName }}</h2>

      </div>


      <p class="deadline">Deadline Project: {{ project.deadlineDate }} {{ project.deadlineTime }} </p>
      <p class="total-time">Total: {{ convertDurationToDHMS(project.total) }}</p>
    </div>
    <Task v-for="task in project.tasks" :key="task.task_id" :task="task" :taskTimer="taskTimer"/>
  </div>
  <Buttons :text="text" :additionalData="project" @submit-success="handleNewData"></Buttons>
</template>


<style scoped>
.proj-wrapper {
  background-color: #31594a;
  padding: 40px 20px;
  border-radius: 10px;
  margin-bottom: 1rem;

  & > .proj-head {

    padding-bottom: 20px;
    width: 100%;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
  }
}

.title-container {
  display: flex;
  flex-direction: row;
  align-items: center;

  > * {
    padding-right: 5px;
  }
}
</style>