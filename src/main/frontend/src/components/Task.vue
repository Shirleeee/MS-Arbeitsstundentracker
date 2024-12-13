<script setup>

import Timer from "./Timer.vue";
import PencilUpdate from "@/components/icons/PencilButtonSVG.vue";
import CreateModal from "@/components/Modal.vue";


const props = defineProps({
  task: {
    type: Object,
    required: true,
    default: () => ({timeEntries: []})
  },
  additionalData: Object,
  taskTimer: Array
});

//TODO in Project vue auch vorhanden - seperate Datei auslagern
const currentData = (id, projectId, name, description, deadline) => {
  return {
    task_id:id,
    projectId:projectId,
    name: name,
    description: description,
    deadline: deadline
  }
};
const emit = defineEmits(['submit-success', 'delete-success']);
const handleUpdateSuccess = (data) => {
  emit('update-task-success', data);
};
</script>

<template>
  <ul class="task">
    <li>
      <div class="title-container">
      <PencilUpdate text="Task" @submit-success="handleUpdateSuccess" :additionalData="additionalData"
                    :currentData="currentData(task.task_id,task.projectId,task.name.taskName,task.description.taskDescription	,task.deadline.deadline)"/>
      <p> Task: {{ task.name.taskName }}</p>

      </div>
      <div>
        <p>Deadline: {{ task.deadlineDate }} {{ task.deadlineTime }}</p>

      </div>
      <div v-if="props.taskTimer && props.taskTimer.length">
        <Timer
            v-for="timer in props.taskTimer.filter(taskTime => task.task_id.toString() === taskTime.task_id.toString())"
            :key="timer.id" :timer="timer"/>
      </div>

    </li>
  </ul>
</template>

<style scoped>
ul.task {
  width: 100%;
  display: flex;
  flex-direction: column;

  & > li {
    width: 100%;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
   &> .title-container{
      display: flex;
      flex-direction: row;
      align-items: center;
      width: 30%;
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


  }
}


</style>


<style scoped>


ul.task {
  width: 100%;
  display: flex;
  flex-direction: column;


  & > li {
    width: 100%;
    display: flex;
    padding-bottom: 20px;
    flex-direction: row;
    justify-content: space-between;

    & > .icons-container .icons {
      & > * {
        padding-right: 5px;
        cursor: pointer;
      }
    }

  }

}
</style>