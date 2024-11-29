<script setup>
import {ref} from 'vue';
import {millisecondsToTimeFormat} from "@/utils/timeUtils.js";
import PlayButtonSVG from "@/components/icons/PlayButtonSVG.vue";
import IconSupport from "@/components/icons/IconSupport.vue";

const props = defineProps({
  timer: Object,
});

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
</script>

<template>
  <div class="icons">
    <p>Time Elapsed: {{ millisecondsToTimeFormat(timer.trackedTime) }}</p>
    <i class="play-btn" @click="startTimer(timer)">
      <PlayButtonSVG/>
    </i>
    <i class="stop-btn" @click="stopTimer(timer)">
      <IconSupport/>
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