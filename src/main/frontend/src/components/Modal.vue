<script setup>

import {ref} from 'vue';
import SubmitForm from './SubmitForm.vue';

const props = defineProps({
  text: String,
  additionalData: Object,
  showModal: Boolean,
  actionType: String,
  currentData: Object,
});

const userId = ref('101');

const emit = defineEmits(['submit-success', 'close']);

const handleSubmitSuccess = (data) => {
  emit('submit-success', data);
  emit('close');
};
const getSubmitUrl = (type) => {
  if (type === 'Project') {
    return props.actionType === 'Create'
        ? import.meta.env.VITE_SUBMIT_PROJECT_URL
        : import.meta.env.VITE_UPDATE_PROJECT_URL;
  } else if (type === 'Task') {
    return props.actionType === 'Create'
        ? import.meta.env.VITE_SUBMIT_TASK_URL
        : import.meta.env.VITE_UPDATE_TASK_URL;
  }
  return '';
};
const methods = {
  closeModal() {
    emit('close');
  }
};


</script>

<template>
  <div class="backdrop greetings" @click.self="methods.closeModal">
    <div class="modal">

      <h1 class="green">{{ text }}</h1>

      <SubmitForm
          v-if="text === 'Project'"
          :actionType="actionType"
          :submitUrl="getSubmitUrl(text)"
          additionalField="userId"
          :additionalValue="userId"
          :text="text"
          :currentData=props.currentData
          @submit-success="handleSubmitSuccess"
      />

      <SubmitForm
          v-if="text === 'Task'"
          :actionType="actionType"
          :text="text"
          :submitUrl="getSubmitUrl(text)"
          additionalField="projectId"
          :additionalValue="props.additionalData.id"
          :currentData=props.currentData
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
