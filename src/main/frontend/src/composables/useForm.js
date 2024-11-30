import { ref } from 'vue';

export function useForm() {
    const errors = ref({
        title: '',
        description: '',
        deadline: '',
    });

    const validateForm = (title, deadline) => {
        let isValid = true;
        errors.value = { title: '', description: '', deadline: '' }; // Reset errors

        if (!title) {
            errors.value.title = 'Title is required';
            isValid = false;
        }

        if (!deadline) {
            errors.value.deadline = 'Deadline is required';
            isValid = false;
        }

        return isValid;
    };

    return { errors, validateForm };
}
