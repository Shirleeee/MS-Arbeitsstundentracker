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
console.log("props.project", props.project);
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
      task_id: data.task_id,
      projectId: data.projectId,
      timer: null,
      isPlaying: false,
      trackedTime: 0
    });
  }
};

const currentData = (id,name, description, deadline) => {
  return {
    id:id,
    name: name,
    description: description,
    deadline: deadline
  }
};

// const handleUpdateSuccess = (data) => {
//   if (!data) {
//     console.error('Received undefined data', data);
//     return;
//   }
//   console.log("data", data);
//   console.log("props.project", props.project);
  // const index = props.project.findIndex(project => project.id === updatedProject.id);
  // console.log("index", index);
  // if (index !== -1) {
  //   projects.value[index] = updatedProject;
  // }
  // title.value = data.projectName;
  // description.value = data.projectDescription;
  // deadline.value = data.deadline;
// };
// const isModalOpen = ref(false);


// const emit = defineEmits(['submit-success']);
// const handleSubmitSuccess = (data) => {
//   isModalOpen.value = false;
//   emit('submit-success', data);
// };
// const handleClose = () => {
//   isModalOpen.value = false;
// };
</script>

<template>
  <div class="proj-wrapper">
    <div class="proj-head">

      <div class="title-container">
        <PencilUpdate text="Project"
                      :currentData="currentData(project.id,project.name.projectName,project.description.projectDescription,project.deadline.deadline)"/>

        <h2 class="title"> {{ project.id }}-{{ project.name.projectName }}</h2>

      </div>


      <p class="deadline">Deadline Project: {{ project.deadlineDate }} {{ project.deadlineTime }} </p>
      <p class="total-time">Total: {{ convertDurationToDHMS(project.total) }}</p>
    </div>
    <Task v-for="task in project.tasks" :key="task.task_id" :task="task" :additionalData="project" :taskTimer="taskTimer" :text="text"/>
  </div>
  <Buttons :text="text" :additionalData="project" @submit-success="handleNewData" :key="project.id"></Buttons>
</template>


<style scoped>
.proj-wrapper {
  background-color: #31594a;
  padding: 40px 20px;
  border-radius: 10px;
  margin-bottom: 1rem;

  & > .proj-head {

    width: 100%;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;justify-items: flex-end;
    border: 1px solid aliceblue;
    margin-bottom: 2rem;
    padding: 1rem;
    border-radius: 10px;
  }
}

.title-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-self: baseline;
  > * {
    padding-right: 5px;
  }
}
</style>