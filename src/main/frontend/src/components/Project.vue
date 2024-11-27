<template>
  <div class="proj-wrapper">
    <div class="proj-head">
      <h2 class="title">{{ project.id }} - {{ project.name }}</h2>
      <p class="deadline">Deadline Project: {{ project.deadlineDate }}</p>
      <p class="total-time">Total: {{ secondsToTimeFormat(project.total) }}</p>
    </div>
    <Task v-for="task in project.tasks" :key="task.id" :task="task" />
  </div>
  <Buttons :text="text" :project="project"></Buttons>
</template>

<script>
import { secondsToTimeFormat } from "@/utils/timeUtils";
import Task from "./Task.vue";
import Buttons from "@/components/Buttons.vue";

export default {
  props: {
    project: Object,
    taskTimer: Array
  },
  components: {Buttons, Task },
  methods: { secondsToTimeFormat },
};
</script>


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