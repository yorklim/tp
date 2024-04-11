package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonComparatorTest {
    @Test
    public void getComparator() {
        // sort by name
        Person alice = new PersonBuilder().withName("Alice").build();
        Person aliceCopy = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        // ascending
        Comparator<Person> nameAscComparator = PersonComparator.getComparator(SortCriteria.NAME, SortOrder.ASC);
        assertTrue(nameAscComparator.compare(alice, bob) < 0);
        assertEquals(0, nameAscComparator.compare(alice, aliceCopy));
        // descending
        Comparator<Person> nameDescComparator = PersonComparator.getComparator(SortCriteria.NAME, SortOrder.DESC);
        assertTrue(nameDescComparator.compare(alice, bob) > 0);
        assertEquals(0, nameDescComparator.compare(alice, aliceCopy));

        // sort by priority
        Person priority1 = new PersonBuilder().withPriority("low").build();
        Person priority1Copy = new PersonBuilder().withPriority("low").build();
        Person priority2 = new PersonBuilder().withPriority("high").build();
        // ascending
        Comparator<Person> priorityAscComparator = PersonComparator.getComparator(SortCriteria.PRIORITY, SortOrder.ASC);
        assertTrue(priorityAscComparator.compare(priority1, priority2) < 0);
        assertEquals(0, priorityAscComparator.compare(priority1, priority1Copy));
        // descending
        Comparator<Person> priorityDescComparator = PersonComparator.getComparator(SortCriteria.PRIORITY,
                SortOrder.DESC);
        assertTrue(priorityDescComparator.compare(priority1, priority2) > 0);
        assertEquals(0, priorityDescComparator.compare(priority1, priority1Copy));

        // sort by birthday
        Person birthday1 = new PersonBuilder().withBirthday("2021-01-01").build();
        Person birthday1Copy = new PersonBuilder().withBirthday("2021-01-01").build();
        Person birthday2 = new PersonBuilder().withBirthday("2021-01-02").build();
        // ascending
        Comparator<Person> birthdayAscComparator = PersonComparator.getComparator(SortCriteria.BIRTHDAY, SortOrder.ASC);
        assertTrue(birthdayAscComparator.compare(birthday1, birthday2) < 0);
        assertEquals(0, birthdayAscComparator.compare(birthday1, birthday1Copy));
        // descending
        Comparator<Person> birthdayDescComparator = PersonComparator.getComparator(SortCriteria.BIRTHDAY,
                SortOrder.DESC);
        assertTrue(birthdayDescComparator.compare(birthday1, birthday2) > 0);
        assertEquals(0, birthdayDescComparator.compare(birthday1, birthday1Copy));

        // sort by invalid or default criteria
        Comparator<Person> invalidAscComparator = PersonComparator.getComparator(SortCriteria.INVALID, SortOrder.ASC);
        // ascending
        assertTrue(invalidAscComparator.compare(alice, bob) == 0);
        assertEquals(0, invalidAscComparator.compare(alice, aliceCopy));
        // descending
        assertTrue(invalidAscComparator.compare(alice, bob) == 0);
        assertEquals(0, invalidAscComparator.compare(alice, aliceCopy));
    }
}
