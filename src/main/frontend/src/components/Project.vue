<script setup>
import {reactive, ref} from 'vue';
import {convertDurationToDHMS} from "@/utils/timeUtils";
import Task from "./Task.vue";
import Buttons from "@/components/Buttons.vue";
import {handleNewDateTime} from "@/composables/handleNewDate.js";
import PencilUpdate from "@/components/icons/PencilButtonSVG.vue";
import TrashDelete from "@/components/icons/TrashSvg.vue";
import axios from "axios";

const props = defineProps({
  project: {
    type: Object,
    required: true,
    default: () => ({tasks: []})
  },
  taskTimer: Array
});


const text = ref('Task');

const currentData = (id, name, description, deadline) => {
  return {
    id: id,
    name: name,
    description: description,
    deadline: deadline
  }
};


const emit = defineEmits(['submit-success', 'delete-success', 'delete-task-success', 'update-project-success', 'update-task-success']);


const handleSubmitSuccess = (data) => {
  // isModalOpen.value = false;
  emit('submit-task-success', data);
};
const handleUpdateSuccess = (data) => {
  emit('update-project-success', data);
};
const handleUpdateTaskSuccess = (data) => {
  emit('update-task-success', data);
};

const handleDeleteTaskSuccess = (id) => {
  emit('delete-task-success', id);
};
const deleteProject = async (id) => {

  const url = import.meta.env.VITE_DELETE_PROJECT_URL + id;

  try {
    const response = await axios.delete(url);


    if (response.status === 200) {
      console.log("response", response);
      alert("Projekt erfolgreich gelöscht!");
      emit("delete-success", id);
    }
  } catch (error) {
    console.error("Fehler beim Löschen:", error);
    alert("Löschen fehlgeschlagen.");
  }

};

</script>

<template>
  <div class="proj-wrapper">
    <div class="proj-head">

      <div class="title-container">
        <PencilUpdate text="Project" @submit-success="handleUpdateSuccess"
                      :currentData="currentData(project.id,project.name.projectName,project.description.projectDescription,project.deadline.deadline)"/>

        <h2 class="title"> {{ project.id }}-{{ project.name.projectName }}</h2>

      </div>
      <p class="deadline">Deadline Project: {{ project.deadlineDate }} {{ project.deadlineTime }} </p>

      <p class="total-time">Total: {{ convertDurationToDHMS(project.total) }}</p>
      <TrashDelete @click="deleteProject(project.id)"/>


    </div>
    <Task @update-task-success="handleUpdateTaskSuccess"  @delete-task-success="handleDeleteTaskSuccess"  v-for="task in project.tasks" :key="task.task_id" :task="task" :additionalData="project"
          :taskTimer="taskTimer" :text="text"/>
  </div>
  <Buttons :text="text" :additionalData="project" @submit-success="handleSubmitSuccess" :key="project.id"></Buttons>
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
    grid-template-columns: 1fr 1fr 1fr 1fr;
    justify-items: end;
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