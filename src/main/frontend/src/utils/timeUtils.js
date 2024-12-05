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
    console.log("durationStr", durationStr);
    let durationInSeconds;
    if (durationStr.typeof === 'string') {

        durationInSeconds = parseFloat(durationStr.replace('00PT', '').replace('S', ''));
    } else {
        durationInSeconds = parseFloat(durationStr);
    }
    const milliseconds = durationInSeconds * 1000;
    console.log("milliseconds", milliseconds);
    // Calculate days, hours, and minutes
    const days = Math.floor(milliseconds / (24 * 60 * 60 * 1000));
    const hours = Math.floor((milliseconds % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000));
    const minutes = Math.floor((milliseconds % (60 * 60 * 1000)) / (60 * 1000));

    // Format the result as `dd:hh:mm`
    return `${String(days).padStart(2, '0')} Tage:${String(hours).padStart(2, '0')} Stunden:${String(minutes).padStart(2, '0')} Minuten`;
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
    console.log("duration", duration);
    if (!duration) {
        return 0;
    }
    const regex = /^P(?:(\d+)Y)?(?:(\d+)M)?(?:(\d+)D)?T?(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?$/;

    const matches = duration.match(regex);
console.log("matches", matches);

    if (matches) {
        const [_, years, months, days, hours, minutes, seconds] = matches;
        console.log(`Years: ${years || 0}`);
        console.log(`Months: ${months || 0}`);
        console.log(`Days: ${days || 0}`);
        console.log(`Hours: ${hours || 0}`);
        console.log(`Minutes: ${minutes || 0}`);
        console.log(`Seconds: ${seconds || 0}`);
    } else {
        console.log("Invalid duration format.");
    }

}


export const getBerlinDateTime = () => {
    const now = new Date();

    const berlinFormatter = new Intl.DateTimeFormat('de-DE', {
        timeZone: 'Europe/Berlin',
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
    });

    const parts = berlinFormatter.formatToParts(now);
    const year = parts.find((p) => p.type === 'year').value;
    const month = parts.find((p) => p.type === 'month').value;
    const day = parts.find((p) => p.type === 'day').value;
    const hour = parts.find((p) => p.type === 'hour').value;
    const minute = parts.find((p) => p.type === 'minute').value;
    const second = parts.find((p) => p.type === 'second').value;
    return `${year}-${month}-${day}T${hour}:${minute}:${second}`;
}
