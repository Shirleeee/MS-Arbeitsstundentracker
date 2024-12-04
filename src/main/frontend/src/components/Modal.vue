<script setup>

import {ref, computed} from 'vue';
import SubmitForm from './SubmitForm.vue';

const props = defineProps({
  text: String,
  additionalData: Object,
  showModal:Boolean,
  actionType: String,
  currentProjectData: Object,
  projectId: String,
});
console.log("MDOAlcurrent",props.currentProjectData);

console.log('MODAL props.actiontype', props.actionType);
const userId = ref('101');
const projectId = ref('');
// const actionType = ref(props.actionType);
const emit = defineEmits(['submit-success', 'close', 'update-success']);
const handleUpdateSuccess = (data) => {
  console.log('MODAL handleUpdateSuccess', data);
  emit('close');
  emit('update-success', data);
};
const handleSubmitSuccess = (data) => {
  console.log('MODAL handleSubmitSuccess', data);

  emit('submit-success', data);
  emit('close');
};
const getSubmitUrl = (type) => {
  if (type === 'Project') {
    return props.actionType === 'Create'
        ? `http://localhost:8081/api/submitProjectData`
        : `http://localhost:8081/api/updateProjectData`;
  }else if (type === 'Task') {
    return props.actionType === 'Create'
        ? `http://localhost:8081/api/submitTaskData`
        : `http://localhost:8081/api/updateTaskData`;
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
          :projectId="props.projectId"
          :text="text"
          :currentProjectData=props.currentProjectData
          @update-success="handleUpdateSuccess"
          @submit-success="handleSubmitSuccess"
      />

      <SubmitForm
          v-if="text === 'Task'"
          formType="Task"
          @close="toggleModal"
          :showModal="showModal"
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
