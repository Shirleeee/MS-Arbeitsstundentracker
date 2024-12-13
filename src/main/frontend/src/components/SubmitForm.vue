<script setup>
import {ref} from 'vue';
import axios from 'axios';

import {useFormData, useFormType} from "@/composables/useFormData.js";
import {getBerlinDateTime} from "@/utils/timeUtils.js";
import {handleErrorResponse} from "@/utils/errorResponse.js";
//definiert Props die von der Elternkomponente übergeben werden
const props = defineProps({
  formType: String,
  text: String,
  submitUrl: String,
  additionalField: String,
  additionalValue: {
    type: [String, Number],
  },
  actionType: String,
  currentData: Object,
  showModal: Boolean,
});
// emit = ausstrahlen, abgeben
//ermöglicht es der Komponente, mit ihrer übergeordneten Komponente zu kommunizieren, indem sie diese Ereignisse aussendet.
const emit = defineEmits(['submit-success', 'close']);

const {title, description, deadline,additionalValue} = useFormType(props.text, props.currentData, props.additionalValue);

let errors = ref({
  title: '',
  description: '',
  deadline: '',
});

const submit = async (event) => {

  try {
    // console.log("Submit form  deadline.value",  deadline.value);
    deadline.value = deadline.value ? deadline.value : null;
    const data = useFormData(props, title.value, description.value, deadline.value,additionalValue.value);

    const response = await axios.post(props.submitUrl, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    errors.value = {
      name: '',
      description: '',
      deadline: ''
    };

    // console.log("response.data", response.data);
    emit('submit-success', response.data);

    // title.value = response.data.projectName;
    // description.value = response.data.projectDescription;
    // deadline.value = response.data.deadline;
    emit('close');
  } catch (error) {

    errors = handleErrorResponse(error,errors);
  }
};


</script>


<template>

  <form @submit.prevent="submit">
    <label for="title">Title</label>
    <input type="text" id="title" v-model="title"/>
    <span v-if="errors.name" class="error-message">{{ errors.name }}</span>
    <label for="description">Description</label>
    <textarea id="description" v-model="description"></textarea>
    <span v-if="errors.description" class="error-message">{{ errors.description }}</span>
    <label for="deadline">Deadline date:</label>

    <input type="datetime-local" value={{getBerlinDateTime()}}  id="deadline" v-model="deadline"/>
    <span v-if="errors.deadline" class="error-message">{{ errors.deadline }}</span>

    <input type="hidden" v-if="additionalField" :value="additionalValue" :name="additionalField"/>
    <button v-if="text==='Project'" type="submit">{{ actionType === 'Create' ? 'Create Project' : 'Update Project' }}</button>
    <button v-if="text==='Task'" type="submit">{{ actionType === 'Create' ? 'Create Task' : 'Update Task' }}</button>
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
