<script setup>
import CreateModal from "@/components/Modal.vue";
import {useFetchProjects} from "@/composables/useFetchProjects.js";
import {ref} from 'vue';

const props = defineProps({
  text: String,
  additionalData: Object,
  showModal: Boolean,
  tasks: Array,
  actionType: String,
});

const isModalOpen = ref(false);

const toggleModal = () => {
  isModalOpen.value = !isModalOpen.value;
};
const emit = defineEmits(['submit-success']);

const handleSubmitSuccess = (data) => {
  isModalOpen.value = false;
  emit('submit-success', data);
};
const handleClose = () => {
  isModalOpen.value = false;
};

</script>

<template>
  <div class="btn-container">
    <div class="export-btn-wrapper">
      <button>Export</button>
    </div>
    <div class="create-btn-wrapper">
      <CreateModal v-if="isModalOpen" actionType="Create" :text="text" current-data="Create" :additionalData="props.additionalData"
                   @close="handleClose"
                   @submit-success="handleSubmitSuccess"/>
      <button @click="toggleModal">Create {{ text }}</button>
    </div>
  </div>
</template>

<style scoped>
.btn-container {
  display: flex;
  flex-direction: row;
  margin: 1rem;

  & > div {
    margin-right: 1rem;
  }
}

</style>