import {ref} from "vue";

export function useFormData(props, title, description, deadline) {
    let data;
    console.log('useFormData - props.currentData.id', props.currentData.id)
    console.log('useFormData - props.actionType', props.actionType)
    console.log('useFormData - props.additionalValue', props.additionalValue)

    if (props.text === 'Task') {
        data = {
            task_id	: props.actionType === 'Update' ? props.currentData.task_id : props.additionalValue,
            projectId: props.currentData.projectId,
            name: {taskName: title},
            description: {taskDescription: description},
            deadline: {deadline: deadline},
        };
    } else if (props.text === 'Project') {
        data = {
            userId: props.actionType === 'Update' ? props.currentData.id : props.additionalValue,
            name: {projectName: title},
            description: {projectDescription: description},
            deadline: {deadline: deadline},
        };

    }
console.log('useFormData - data', data)
    return data;
}

export function useFormType(formType, currentData) {
console.log('useFormType currentData', currentData)
    console.log('useFormType formType', formType)
    const title = ref('');
    const description = ref('');
    const deadline = ref('');

    if (typeof currentData === 'object') {
        console.log('useFormType currentData', currentData)
            title.value = currentData.name;
            description.value = currentData.description;
            deadline.value = currentData.deadline;

    }else{

        console.log('useFormType formType', formType)
        if (formType === 'Task' && currentData === 'Create') {
            title.value = 'TASK create form in vueJs ';
            description.value = 'create form in vueJs';
            deadline.value = '';
        } else if (formType === 'Project'&& currentData === 'Create') {
            title.value = 'PROJECT create form in vueJs'//.repeat(200);
            description.value = 'create form in vueJs'//.repeat(200);
            deadline.value = '';
        }

    }


    return {title, description, deadline};
}
