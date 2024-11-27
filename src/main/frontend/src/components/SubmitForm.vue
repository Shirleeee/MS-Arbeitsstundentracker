<template>
  <form @submit.prevent="submit">
    <label for="title">Title</label>
    <input type="text" id="title" v-model="title"/>

    <label for="description">Description</label>
    <textarea id="description" v-model="description"></textarea>

    <label for="deadline">Deadline date:</label>
    <input type="date" id="deadline" v-model="deadline"/>

    <input type="hidden" v-if="additionalField" :value="additionalValue" :name="additionalField"/>

    <button type="submit">Submit {{ formType }}</button>
  </form>
</template>
<script setup>
import { ref } from 'vue';
import axios from 'axios';

const props = defineProps({
  formType: {
    type: String,
    required: true,
  },
  submitUrl: {
    type: String,
    required: true,
  },
  additionalField: {
    type: String,
    required: false,
  },
  additionalValue: {
    type: [String, Number],
    required: false,
  },
});

const title = ref('create form in vueJs');
const description = ref('create form in vueJs');
const deadline = ref('');

const submit = async () => {
  try {
    const data = {
      name: title.value,
      description: description.value,
      deadline: new Date(deadline.value),
    };

    if (props.additionalField) {
      data[props.additionalField] = props.additionalValue;
    }

    const response = await axios.post(props.submitUrl, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });

    console.log('Daten erfolgreich gesendet:', response.data);
  } catch (error) {
    console.error('Fehler beim Senden der Daten:', error);
  }
};
</script>

<style scoped>

form {
  display: flex;
  flex-direction: column;

  & > label {
    color: #181818;
  }
}
button {
  margin-top: 10px;
}
h3 {
  font-size: 1.2rem;
}
</style>
