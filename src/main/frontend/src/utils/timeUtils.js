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
export function formatDateToDDMMYYYY(datetimeStr) {
    const date = new Date(datetimeStr);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
    const year = date.getFullYear();
    return `${day}.${month}.${year}`;
}
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


export const formatDateForBackend = (date) => {
    const d = new Date(date);
    return d.toISOString().split('T')[0] + 'T00:00:00';
};
