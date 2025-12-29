const DATE_FULL_REGEX = /^\d{4}-\d{2}-\d{2}$/u
const DATE_MONTH_REGEX = /^\d{4}-\d{2}$/u

/**
 * Parse a date string and return a Date object.
 * @param dateInput - The date string to parse.
 * @returns A Date object.
 */
export function parseScheduleDate(dateInput?: string): Date {
    if (!dateInput) {
        return new Date() // Today
    }

    // YYYY-MM-DD (full date)
    if (DATE_FULL_REGEX.test(dateInput)) {
        return new Date(`${dateInput}T00:00:00Z`)
    }

    // YYYY-MM (month)
    if (DATE_MONTH_REGEX.test(dateInput)) {
        return new Date(`${dateInput}-01T00:00:00Z`)
    }

    // ISO or valid date string
    const parsed = new Date(dateInput)
    if (!Number.isNaN(parsed.getTime())) {
        return parsed
    }

    throw new Error(`Invalid date format: ${dateInput}. Use YYYY-MM-DD or YYYY-MM`)
}

export interface ScheduleOptions {
    /** Date in 'YYYY-MM-DD', 'YYYY-MM', or ISO format. Defaults to today */
    date?: string
    /** Timezone offset in hours (e.g., 5.5 for IST). Defaults to UTC+0 */
    timezone?: number
}
