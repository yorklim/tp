package seedu.address.model.person;

import static seedu.address.model.Model.COMPARATOR_SHOW_ORIGINAL_ORDER;

import java.util.Comparator;

/**
 * Comparator for comparing persons.
 */
public class PersonComparator {
    /**
     * Returns a comparator based on the sort criteria and sort order.
     *
     * @param sortCriteria the sort criteria
     * @param sortOrder the sort order
     * @return the comparator
     */
    public static Comparator<Person> getComparator(SortCriteria sortCriteria, SortOrder sortOrder) {
        Comparator<Person> comparator;
        switch (sortCriteria) {
        case NAME:
            comparator = Comparator.comparing(Person::getName);
            break;
        case PRIORITY:
            comparator = Comparator.comparing(Person::getPriority);
            break;
        case BIRTHDAY:
            comparator = Comparator.comparing(Person::getBirthday);
            break;
        default:
            return COMPARATOR_SHOW_ORIGINAL_ORDER;
        }
        if (sortOrder == SortOrder.DESC) {
            return comparator.reversed();
        }
        return comparator;
    }
}
