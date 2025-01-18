import {ref} from "vue";
import {
    formatDate,
    formatTime,
    parseDurationToSeconds,
    convertDurationToDHMS
} from "./../utils/timeUtils.js";

// Custom Hook für Projekte, Tasks und Time Entries
export function useFetchProjects(userId) {
    const projects = ref([]);
    const error = ref(null);
    const taskTimer = ref([]);
    const timeEntries = ref([]);


    const fetchData = async () => {
        try {
            const [projectsRes, tasksRes, timeEntriesRes] = await Promise.all([
                fetch(`http://localhost:8081/api/project`).then(res => {
                    return res.text();
                }),
                fetch(`http://localhost:8081/api/task`).then(res => {
                    return res.text();
                }),
                fetch(`http://localhost:8081/api/time_entries`).then(res => {
                    return res.text();
                }),
            ]);


            const projectsData = JSON.parse(projectsRes);
            const tasksData = JSON.parse(tasksRes);
            const timeEntriesData = JSON.parse(timeEntriesRes);
            timeEntries.value = timeEntriesData;
            projects.value = mapProjects(projectsData, tasksData, timeEntriesData);

        } catch (err) {
            error.value = "Fehler beim Abrufen der Daten: " + err.message;
            console.error(error.value + " " + err);
        }

    };

    const mapProjects = (projects, tasks, timeEntries) => {

        projects = projects.filter(project => project.userId.toString() === userId.toString());

        return projects.map(project => {

            const projectTasks = tasks.filter(task => {
                return task.projectId.toString() === project.id.toString()
            });

            const mappedTasks = projectTasks.map(task => {


                const taskTimeEntries = timeEntries.filter(entry => {

                    return entry.taskId.toString() === task.task_id.toString();
                });


                taskTimer.value.push({
                    id: taskTimeEntries.length > 0 ? taskTimeEntries[0].id : null,
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


    return {projects, taskTimer, timeEntries, fetchData, error};
}
