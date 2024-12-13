import {ref} from "vue";
import {
    formatDate,
    formatTime,
    parseDurationToSeconds,
    convertDurationToDHMS
} from "./../utils/timeUtils.js";

// Custom Hook für Projekte, Tasks und Time Entries
export function useFetchProjects() {
    const projects = ref([]);
    const error = ref(null);
    const taskTimer = ref([]);

    // Fetch-Methode, um Daten abzurufen
    const fetchData = async () => {
        try {
            const [projectsRes, tasksRes, timeEntriesRes] = await Promise.all([
                fetch(`http://localhost:8081/api/project`).then(res => {
                    // console.log(res);
                    return res.text();  // Lese die Antwort als Text
                }),
                fetch(`http://localhost:8081/api/task`).then(res => {
                    //console.log(res);
                    return res.text();  // Lese die Antwort als Text
                }),
                fetch(`http://localhost:8081/api/time_entries`).then(res => {
                    //console.log(res);
                    return res.text();  // Lese die Antwort als Text
                }),
            ]);

             // console.log("Projects response:", projectsRes);
             // console.log("Tasks response:", tasksRes);
             // console.log("Time Entries response:", timeEntriesRes);


            const projectsData = JSON.parse(projectsRes);
            const tasksData = JSON.parse(tasksRes);
            const timeEntriesData = JSON.parse(timeEntriesRes);

            // Daten transformieren
            projects.value = mapProjects(projectsData, tasksData, timeEntriesData);

        } catch (err) {
            error.value = "Fehler beim Abrufen der Daten: " + err.message;
            console.error(error.value + " " + err);
        }

    };

    // Transformationslogik für Projekte, Tasks und Time Entries
    const mapProjects = (projects, tasks, timeEntries) => {
        return projects.map(project => {

            // console.log(project);
            const projectTasks = tasks.filter(task => {
              // console.log(task);
                return task.projectId.toString() === project.id.toString()
            });

            const mappedTasks = projectTasks.map(task => {

                const taskTimeEntries = timeEntries.filter(entry => {
                    // console.log("timeEntries entry.", entry);
                    return entry.taskId.toString() === task.task_id.toString();
                });

              // console.log("parseDurationToSeconds.parseDurationToSeconds", parseDurationToSeconds(taskTimeEntries.trackedTime));
                taskTimer.value.push({
                    task_id: task.task_id,
                    projectId: project.id,
                    timer: null,
                    isPlaying: false,
                    trackedTime: taskTimeEntries.reduce((sum, entry) => {
                        return sum + parseDurationToSeconds(entry.timePeriod.timePeriod);
                    }, 0),
                });

                return {
                    ...task,
                    timeEntries: taskTimeEntries,
                    deadlineDate: formatDate(task.deadline.deadline),
                    deadlineTime: formatTime(task.deadline.deadline),
                };
            });

            const totalTrackedTime = mappedTasks.reduce((sum, task) => {

                return sum + task.timeEntries.reduce((timeSum, entry) => timeSum + parseDurationToSeconds(entry.timePeriod.timePeriod), 0);
            }, 0);

            return {
                ...project,
                tasks: mappedTasks,
                deadlineDate: formatDate(project.deadline.deadline),
                deadlineTime: formatTime(project.deadline.deadline),
                total: totalTrackedTime,
            };
        });
    };


    return {projects, taskTimer, fetchData, error};
}
