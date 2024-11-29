/**
 * Konvertiert Sekunden in ein Zeitformat (HH:MM:SS).
 * @param {number} milliseconds - Die Anzahl der Sekunden.
 * @returns {string} - Formatierte Zeit als HH:MM:SS.
 */
export function millisecondsToTimeFormat(milliseconds) {
    // Berechne die Stunden, Minuten, Sekunden und verbleibenden Millisekunden
    const hours = Math.floor(milliseconds / 3600000); // 1 Stunde = 3600000 Millisekunden
    const minutes = Math.floor((milliseconds % 3600000) / 60000); // 1 Minute = 60000 Millisekunden
    const seconds = Math.floor((milliseconds % 60000) / 1000); // 1 Sekunde = 1000 Millisekunden
    const millisecondsLeft = milliseconds % 1000; // Die verbleibenden Millisekunden

    // Rückgabe im Format hh:mm:ss:SSS
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}:${String(millisecondsLeft).padStart(3, '0')}`;
}

export const parseDuration = (durationStr) => {

    try {
        // Prüfen, ob das Format mit "PT" beginnt
        if (durationStr.startsWith("PT")) {

                const durationInSeconds = parseFloat(durationStr.replace('PT', '').replace('S', ''));
                const miliseconds = durationInSeconds * 1000;
                console.log("miliseconds", miliseconds);
                return miliseconds;

        }

        // Falls kein "PT"-Format, einfach versuchen, die Zahl zu parsen
        return parseInt(durationStr);
    } catch (err) {
        console.error("Fehler beim Parsen der Duration:", err);
        return 0; // Standardwert bei Fehlern
    }
};


export const formatDateForBackend = (date) => {
    const d = new Date(date);
    return d.toISOString().split('T')[0] + 'T00:00:00';
};
