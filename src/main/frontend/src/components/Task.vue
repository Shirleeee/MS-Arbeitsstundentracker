<template>
  <ul class="task">
    <li>
      <p>Task: {{ task.name }}</p>
      <p>Deadline: {{ task.deadlineDate }} {{ task.deadlineTime }}</p>
      <div>
        <Timer v-for="timer in this.taskTimer.filter(taskTime => task.id.toString() === taskTime.taskId.toString())" :timer="timer" />
      </div>
    </li>
  </ul>
</template>

<script>
import Timer from "./Timer.vue";
import {useFetchProjects} from "@/composables/useFetchProjects.js";
export default {
  props: {
    task: Object,
    taskTimer: Array
  },
  setup() {

    const { projects, taskTimer, fetchData, error } = useFetchProjects();


    fetchData();

    return {  taskTimer };
  },
  computed: {
    // taskTimers() {
    //
    //   console.log('project', this.taskTimer)
    //
    //   return this.taskTimer || [];
    // },
  },
  components: { Timer },
};
</script>


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