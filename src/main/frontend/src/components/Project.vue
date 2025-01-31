<script setup>
import {ref} from 'vue';
import {convertDurationToDHMS} from "@/utils/timeUtils";
import Task from "./Task.vue";
import Buttons from "@/components/Buttons.vue";
import PencilUpdate from "@/components/icons/PencilButtonSVG.vue";
import TrashDelete from "@/components/icons/TrashSvg.vue";
import axios from "axios";

const props = defineProps({
  project: {
    type: Object,
    required: true,
    default: () => ({tasks: []})
  },

  user: Object,
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
  emit('submit-task-success', data);
};
const handleUpdateSuccess = (data) => {
  emit('update-project-success', data);
};
const handleUpdateTaskSuccess = (data) => {
  emit('update-task-success', data);
};

const handleDeleteTaskSuccess = (task) => {
  emit('delete-task-success', task);
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

        <h2 class="title">{{ project.name.projectName }}</h2>

      </div>
      <p class="deadline">Deadline Project: {{ project.deadlineDate }} {{ project.deadlineTime }} </p>

      <p class="total-time">Total: {{ convertDurationToDHMS(project.total) }}</p>

      <div class="delete">

        <TrashDelete @click="deleteProject(project.id)"/>

      </div>
    </div>
    <Task @update-task-success="handleUpdateTaskSuccess" @delete-task-success="handleDeleteTaskSuccess"
          v-for="task in project.tasks" :key="task.task_id" :task="task" :additionalData="project"
          :taskTimer="taskTimer" :text="text"/>
  </div>
  <Buttons :text="text" :additionalData="project" :projId="project.id" :userId=user.id
           @submit-success="handleSubmitSuccess" :key="project.id"></Buttons>
</template>


<style scoped>

.proj-wrapper {
  background-color: #31594a;
  padding: 40px 20px;
  border-radius: 10px;
  margin-bottom: 1rem;

  & > .proj-head {

    width: 100%;

    align-items: center;
    margin-bottom: 2rem;
    padding: 1rem;
    border-radius: 10px;

    & > .total-time {

      font-weight: 700;
    }
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