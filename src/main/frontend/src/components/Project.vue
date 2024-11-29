
<script setup>
import { ref } from 'vue';
import {millisecondsToTimeFormat} from "@/utils/timeUtils";
import Task from "./Task.vue";
import Buttons from "@/components/Buttons.vue";

const props = defineProps({
  project: {
    type: Object,
    required: true,
    default: () => ({ tasks: [] })
  },
  taskTimer: Array
});

const text = ref('Task');

console.log("project.total");
console.log( props.project.total);


const handleNewData = (data) => {
  if (!data) {
    console.error('Received undefined data',data);
    return;
  }



  if (data.projectId) {
    props.project.tasks.push(data);
  }

};
</script>

<template>
  <div class="proj-wrapper">
    <div class="proj-head">
      <h2 class="title"> {{ project.name.projectName	 }}</h2>
      <p class="deadline">Deadline Project: {{ project.deadlineDate }}</p>
      <p class="total-time">Total: {{ millisecondsToTimeFormat(project.total) }}</p>
    </div>
    <Task v-for="task in project.tasks" :key="task.id.taskId" :task="task" />
  </div>
  <Buttons :text="text" :additionalData="project" @submit-success="handleNewData" ></Buttons>
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