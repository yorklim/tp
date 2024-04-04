package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;

import seedu.address.commons.util.DateUtil;

/**
 * Represents a Client's birthday in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBirthday(String)}
 */
public class Birthday implements Comparable<Birthday> {
    public static final String MESSAGE_CONSTRAINTS = DateUtil.getMessageConstraintsForDateType("Birthday")
            + " Birthday should not be in the future.";
    public final LocalDate date;

    /**
     * Constructs a {@code Birthday}.
     *
     * @param birthday A valid birthday.
     */
    public Birthday(String birthday) {
        requireNonNull(birthday);
        checkArgument(isValidBirthday(birthday), MESSAGE_CONSTRAINTS);
        this.date = DateUtil.parseStringToDate(birthday);
    }

    /**
     * Returns true if a given string is a valid birthday.
     */
    public static boolean isValidBirthday(String test) {
        if (!DateUtil.isValidDateString(test)) {
            return false;
        }
        LocalDate birthdayTest = DateUtil.parseStringToDate(test);
        if (DateUtil.isFutureDate(birthdayTest)) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the birthday is upcoming within at most 1 week's time and is not in the past.
     * @return true if the birthday is upcoming
     */
    public boolean isUpcoming() {
        int currentYear = LocalDate.now().getYear();
        LocalDate birthdayThisYear = date.withYear(currentYear);
        return !birthdayThisYear.isBefore(LocalDate.now())
                && !birthdayThisYear.isAfter(LocalDate.now().plusWeeks(1));
    }

    @Override
    public String toString() {
        return DateUtil.parseDateToString(date);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Birthday)) {
            return false;
        }

        Birthday otherBirthday = (Birthday) other;
        return date.equals(otherBirthday.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public int compareTo(Birthday other) {
        return date.compareTo(other.date);
    }

    /**
     * Compares the month and date of this birthday with another birthday.
     * Ignores the year.
     *
     * @param other The other birthday to compare with
     * @return A negative integer, zero, or a positive integer as this birthday is before, at the same time, or after
     */
    public int compareByBirthdayMonthAndDateOnly(Birthday other) {
        int currentYear = LocalDate.now().getYear();
        return date.withYear(currentYear).compareTo(other.date.withYear(currentYear));
    }
}
