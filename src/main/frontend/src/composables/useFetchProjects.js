import {ref} from "vue";
import {formatDate, formatTime, parseDurationToSeconds,} from "./../utils/timeUtils.js";


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
            // Daten transformieren
            projects.value = mapProjects(projectsData, tasksData, timeEntriesData, userId);

        } catch (err) {
            error.value = "Fehler beim Abrufen der Daten: " + err.message;
            console.error(error.value + " " + err);
        }

    };


    const mapProjects = (projects, tasks, timeEntries, userId) => {

        const userProjects = filterProjectsByUserId(projects, userId);

        return userProjects.map((project) => {
            const projectTasks = filterTasksByProjectId(tasks, project.id);
            const mappedTasks = projectTasks.map((task) => mapTask(task, project.id, timeEntries));

            const totalTrackedTime = calculateTotalTrackedTime(mappedTasks);

            return {
                ...project,
                tasks: mappedTasks,
                deadlineDate: formatDate(project.deadline.deadline),
                deadlineTime: formatTime(project.deadline.deadline),
                total: totalTrackedTime,
            };
        });
    };

    const filterProjectsByUserId = (projects, userId) =>
        projects.filter((project) => project.userId.toString() === userId.toString());

    const filterTasksByProjectId = (tasks, projectId) =>
        tasks.filter((task) => task.projectId.toString() === projectId.toString());

    const mapTask = (task, projectId, timeEntries) => {
        const taskTimeEntries = filterTimeEntriesByTaskId(timeEntries, task.task_id);
        const trackedTime = calculateTrackedTime(taskTimeEntries);

        taskTimer.value.push(createTaskTimer(task, projectId, taskTimeEntries, trackedTime));

        return {
            ...task,
            timeEntries: taskTimeEntries,
            deadlineDate: formatDate(task.deadline.deadline),
            deadlineTime: formatTime(task.deadline.deadline),
        };
    };

    const filterTimeEntriesByTaskId = (timeEntries, taskId) =>
        timeEntries.filter((entry) => entry.taskId.toString() === taskId.toString());

    const calculateTrackedTime = (timeEntries) =>
        timeEntries.reduce((sum, entry) => sum + parseDurationToSeconds(entry.timePeriod.timePeriod), 0);

    const createTaskTimer = (task, projectId, timeEntries, trackedTime) => ({
        id: timeEntries.length > 0 ? timeEntries[0].id : null,
        task_id: task.task_id,
        projectId: projectId,
        timer: null,
        isPlaying: false,
        trackedTime: trackedTime,
    });

    const calculateTotalTrackedTime = (tasks) =>
        tasks.reduce(
            (sum, task) =>
                sum +
                task.timeEntries.reduce(
                    (timeSum, entry) => timeSum + parseDurationToSeconds(entry.timePeriod.timePeriod),
                    0
                ),
            0
        );

    return {projects, taskTimer, timeEntries, fetchData, error};
}
