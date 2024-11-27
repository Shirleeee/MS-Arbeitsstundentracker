<script>


export default {
  data() {
    return {
      projects: [],
      tasks: [],
      timeEntries: [],
      text: 'Task',
      taskTimer: [],


    }
  },
  methods: {
    secondsToTimeFormat(seconds) {

      const hours = Math.floor(seconds / 3600);
      const minutes = Math.floor((seconds % 3600) / 60);
      const secondsLeft = seconds % 60;
      const formattedTime = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(secondsLeft).padStart(2, '0')}`;
         return formattedTime;
    },
    stopTimer(timeEntry) {

      console.log('timeEntry', timeEntry)
      clearInterval(timeEntry.timer)
      timeEntry.isPlaying = false;
    },
    startTimer(timeEntry) {
      console.log('timeEntry', timeEntry)
      timeEntry.timer = setInterval(() => {
        timeEntry.trackedTime += 1;
      }, 1000)
      timeEntry.isPlaying = true;

    }
  },
  mounted() {
    setTimeout(() => {
      console.log('timeElapsed', this.trackedTime)
    }, 2000)
    Promise.all([
      fetch("http://localhost:3000/projects").then(res => res.json()),
      fetch("http://localhost:3000/tasks").then(res => res.json()),
      fetch("http://localhost:3000/timeEntries").then(res => res.json()),
    ])
        .then(([projects, tasks, timeEntries]) => {
          // Map projects
          this.projects = projects.map(project => {
            const deadlineDate = new Date(project.deadline);
            const formattedDate = deadlineDate.toLocaleDateString("de-DE", {
              day: "2-digit",
              month: "2-digit",
              year: "numeric",
            });
            const formattedTime = deadlineDate.toLocaleTimeString("de-DE", {
              hour: "2-digit",
              minute: "2-digit",
              hour12: false,
            });
            let projectTotal = 0;
            // Filter tasks belonging to the project
            const projectTasks = tasks
                .filter(task => task.projectId.toString() === project.id.toString())
                .map(task => {
console.log('task', task)
                  const taskDeadlineDate = new Date(task.deadline);
                  return {
                    ...task,
                    deadlineDate: taskDeadlineDate.toLocaleDateString("de-DE", {
                      day: "2-digit",
                      month: "2-digit",
                      year: "numeric",
                    }),
                    deadlineTime: taskDeadlineDate.toLocaleTimeString("de-DE", {
                      hour: "2-digit",
                      minute: "2-digit",
                      hour12: false,
                    }),
                  };
                });

            // Add timeEntries to each task
            projectTasks.forEach(task => {
//push taskTimer object to taskTimer array for each task
              this.taskTimer.push({
                taskId: task.id,
                projectId: project.id,
                timer: null,
                isPlaying: false,
                trackedTime: 0
              })

              task.timeEntries = timeEntries.filter(
                  entry => entry.taskId.toString() === task.id.toString()
              ).map(entry => {


//find the taskTimer object for the task and add the duration to the trackedTime
                this.taskTimer.find((timer) => {
                  if (timer.taskId === task.id) {
                    timer.trackedTime += entry.duration;
                    if (timer.projectId === project.id) {
                      projectTotal += entry.duration; // Addiere die Dauer zum Gesamtwert des Projekts
                    }
                  }

                })

                const entryStartTimeDate = new Date(entry.startTime);
                const entryEndTimeDate = new Date(entry.endTime);

                return {
                  ...entry,
                  entryStartTimeDate: entryStartTimeDate.toLocaleDateString("de-DE", {
                    day: "2-digit",
                    month: "2-digit",
                    year: "numeric",
                  }),
                  entryStartTime: entryStartTimeDate.toLocaleTimeString("de-DE", {
                    hour: "2-digit",
                    minute: "2-digit",
                    hour12: false,
                  }),
                  entryEndTimeDate: entryEndTimeDate.toLocaleDateString("de-DE", {
                    day: "2-digit",
                    month: "2-digit",
                    year: "numeric",
                  }),
                  entryEndTime: entryEndTimeDate.toLocaleTimeString("de-DE", {
                    hour: "2-digit",
                    minute: "2-digit",
                    hour12: false,
                  }),
                };
              });
            });
            project.total = projectTotal;

            return {
              ...project,
              deadlineDate: formattedDate,
              deadlineTime: formattedTime,
              tasks: projectTasks, // Include tasks with their timeEntries
            };
          });
          console.log('this.projects', this.projects)
          console.log('this.tasks', this.tasks)
        })
        .catch(error => console.error("Error fetching data:", error.message));


  }
}
</script>
<script setup>
import IconEcosystem from "@/components/icons/IconEcosystem.vue";
import IconSupport from "@/components/icons/IconSupport.vue";

import Buttons from "@/components/Buttons.vue";
</script>

<template>
  <div v-for="project in projects" :key="project.id" class="project-container-item">


    <div class="proj-wrapper">
      <div class="proj-head">
        <h2 class="title">
          {{ project.id }}-
          {{ project.name }}
        </h2>
        <p class="deadline">Deadline Project: {{ project.deadlineDate }}</p>
        <p class="total-time">total: {{ secondsToTimeFormat(project.total) }} </p>
      </div>


      <ul v-for="task in project.tasks" :key="task.id" class="tasks">
        <li v-if="project.id.toString() === task.projectId.toString()" class="task">


          <p>Task: {{ task.name }}</p>
          <p>{{ task.time }}</p>
          <div>
            <p>taskId: {{ task.id }}</p>
            <p>Deadline date: {{ task.deadlineDate }}</p>
            <p>Deadline time: {{ task.deadlineTime }} Uhr</p>

          </div>


          <div v-for="taskT in this.taskTimer.filter(timer => task.id.toString() === timer.taskId.toString())" :key="task.id" class="icons-container">

            <div v-if="task.id.toString() === taskT.taskId.toString()" class="icons">
              <p>Time Elapsed: {{ secondsToTimeFormat(taskT.trackedTime ) }}</p>

              <i class="play-btn" @click="startTimer(taskT)">
                <IconEcosystem/>
              </i>
              <i class="stop-btn" @click="stopTimer(taskT)">
                <IconSupport/>
              </i>
            </div>
          </div>


        </li>

      </ul>

    </div>


    <Buttons :text="text"></Buttons>
  </div>
</template>

<style scoped>
.project-container-item {
  width: 100%;
  display: flex;
  flex-direction: column;
  font-size: 18px;

  & > .proj-wrapper {
    background-color: #31594a;
    padding: 40px 20px;
    border-radius: 10px;
    margin-bottom: 1rem;

    & > .proj-head {
      width: 100%;
      display: flex;
      padding-bottom: 20px;
      flex-direction: row;
      justify-content: space-between;
    }

    & > ul.tasks {
      width: 100%;
      display: flex;
      flex-direction: column;


      & > li.task {
        width: 100%;

        display: grid;
        grid-template-columns: 1fr 1fr 1fr 1fr;

        & > .icons-container .icons {
          & > * {
            padding-right: 5px;
            cursor: pointer;
          }
        }

      }
    }

  }
}

</style>
