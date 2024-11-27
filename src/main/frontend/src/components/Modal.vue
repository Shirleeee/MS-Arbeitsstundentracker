<script setup>

import { ref, computed } from 'vue';
import SubmitForm from './SubmitForm.vue';

const props = defineProps({
  text: String,
  additionalData: Object
});



const userId = ref('101');
const projectId = ref('');

const getCurrentDay = computed(() => {
  const today = new Date();
  return today.toISOString().split('T')[0];
});
const emit = defineEmits(['submit-success', 'close']);

const handleSubmitSuccess = (data) => {

  emit('submit-success',data);
  // Emit the 'close' event to close the modal
  emit('close');
};

</script>

<template>
  <div class="backdrop greetings" @click.self="emit('close')">
    <div class="modal">

      <h1 class="green">{{ text }}</h1>

      <SubmitForm
          v-if="text === 'Project'"
          formType="Project"
          submitUrl="http://localhost:8081/api/submitProjectData"
          additionalField="userId"
          :additionalValue="userId"
          @submit-success="handleSubmitSuccess"
      />

      <SubmitForm
          v-if="text === 'Task'"
          formType="Task"
          submitUrl="http://localhost:8081/api/submitTaskData"
          additionalField="projectId"
          :additionalValue="props.additionalData.id"
          @submit-success="handleSubmitSuccess"
      />
    </div>

  </div>


</template>

<style scoped>

.modal {
  width: 400px;
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  margin: auto;

  & > p {
    color: #181818;
  }
}

.backdrop {
  top: 0;
  left: 0;
  position: fixed;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);;
}

h1 {
  font-weight: 500;
  font-size: 2.6rem;
  position: relative;
  top: -10px;
}


h3 {
  font-size: 1.2rem;
}
.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}
</style>
