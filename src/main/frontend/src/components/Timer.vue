<script setup>
import { ref } from 'vue';
import { secondsToTimeFormat } from "@/utils/timeUtils.js";
import IconEcosystem from "@/components/icons/IconEcosystem.vue";
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
    <p>Time Elapsed: {{ secondsToTimeFormat(timer.trackedTime) }}</p>
    <i class="play-btn" @click="startTimer(timer)">
      <IconEcosystem />
    </i>
    <i class="stop-btn" @click="stopTimer(timer)">
      <IconSupport />
    </i>
  </div>
</template>