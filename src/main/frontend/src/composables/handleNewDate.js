import {ref} from "vue";
import {formatDate, formatTime} from "@/utils/timeUtils.js";

export const handleNewDateTime = (data) => {
    const deadlineDate = ref(formatDate(data.deadline.deadline));
    const deadlineTime = ref(formatTime(data.deadline.deadline));

    console.log("HEADER$ deadlineDate", deadlineDate.value);
    console.log("HEADER$ deadlineTime", deadlineTime.value);
    data.deadlineDate = deadlineDate.value;
    data.deadlineTime = deadlineTime.value;

    return data;
}