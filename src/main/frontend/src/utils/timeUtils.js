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