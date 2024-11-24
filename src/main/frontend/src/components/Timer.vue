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

<script>
import { secondsToTimeFormat } from "@/utils/timeUtils.js";
import IconEcosystem from "@/components/icons/IconEcosystem.vue";
import IconSupport from "@/components/icons/IconSupport.vue";

export default {
  props: {
    timer: Object,
  },
  methods: {
    startTimer(timer) {
      timer.timer = setInterval(() => {
        timer.trackedTime += 1;
      }, 1000);
      timer.isPlaying = true;
    },
    stopTimer(timer) {
      clearInterval(timer.timer);
      timer.isPlaying = false;
    },
    secondsToTimeFormat,
  },
  components: { IconEcosystem, IconSupport },
};
</script>
