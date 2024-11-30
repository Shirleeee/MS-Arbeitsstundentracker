import { formatDateForBackend } from "@/utils/timeUtils.js";
import {ref} from "vue";

export function useFormData(props, title, description, deadline) {
    let data;

    if (props.formType === 'Task') {
        data = {
            projectId: { value: props.additionalValue },
            name: { taskName: title },
            description: { taskDescription: description },
            deadline: { deadline: formatDateForBackend(deadline) },
        };
    } else if (props.formType === 'Project') {
        data = {
            userId: props.additionalValue,
            name: { projectName: title },
            description: { projectDescription: description },
            deadline: { deadline: formatDateForBackend(deadline) },
        };
    }

    return data;
}
export function useFormType(formType) {
    const title = ref('');
    const description = ref('');
    const deadline = ref('');

    if (formType === 'Task') {
        title.value = 'TASK create form in vueJs ';
        description.value = 'create form in vueJs';
        deadline.value = '';
    } else if (formType === 'Project') {
        title.value = 'PROJECT create form in vueJs'.repeat(200);
        description.value = 'create form in vueJs'.repeat(200);
        deadline.value = '';
    }

    return { title, description, deadline };
}
