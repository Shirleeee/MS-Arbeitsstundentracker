<script setup>


</script>
<script>

export default {
  props: ['text'],
  data() {
    return {

      title: 'create forms in vue 3',
      description: 'forms are easy to create in vue 3'
    }
  },
  methods: {
    create() {
      console.log('this.title', this.$refs.name)
      this.$refs.name.classList.add('active')
      this.$refs.name.focus()
    },
    closeModal() {

      this.$emit('close');
    }
  }
}
</script>
<template>
  <div class="backdrop greetings" @click.self="$emit('close')">
    <div class="modal">

      <h1 class="green">{{ text }}</h1>

      <p>{{ text }}: {{ title }}</p>

      <form action="" method="get" @submit="create">
        <label for="title">{{ text }} Title</label>
        <input type="text" name="title" id="title" ref="name" v-model="title">

        <label for="projectDescription">Description</label>
        <textarea cols="40" rows="10" name="description" id="description"
                  v-model="description"></textarea>

        <button @click="create">Create {{ text }}</button>
      </form>
    </div>

  </div>


</template>

<style scoped>
form {
  display: flex;
  flex-direction: column;

  & > label {
    color: #181818;
  }
}

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
