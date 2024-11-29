import { ref } from "vue";
import {parseDuration} from "./../utils/timeUtils.js"
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
                console.log(res);
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

              console.log(task);
              return  task.projectId.value.toString() === project.id.toString()
            });

            const mappedTasks = projectTasks.map(task => {
                const taskTimeEntries = timeEntries.filter(entry => entry.taskId.taskId.toString() === task.id.toString());
                console.log("taskTimeEntries");
                console.log(taskTimeEntries);

                taskTimer.value.push({
                    taskId: task.taskId,
                    projectId: project.id,
                    timer: null,
                    isPlaying: false,
                    trackedTime: taskTimeEntries.reduce((sum, entry) => sum + parseDuration(entry.timePeriod.timePeriod), 0),
                });
//                 console.log("taskTimer");
// console.log(taskTimer);
                return {
                    ...task,
                    timeEntries: taskTimeEntries,
                    deadlineDate: formatDate(task.deadline.deadline),
                    deadlineTime: formatTime(task.deadline.deadline),
                };
            });

            const totalTrackedTime = mappedTasks.reduce((sum, task) => {
                return sum + task.timeEntries.reduce((timeSum, entry) => timeSum + parseDuration(entry.timePeriod.timePeriod), 0);
            }, 0);

            return {
                ...project,
                tasks: mappedTasks,
                deadlineDate: formatDate(project.deadline.deadline	),
                deadlineTime: formatTime(project.deadline.deadline	),
                total: totalTrackedTime,
            };
        });
    };

    // Hilfsfunktionen für Formatierungen
    const formatDate = dateStr =>
        new Date(dateStr).toLocaleDateString("de-DE", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
        });

    const formatTime = dateStr =>
        new Date(dateStr).toLocaleTimeString("de-DE", {
            hour: "2-digit",
            minute: "2-digit",
            hour12: false,
        });

    return { projects, taskTimer,fetchData, error };
}
