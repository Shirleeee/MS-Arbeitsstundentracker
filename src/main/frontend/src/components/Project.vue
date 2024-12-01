<script setup>
import {ref} from 'vue';
import {convertDurationToDHMS} from "@/utils/timeUtils";
import Task from "./Task.vue";
import Buttons from "@/components/Buttons.vue";
import {handleNewDateTime} from "@/composables/handleNewDate.js";

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
  }
};
</script>

<template>
  <div class="proj-wrapper">
    <div class="proj-head">
      <h2 class="title"> {{ project.name.projectName }}</h2>
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
    width: 100%;
    display: flex;
    padding-bottom: 20px;
    flex-direction: row;
    justify-content: space-between;
  }
}
</style>