
<script setup>
import { ref, computed } from 'vue';
import Timer from "./Timer.vue";
import { useFetchProjects } from "@/composables/useFetchProjects.js";

const props = defineProps({
  task: Object,
  taskTimer: Array
});

const { projects, taskTimer, fetchData, error } = useFetchProjects();

fetchData();
</script>

<template>
  <ul class="task">
    <li>
      <p>Task: {{ task.name }}</p>
      <p>Deadline: {{ task.deadlineDate }} {{ task.deadlineTime }}</p>
      <div v-if="taskTimer && taskTimer.length">
        <Timer v-for="timer in taskTimer.filter(taskTime => task.id.toString() === taskTime.taskId.toString())" :key="timer.id" :timer="timer" />
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


  & > li{
    width: 100%;

    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;

    & > .icons-container .icons {
      & > * {
        padding-right: 5px;
        cursor: pointer;
      }
    }

  }

}
</style>