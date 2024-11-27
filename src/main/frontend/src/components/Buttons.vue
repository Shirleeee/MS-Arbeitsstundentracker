<script setup>


</script>
<script>
import CreateModal from "@/components/Modal.vue";

export default {
props: ['text'],
  name: 'Home',
  components: {CreateModal},
  data() {
    return {
      showModal: false,
    }
  },
  methods: {

    toggleModal() {

      this.showModal = !this.showModal
    }

  }
}
</script>
<template>

  <div class="btn-container">

    <div class="export-btn-wrapper">
      <button>Export</button>
    </div>


    <div class="create-btn-wrapper">
      <div v-if="showModal">
        <CreateModal :text="text" :project="project" @close="toggleModal" >
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