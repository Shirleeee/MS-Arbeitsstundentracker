<script setup>
import CreateModal from "@/components/Modal.vue";
import {useFetchProjects} from "@/composables/useFetchProjects.js";
import {ref} from 'vue';
import axios from "axios";

const props = defineProps({
  text: String,
  additionalData: Object,
  showModal: Boolean,
  tasks: Array,
  actionType: String,
  userId: String,
  projId: Number
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
const exportData = (userId, projId) => {

  const url = import.meta.env.VITE_EXPORT_WHOLE_URL + userId + "/" + projId;
  // Sende eine POST-Anfrage an das Backend, um die PDF zu erzeugen
  axios.post(url, {}, {responseType: 'blob'})
      .then(response => {
        // Hier wird die PDF vom Backend empfangen
        const blob = response.data;
        const link = document.createElement('a');
        const url = window.URL.createObjectURL(blob);
        link.href = url;
        link.download = `${userId}_${projId}_report.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
      })
      .catch(error => {
        console.error("Fehler beim Export:", error);
      });

};
</script>

<template>
  <div class="btn-container">
    <div class="export-btn-wrapper">
      <button @click="exportData(userId,projId)">Export</button>
    </div>
    <div class="create-btn-wrapper">
      <CreateModal v-if="isModalOpen" actionType="Create" :text="text" current-data="Create"
                   :additionalData="props.additionalData"
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