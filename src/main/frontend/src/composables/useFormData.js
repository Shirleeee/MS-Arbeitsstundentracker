import {ref} from "vue";

export function useFormData(props, title, description, deadline) {
    let data;
console.log('useFormData - props.projectId ', props.projectId )
    console.log('useFormData - title', title)
    console.log('useFormData - description', description)
    console.log('useFormData - deadline', deadline)

    if (props.text === 'Task') {
        data = {
            projectId: props.additionalValue,
            name: {taskName: title},
            description: {taskDescription: description},
            deadline: {deadline: deadline},
        };
    } else if (props.text === 'Project') {
        data = {
            userId: props.actionType === 'Update' ? props.projectId : props.additionalValue,
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

    const title = ref('');
    const description = ref('');
    const deadline = ref('');

    if (typeof currentData === 'object') {
        console.log('useFormType currentData', currentData)

            title.value = currentData.name;
            description.value = currentData.description;
            deadline.value = currentData.deadline;

    }else{

        console.log('formType', formType)
        if (formType === 'Task') {
            title.value = 'TASK create form in vueJs ';
            description.value = 'create form in vueJs';
            deadline.value = '';
        } else if (formType === 'Project') {
            title.value = 'PROJECT create form in vueJs'//.repeat(200);
            description.value = 'create form in vueJs'//.repeat(200);
            deadline.value = '';
        }

    }


    return {title, description, deadline};
}
