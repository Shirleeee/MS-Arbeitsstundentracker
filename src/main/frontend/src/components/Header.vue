<script setup>
import {ref} from 'vue';
import Buttons from "@/components/Buttons.vue";
import {handleNewDateTime} from "@/composables/handleNewDate";

const props = defineProps({
  projects: Array,
  user: Object
});

const text = ref('Project');
const emit = defineEmits(['submit-success']);
const handleNewData = (data) => {
  if (!data) {
    console.error('Received undefined data', data);
    return;
  }
  data = handleNewDateTime(data);
  if (data.userId) {
    emit('submit-success', data);
  }

};
</script>

<template>
  <header>
    <div class="header-container">
      <h1>Hallo {{ user.name }}</h1>
      <Buttons :text="text" :additionalData="props.projects" :projId="0" :userId=user.id
               @submit-success="handleNewData"></Buttons>

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