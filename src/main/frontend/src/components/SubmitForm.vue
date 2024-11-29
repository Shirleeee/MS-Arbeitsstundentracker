<script setup>
import {ref} from 'vue';
import axios from 'axios';
import {formatDateForBackend} from "@/utils/timeUtils.js";

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
const emit = defineEmits(['submit-success']);

const title = ref('create form in vueJs');
const description = ref('create form in vueJs');
const deadline = ref('');

const submit = async () => {
  try {
    let data;
    if (props.formType === 'Task') {
      data = {

        name: {taskName: title.value},
        description: {taskDescription: description.value},
        deadline: {deadline: formatDateForBackend(deadline.value)},
      };
    } else {
      data = {
        userId: 101,
        name: {projectName: title.value},
        description: {projectDescription: description.value},
        deadline: {deadline: formatDateForBackend(deadline.value)},
      };
    }

    console.log("data", data);
    if (props.additionalField) {
      data[props.additionalField] = props.additionalValue;
    }

    const response = await axios.post(props.submitUrl, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    console.log("response", response);
    if (response.data) {
      emit('submit-success', response.data);
    } else {
      console.error('Received empty response data');
    }

    emit('submit-success', response.data);
  } catch (error) {
    console.error('Fehler beim Senden der Daten:', error);
  }
};
</script>


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
