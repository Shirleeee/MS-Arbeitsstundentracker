import { ref } from "vue";

// Custom Hook für Projekte, Tasks und Time Entries
export function useFetchProjects() {
    const projects = ref([]);
    const error = ref(null);
    const taskTimer = ref([]);

    // Fetch-Methode, um Daten abzurufen
    const fetchData = async () => {
        try {
            const [projectsRes, tasksRes, timeEntriesRes] = await Promise.all([
                fetch("http://localhost:3000/projects").then(res => res.json()),
                fetch("http://localhost:3000/tasks").then(res => res.json()),
                fetch("http://localhost:3000/timeEntries").then(res => res.json()),
            ]);

            // Daten transformieren
            projects.value = mapProjects(projectsRes, tasksRes, timeEntriesRes);
        } catch (err) {
            error.value = "Fehler beim Abrufen der Daten: " + err.message;
            console.error(error.value);
        }
    };

    // Transformationslogik für Projekte, Tasks und Time Entries
    const mapProjects = (projects, tasks, timeEntries) => {
        return projects.map(project => {
            const projectTasks = tasks.filter(task => task.projectId.toString() === project.id.toString());

            const mappedTasks = projectTasks.map(task => {
                const taskTimeEntries = timeEntries.filter(entry => entry.taskId.toString() === task.id.toString());


                taskTimer.value.push({
                    taskId: task.id,
                    projectId: project.id,
                    timer: null,
                    isPlaying: false,
                    trackedTime: taskTimeEntries.reduce((sum, entry) => sum + entry.duration, 0),
                });

                return {
                    ...task,
                    timeEntries: taskTimeEntries,
                    deadlineDate: formatDate(task.deadline),
                    deadlineTime: formatTime(task.deadline),
                };
            });

            const totalTrackedTime = mappedTasks.reduce((sum, task) => {
                return sum + task.timeEntries.reduce((timeSum, entry) => timeSum + entry.duration, 0);
            }, 0);

            return {
                ...project,
                tasks: mappedTasks,
                deadlineDate: formatDate(project.deadline),
                deadlineTime: formatTime(project.deadline),
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