<script setup>
import { ref, onMounted, reactive } from 'vue';
import { secondsToTimeFormat, getBerlinDateTime } from "@/utils/timeUtils.js";
import PlayButtonSVG from "@/components/icons/PlaySvg.vue";
import StopSvg from "@/components/icons/StopSvg.vue";
import axios from "axios";
import { handleErrorResponse } from "@/utils/errorResponse.js";

const props = defineProps({
  timer: Object,
  key: String
});

let errors = ref({
  startTime: '',
  endTime: '',
  duration: '',
});
const hasRestoredState = ref(false);
const timerId = ref();
const timerState = reactive({
  activeTimerId: localStorage.getItem('activeTimerId') || null,
});

const clearTimerState = (timer) => {
  localStorage.removeItem(`timer_${timer.task_id}`);
  localStorage.removeItem('activeTimerId');
};

const saveTimerState = (timer) => {
  const state = {
    id: props.timer.id,
    trackedTime: timer.trackedTime,
    isPlaying: timer.isPlaying,
    startTime: timer.startTime || null,
  };
  localStorage.setItem(`timer_${props.timer.task_id}`, JSON.stringify(state));
  localStorage.setItem('activeTimerId', timerState.activeTimerId);
};

const startTimer = (timer) => {

  const activeId =  localStorage.getItem('activeTimerId');
  console.log("activeId", activeId);
  if (activeId && activeId !== timer.id) {
    console.warn("Ein anderer Timer läuft bereits:", timerState.activeTimerId);
    return false;
  }
  if (timer.isPlaying) {
    console.warn("Dieser Timer läuft bereits.");
    return false;
  }
  if (timer.timer) {
    clearInterval(timer.timer);
  }
  timer.timer = setInterval(() => {
    timer.trackedTime += 1;
  }, 1000);

  timer.isPlaying = true;
  timerState.activeTimerId = timer.id;
  localStorage.setItem('activeTimerId', timerState.activeTimerId);
  saveTimerState(timer);
  console.log("startTimer activeTimerId gesetzt auf", timerState.activeTimerId);
  return true;
};

const stopTimer = (timer) => {
  clearInterval(timer.timer);
  timer.isPlaying = false;
  if (timerState.activeTimerId === timer.id) {
    timerState.activeTimerId = null;
    localStorage.removeItem('activeTimerId');
  }
};

const restoreTimerState = (timer) => {
  const savedState = localStorage.getItem(`timer_${props.timer.task_id}`);
  if (savedState) {
    const { id, trackedTime, isPlaying, startTime } = JSON.parse(savedState);
    timer.trackedTime = trackedTime || 0;
    timer.startTime = startTime;
    timer.id = id;

    if (isPlaying && startTime) {
      const elapsedTime = Math.floor((Date.now() - new Date(startTime).getTime()) / 1000);
      timer.trackedTime += elapsedTime;

      if (!timer.timer) {
        timer.timer = setInterval(() => {
          timer.trackedTime += 1;
        }, 1000);
      }

      timer.isPlaying = true;
      timerState.activeTimerId = id;
      localStorage.setItem('activeTimerId', timerState.activeTimerId);
    } else {
      timer.isPlaying = false;
    }
  }
};


const submitStartTime = async (timer) => {

  if (timer.isPlaying) {
    console.warn("Ein anderer Timer läuft bereits oder dieser Timer läuft bereits.");
    return;
  }
  if (startTimer(timer)) {
    try {
      const currentDateTimeLocal = getBerlinDateTime();
      const data = {
        taskId: props.timer.task_id,
        startTime: { startTime: currentDateTimeLocal },
      };
      timer.startTime = currentDateTimeLocal;

      const response = await axios.post(import.meta.env.VITE_SUBMIT_STARTTIME_URL, data, {
        headers: {
          'Content-Type': 'application/json',
        },
      });
      timer.id = response.data.id;

      timerId.value = response.data.id;
      saveTimerState(timer);

      errors.value = {
        startTime: '',
        endTime: ''
      };
    } catch (error) {
      errors.value = handleErrorResponse(error, errors);
    }
  } else {
    console.warn("startTime existiert bereits:", timer.startTime);
  }

};

const submitStopTime = async (timer) => {
  try {
    const timerItem = localStorage.getItem(`timer_${props.timer.task_id}`);
    console.log("timerItem", timerItem);
    clearTimerState(timer);
    stopTimer(timer);
    localStorage.clear();
    const currentDateTimeLocal = getBerlinDateTime();

    const data = {
      id: props.timer.id,
      endTime: { endTime: currentDateTimeLocal },
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
    timer.timer = null;
  } catch (error) {
    handleErrorResponse(error, errors);
  }
};

onMounted(() => {

  restoreTimerState(props.timer);

});
</script>

<template>
  <p> Time Elapsed: {{ secondsToTimeFormat(timer.trackedTime) }}</p>
  <div class="icons">
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
/* Add your styles here */
</style>