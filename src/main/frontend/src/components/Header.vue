<script>


export default {
  data() {
    return {
      text: 'Project',
      showModal: false,
    }
  },
  methods: {

  }
}
</script>
<script setup>
import Buttons from '@/components/Buttons.vue';
</script>
<template>
  <header>
    <div class="header-container">
      <h1>Hallo Shirley</h1>
    <Buttons :text="text"> </Buttons>

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