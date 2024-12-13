import {ref} from "vue";

export function useFormData(props, title, description, deadline, additionalValue) {
    let data;
    // console.log('useFormData - props.currentData.id', props.currentData)
    // console.log('useFormData - props.actionType', props.actionType)
    // console.log('useFormData - props.additionalValue', additionalValue)

    if (props.text === 'Task') {
        data = {
            task_id: props.currentData.task_id,
            projectId: props.actionType === 'Update' ? props.currentData.projectId : props.additionalValue,
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
    // console.log('useFormData - data', data)
    return data;
}

export function useFormType(formType, currentData, addValue) {

    const title = ref('');
    const description = ref('');
    const deadline = ref('');
    const additionalValue = ref('');
    if (typeof currentData === 'object') {
        title.value = currentData.name;
        description.value = currentData.description;
        deadline.value = currentData.deadline;

    } else {

        if (formType === 'Task' && currentData === 'Create') {
            title.value = 'TASK create form in vueJs ';
            description.value = 'create form in vueJs';
            deadline.value = '';
            additionalValue.value = '';
        } else if (formType === 'Project' && currentData === 'Create') {
            title.value = 'PROJECT create form in vueJs'//.repeat(200);
            description.value = 'create form in vueJs'//.repeat(200);
            deadline.value = '';
            additionalValue.value = addValue;
        }

    }


    return {title, description, deadline, additionalValue};
}
