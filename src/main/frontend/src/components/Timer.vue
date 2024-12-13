<script setup>
import {ref, onMounted, onBeforeUnmount} from 'vue';
import {secondsToTimeFormat, getBerlinDateTime} from "@/utils/timeUtils.js";
import PlayButtonSVG from "@/components/icons/PlaySvg.vue";
import StopSvg from "@/components/icons/StopSvg.vue";
import axios from "axios";
import {handleErrorResponse} from "@/utils/errorResponse.js";

const props = defineProps({
  timer: Object,
  key: String
});

let errors = ref({
  startTime: '',
  endTime: '',
  Duration: '',
});

const timerId = ref();

const clearTimerState = (timer) => {
  localStorage.removeItem(`timer_${timer.task_id}`);
};

const saveTimerState = (timer) => {
  console.log("SAVETIMERSTATE props.timer.id", props.timer.id);
  const timerState = {
    id: props.timer.id,
    trackedTime: timer.trackedTime,
    isPlaying: timer.isPlaying,
    startTime: timer.startTime || null,
  };
  localStorage.setItem(`timer_${props.timer.task_id}`, JSON.stringify(timerState));
  console.log("Saved state:", localStorage.getItem(`timer_${props.timer.task_id}`));
};

const startTimer = (timer) => {

  if (timer.timer) {
    clearInterval(timer.timer);
  }

  timer.timer = setInterval(() => {
    timer.trackedTime += 1;

  }, 1000);
  timer.isPlaying = true;
};

const stopTimer = (timer) => {
  clearInterval(timer.timer);
  timer.isPlaying = false;
};

const restoreTimerState = (timer) => {


  const savedState = localStorage.getItem(`timer_${props.timer.task_id}`);
  console.log("savedState restoreTimerState", savedState);
  if (savedState) {
    const {id, trackedTime, isPlaying, startTime} = JSON.parse(savedState);
    timer.trackedTime = trackedTime || 0;
    timer.startTime = startTime;
    timer.id = id;


    if (isPlaying) {

      timer.trackedTime = Math.floor((Date.now() - new Date(startTime).getTime()) / 1000);
      startTimer(timer);
    }
  }
};

const submitStartTime = async (timer) => {
  startTimer(timer);
  try {

    const currentDateTimeLocal = getBerlinDateTime();
    const data = {
      taskId: props.timer.task_id,
      startTime: {startTime: currentDateTimeLocal},
    };
    timer.startTime = currentDateTimeLocal;

    const response = await axios.post(import.meta.env.VITE_SUBMIT_STARTTIME_URL, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    timer.id = response.data.id;
    console.log("response submitStartTime timer", timer);

    console.log("response submitStartTime", response);
    timerId.value = response.data.id;
    saveTimerState(timer);

    errors.value = {
      startTime: '',
      endTime: ''
    };

  } catch (error) {
    errors.value = handleErrorResponse(error, errors);
  }
};

const submitStopTime = async (timer) => {
  try {
    const timerItem = localStorage.getItem(`timer_${props.timer.task_id}`);
    // const timerObject = JSON.parse(timerItem);
    console.log("timerItem", timerItem);
    clearTimerState(timer);

    stopTimer(timer);
    localStorage.clear();
    const currentDateTimeLocal = getBerlinDateTime();


    const data = {
      id: props.timer.id,
      endTime: {endTime: currentDateTimeLocal},
    };
    const response = await axios.post(import.meta.env.VITE_SUBMIT_ENDTIME_URL, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });

    console.log("response", response);

    errors.value = {
      name: '',
      startTime: '',
      endTime: ''
    };

    timerId.value = null;
  } catch (error) {
    handleErrorResponse(error, errors);
  }
};

onMounted(() => {
  restoreTimerState(props.timer);
});

</script>

<template>
  <div class="icons">
    <p>Time Elapsed: {{ secondsToTimeFormat(timer.trackedTime) }}</p>
    <i class="play-btn" @click="submitStartTime(timer)">
      <PlayButtonSVG/>
      <span v-if="errors.startTime" class="error-message">{{ errors.startTime }}</span>
    </i>
    <i class="stop-btn" @click="submitStopTime(timer)">
      <StopSvg/>
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