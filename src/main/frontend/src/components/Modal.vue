<script setup>
import axios from 'axios';

</script>
<script>

import {ref} from "vue";
import axios from "axios";

export default {
  props: ['text'],
  data() {
    return {
      title : ref('create forms in vue 300'),
      description : ref('create forms in vue 3'),
      userId: ref('101'),
      currentDay:  this.getCurrentDay,
      deadline : ref(''),
    }
  },

  methods: {
    getCurrentDay:()=>{
      const today = new Date();
      return today.toISOString().split('T')[0];
    },
     submitData : async () => {
       console.log('Class: methods, Function: submitData, Line 35 (): '
           , deadline);
      try {
        const data = {
          name: title.value,
          description: description.value,
          userId:parseInt(userId.value),//TODO
          deadline: new Date(deadline.value)  ,
        };
  console.log('Class: methods, Function: submitData, Line 37 (): '
  , new Date(deadline));
        const response = await axios.post('http://localhost:8081/api/submitProjectData', data, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        console.log('Daten erfolgreich gesendet:', response.data);
      } catch (error) {
        console.error('Fehler beim Senden der Daten:', error);
      }
    },
    create() {
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

      <p>{{ text }}: {{ name }}</p>

      <form action="" method="get" @submit.prevent="submitData">
        <label for="title">{{ text }} Title</label>
        <input type="text" name="title" id="title" ref="title" v-model="title">
        <input hidden value="101" type="text" name="userId" id="userId" ref="userId" v-model="userId">

        <label for="projectDescription">Description</label>
        <textarea cols="40" rows="10" name="description" id="description"
                  v-model="description"></textarea>
        <label for="deadline">Deadline date:</label>

        <input type="date" id="deadline" name="deadline" :value=currentDay  />

        <button type="submit">Create {{ text }}</button>
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
