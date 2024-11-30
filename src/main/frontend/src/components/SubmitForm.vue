<script setup>
import {ref} from 'vue';
import axios from 'axios';
import {formatDateForBackend} from "@/utils/timeUtils.js";
import {useForm} from "@/composables/useForm.js";
import {useFormData, useFormType} from "@/composables/useFormData.js";
//definiert Props die von der Elternkomponente übergeben werden
const props = defineProps({
  formType: String,
  submitUrl: String,
  additionalField: String,
  additionalValue: {
    type: [String, Number],
  },
  showModal: Boolean,
});
// emit = ausstrahlen, abgeben
//ermöglicht es der Komponente, mit ihrer übergeordneten Komponente zu kommunizieren, indem sie diese Ereignisse aussendet.
const emit = defineEmits(['submit-success', 'close']);

const {title, description, deadline} = useFormType(props.formType);

const {errors} = useForm();


const submit = async (event) => {
  try {

    deadline.value = deadline.value ? formatDateForBackend(deadline.value) : null;
    const data = useFormData(props, title.value, description.value, deadline.value);

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
   emit('submit-success', response.data);
    emit('close');
  } catch (error) {

    if (error.response && error.response.data) {
      error.response.data.forEach(err => {
        if (err.field) {
          console.log("err", err);
          errors.value[err.field] = err.message;
        }
      });
    } else {
      console.error('Error creating project', error);
    }
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
    <input type="date" id="deadline" v-model="deadline"/>
    <span v-if="errors.deadline" class="error-message">{{ errors.deadline }}</span>
    <input type="hidden" v-if="additionalField" :value="additionalValue" :name="additionalField"/>


    <button type="submit" @close="toggleModal">Submit {{ formType }}</button>
  </form>
</template>


<style scoped>
.error-message {
  color: red;
  font-size: 0.875em;
}

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
