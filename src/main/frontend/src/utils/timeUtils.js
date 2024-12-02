export function secondsToTimeFormat(seconds) {


    if (isNaN(seconds)) {
        return '00:00:00';
    }
    const hours = Math.floor(seconds / 3600); // 1 Stunde = 3600 Sekunden
    const minutes = Math.floor((seconds % 3600) / 60); // 1 Minute = 60 Sekunden
    const remainingSeconds = seconds % 60; // Verbleibende Sekunden

    // Rückgabe im Format hh:mm:ss
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`;
}


export function convertDurationToDHMS(durationStr) {
    if (!durationStr) {
        return '00:00:00';
    }

    // Extract milliseconds from the duration string
    const durationInSeconds = parseFloat(durationStr.replace('00PT', '').replace('S', ''));
    const milliseconds = durationInSeconds * 1000;

    // Calculate days, hours, and minutes
    const days = Math.floor(milliseconds / (24 * 60 * 60 * 1000));
    const hours = Math.floor((milliseconds % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000));
    const minutes = Math.floor((milliseconds % (60 * 60 * 1000)) / (60 * 1000));

    // Format the result as `dd:hh:mm`
    return `${String(days).padStart(2, '0')}:${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
}


export const parseDateTimeLocal = (dateStr) => {
    const [datePart, timePart] = dateStr.split('T');
    const [year, month, day] = datePart.split('-').map(Number);
    const [hour, minute] = timePart.split(':').map(Number);

    // Erstelle ein Datum ohne Zeitzonen-Offset
    return new Date(year, month - 1, day, hour, minute);
};


export const formatDate = dateStr =>
    new Date(dateStr).toLocaleDateString("de-DE", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    });

export const formatTime = (dateStr) => {
    const localDate = parseDateTimeLocal(dateStr);
    return localDate.toLocaleTimeString("de-DE", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
    });
};

export const parseDurationToSeconds = (duration) => {

    if (!duration) {
        return 0;
    }
    const regex = /PT(\d+(\.\d+)?)S/;
    const match = duration.match(regex);

    if (match) {
        return Math.round(parseFloat(match[1])); // Gibt die Sekunden als Fließkommazahl zurück
    } else {
        throw new Error('Ungültiges Format der Dauer');
    }
}


