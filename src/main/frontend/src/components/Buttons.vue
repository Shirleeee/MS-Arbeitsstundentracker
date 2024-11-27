<script setup>
import CreateModal from "@/components/Modal.vue";

import { ref } from 'vue';

const props = defineProps({
  text: String,
  additionalData: Object
});


console.log('Class:BUTTONS, Function: , Line 5 (): ', props.text)

const showModal = ref(false);

function toggleModal() {
  showModal.value = !showModal.value;
}
</script>

<template>

  <div class="btn-container">

    <div class="export-btn-wrapper">
      <button>Export</button>
    </div>


    <div class="create-btn-wrapper">
      <div v-if="showModal">
        <CreateModal :text= text  :additionalData="additionalData" @close="toggleModal" >
        </CreateModal>
      </div>
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