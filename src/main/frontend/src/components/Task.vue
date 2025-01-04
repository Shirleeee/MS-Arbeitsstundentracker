<script setup>

import Timer from "./Timer.vue";
import PencilUpdate from "@/components/icons/PencilButtonSVG.vue";
import TrashDelete from "@/components/icons/TrashSvg.vue";
import axios from "axios";


const props = defineProps({
  task: {
    type: Object,
    required: true,
    default: () => ({timeEntries: []})
  },
  additionalData: Object,
  taskTimer: Array
});

const currentData = (id, projectId, name, description, deadline) => {
  return {
    task_id: id,
    projectId: projectId,
    name: name,
    description: description,
    deadline: deadline
  }
};
const emit = defineEmits(['submit-success', 'delete-success']);
const handleUpdateSuccess = (data) => {
  emit('update-task-success', data);
};
const deleteTask = async (task) => {
  const id = task.task_id;
  const url = import.meta.env.VITE_DELETE_TASK_URL + id;

  try {
    const response = await axios.delete(url);

    if (response.status === 200) {
      console.log("response", response);
      alert("Task erfolgreich gelöscht!");
      emit("delete-task-success", task);
    }
  } catch (error) {
    console.error("Fehler beim Löschen:", error);
    alert("Löschen fehlgeschlagen.");
  }

};
</script>

<template>
  <ul class="task">
    <li class="task-list">
      <div class="title-container">
        <PencilUpdate text="Task" @submit-success="handleUpdateSuccess" :additionalData="additionalData"
                      :currentData="currentData(task.task_id,task.projectId,task.name.taskName,task.description.taskDescription	,task.deadline.deadline)"/>
        <p>Task: {{ task.name.taskName }}</p>

      </div>
      <div>
        <p>Deadline: {{ task.deadlineDate }} {{ task.deadlineTime }}</p>

      </div>
      <div class="icons" v-if="props.taskTimer && props.taskTimer.length">
        <Timer
            v-for="timer in props.taskTimer.filter(taskTime => task.task_id.toString() === taskTime.task_id.toString())"
            :key="timer.id" :timer="timer"/>
      </div>
      <div class="trash-container">
        <TrashDelete @click="deleteTask(task)"/>
      </div>
    </li>
  </ul>
</template>

<style scoped>
ul.task {
  width: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 10px;
  margin: 1rem 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);


  & > .task-list {
    width: 100%;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;

    & > div {
      display: flex;
      align-items: start;
      padding: 1rem;

      > * {
        padding-right: 5px;

      }
    }

    & > .icons-container .icons {
      & > * {
        padding-right: 5px;
        cursor: pointer;
      }
    }

    & > .trash-container {
      display: flex;
      justify-content: end;
    }


  }
}


</style>

