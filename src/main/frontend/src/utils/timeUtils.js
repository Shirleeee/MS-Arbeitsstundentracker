/**
 * Konvertiert Sekunden in ein Zeitformat (HH:MM:SS).
 * @param {number} seconds - Die Anzahl der Sekunden.
 * @returns {string} - Formatierte Zeit als HH:MM:SS.
 */
export function secondsToTimeFormat(seconds) {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secondsLeft = seconds % 60;

    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(secondsLeft).padStart(2, '0')}`;
}

export const parseDuration = (durationStr) => {
    try {
        // Prüfen, ob das Format mit "PT" beginnt
        if (durationStr.startsWith("PT")) {
            const match = durationStr.match(/PT(\d+(?:\.\d+)?)S/); // Extrahiert die Sekunden aus dem String
            if (match) {

                return parseInt(match[1]); // Gibt die Dauer in Sekunden als Zahl zurück
            } else {
                throw new Error("Ungültiges Duration-Format");
            }
        }
        // Falls kein "PT"-Format, einfach versuchen, die Zahl zu parsen
        return parseInt(durationStr);
    } catch (err) {
        console.error("Fehler beim Parsen der Duration:", err);
        return 0; // Standardwert bei Fehlern
    }
};
