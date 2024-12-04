

<script setup>
import CreateModal from "@/components/Modal.vue";
import {ref} from 'vue';

const props = defineProps({
  text: String,
  additionalData: Object,
  showModal: Boolean,
  projectId: String,
  currentProjectData: Object
});


const isModalOpen = ref(false);
const handleClose = () => {
  isModalOpen.value = false;
};
const emit = defineEmits(['update-success']);
const toggleModal = () => {
  isModalOpen.value = !isModalOpen.value;
};
const handleUpdateSuccess = (data) => {
  isModalOpen.value = false;
  emit('update-success', data);
};
</script>



<template>
<div class="svg-container">
  <CreateModal v-if="isModalOpen" :projectId="props.projectId" :text="text" actionType="Update"
               :currentProjectData=props.currentProjectData
               :additionalData="additionalData"
               @close="handleClose"
               @update-success="handleUpdateSuccess"/>
  <svg @click="toggleModal"
     aria-hidden="true"
     role="img"
     width="24"
     height="24"
     class="iconify iconify--mdi"
     xmlns="http://www.w3.org/2000/svg"   viewBox="0 0 24 24">
  <path fill="currentColor" d="M20.71,7.04C21.1,6.65 21.1,6 20.71,5.63L18.37,3.29C18,2.9 17.35,2.9 16.96,3.29L15.12,5.12L18.87,8.87M3,17.25V21H6.75L17.81,9.93L14.06,6.18L3,17.25Z" />
</svg>
</div>
</template>


<style scoped>
  svg {

    fill: currentColor;
    &:hover {
      border: currentColor solid 1px;
    }
  }
  .svg-container {
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;

  }
</style>
