
<script setup>
import { ref } from 'vue';

import Buttons from "@/components/Buttons.vue";

const props = defineProps({
  projects: Array
});

const text = ref('Project');
const emit = defineEmits(['update-projects']);
const handleNewData = (data) => {
  if (!data) {
    console.error('Received undefined data',data);
    return;
  }
  console.log(typeof props.projects);

  console.log("dd",data);

  if (data.userId) {
    emit('update-projects', data);
  }
};
</script>

<template>
  <header>
    <div class="header-container">
      <h1>Hallo Shirley</h1>
    <Buttons :text="text" :additionalData="props.projects" @submit-success="handleNewData"> </Buttons>

    </div>

  </header>
</template>

<style scoped>
header {
  width: 100%;
  margin: 1rem;
  display: block;
}


.header-container {
  display: flex;
  justify-content: space-between;
}
</style>