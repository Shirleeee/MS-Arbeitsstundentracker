






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

// Hilfsfunktionen für Formatierungen
export   const formatDate = dateStr =>
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











export const parseDuration = (durationStr) => {


    if (!durationStr) {
        return 0;
    }
    try {
        if (durationStr.startsWith("PT")) {

            const durationInSeconds = parseFloat(durationStr.replace('PT', '').replace('S', ''));
            const miliseconds = durationInSeconds * 1000;
            console.log("miliseconds", miliseconds);
            return miliseconds;
        }
        return parseInt(durationStr);
    } catch (err) {
        console.error("Fehler beim Parsen der Duration:", err);
        return 0; // Standardwert bei Fehlern
    }
};


