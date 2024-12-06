<script setup>
import {ref} from 'vue';
import {secondsToTimeFormat,getBerlinDateTime} from "@/utils/timeUtils.js";
import PlayButtonSVG from "@/components/icons/PlayButtonSVG.vue";
import IconSupport from "@/components/icons/IconSupport.vue";
import axios from "axios";
import {handleErrorResponse} from "@/utils/errorResponse.js";

const props = defineProps({
  timer: Object,

});
let errors = ref({
  startTime: '',
  endTime: '',
  Duration: '',
});
const timerId = ref();

// const emit = defineEmits(['submitStartTime-success']);
// console.log("TIMER !!!!", props.timer);

const startTimer = (timer) => {
  timer.timer = setInterval(() => {
    timer.trackedTime += 1;
  }, 1000);
  timer.isPlaying = true;
};
const stopTimer = (timer) => {
  clearInterval(timer.timer);
  timer.isPlaying = false;
};

///!
const submitStartTime = async (timer) => {

  try {
    startTimer(timer);
    console.log("timer", timer);
    const currentDateTimeLocal = getBerlinDateTime();
    console.log("currentData", currentDateTimeLocal);
console.log("timer.task_id", props.timer.task_id);
    const data = {
      taskId: props.timer.task_id,
      startTime: {startTime: currentDateTimeLocal},
    };
    const response = await axios.post(import.meta.env.VITE_SUBMIT_STARTTIME_URL, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    timerId.value = response.data.id;
    errors.value = {

      startTime: '',
      endTime: ''
    };

    console.log("TIMER response.data", response.data);
    // emit('submitStartTime-success', response.data);

  } catch (error) {

    errors = handleErrorResponse(error,errors);
  }
};

const submitStopTime = async (timer) => {

  try {
    stopTimer(timer);
    console.log("timer", timer);
    const currentDateTimeLocal = getBerlinDateTime();
    console.log("currentDataStopp", currentDateTimeLocal);
    console.log("timerId", timerId.value);

    const data = {
      id: timerId.value,
      endTime: {endTime: currentDateTimeLocal},
    };
    const response = await axios.post(import.meta.env.VITE_SUBMIT_ENDTIME_URL, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });

    errors.value = {
      name: '',
      startTime: '',
      endTime: ''
    };

    console.log("TIMER response.data", response.data);
    // emit('submitStartTime-success', response.data);

    timerId.value = null;
  } catch (error) {
    handleErrorResponse(error,errors);
  }
};


</script>

<template>
  <div class="icons">
    <p>Time Elapsed: {{ secondsToTimeFormat(timer.trackedTime) }}</p>
    <i class="play-btn" @click="submitStartTime(timer)">
      <PlayButtonSVG/>
      <span v-if="errors.startTime" class="error-message">{{ errors.startTime }}</span>

    </i>
    <i class="stop-btn" @click="submitStopTime(timer)">
      <IconSupport/>
      <span v-if="errors.endTime" class="error-message">{{ errors.endTime }}</span>
    </i>
  </div>
</template>


<style scoped>



.icons {
  & > * {
    padding-block: 1rem;

    padding-right: 5px;
    cursor: pointer;
  }
}


</style>