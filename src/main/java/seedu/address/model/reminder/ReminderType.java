package seedu.address.model.reminder;

/**
 * Represents the type of reminder.
 */
public enum ReminderType {
    LAST_MET,
    SCHEDULES,
    BIRTHDAYS;

    @Override
    public String toString() {
        switch(this) {
        case LAST_MET:
            return "Last Met";
        case SCHEDULES:
            return "Schedules";
        case BIRTHDAYS:
            return "Birthday Reminders";
        default:
            throw new IllegalArgumentException();
        }
    }
}
