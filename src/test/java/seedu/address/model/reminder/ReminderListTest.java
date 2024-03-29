package seedu.address.model.reminder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.DateUtil;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;



public class ReminderListTest {
    @Test
    public void toStringMethodLastMet() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        Person person1 = new PersonBuilder().withLastMet(LocalDate.of(2001, 1, 1)).build();
        remindersList.add(person1);
        Person person2 = new PersonBuilder().withLastMet(LocalDate.of(2002, 2, 2)).build();
        remindersList.add(person2);
        ReminderList reminderList = new ReminderList(ReminderType.LAST_MET, remindersList);
        assertEquals(reminderList.toString(), person1.overdueLastMetStringFormat()
                + "\n" + person2.overdueLastMetStringFormat() + "\n");
    }

    @Test
    public void toStringMethodSchedule() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        Person person1 = new PersonBuilder().withSchedule(LocalDateTime.now().plusDays(10), false).build();
        remindersList.add(person1);
        Person person2 = new PersonBuilder().withSchedule(LocalDateTime.now().plusDays(5), false).build();
        remindersList.add(person2);
        ReminderList reminderList = new ReminderList(ReminderType.SCHEDULES, remindersList);
        assertEquals(reminderList.toString(), person1.scheduleStringFormat()
                + "\n" + person2.scheduleStringFormat() + "\n");
    }

    @Test
    public void toStringMethodBirthday() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        Person person1 = new PersonBuilder().withBirthday(DateUtil.parseDateToString(
                LocalDate.of(2001, 1, 1))).build();
        remindersList.add(person1);
        Person person2 = new PersonBuilder().withBirthday(DateUtil.parseDateToString(
                LocalDate.of(2002, 2, 2))).build();
        remindersList.add(person2);
        ReminderList reminderList = new ReminderList(ReminderType.BIRTHDAYS, remindersList);
        assertEquals(reminderList.toString(), person1.birthdayStringFormat()
                + "\n" + person2.birthdayStringFormat() + "\n");
    }

    @Test
    public void equals_sameObject_true() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        ReminderList reminderList = new ReminderList(ReminderType.LAST_MET, remindersList);
        assertEquals(reminderList, reminderList);
    }

    @Test
    public void equals_sameValues_true() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        ReminderList reminderList1 = new ReminderList(ReminderType.LAST_MET, remindersList);
        ReminderList reminderList2 = new ReminderList(ReminderType.LAST_MET, remindersList);
        assertEquals(reminderList1, reminderList2);
    }

    @Test
    public void equals_differentType_false() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        ReminderList reminderList = new ReminderList(ReminderType.LAST_MET, remindersList);
        assertEquals(reminderList.equals(1), false);
    }

    @Test
    public void equals_differentReminderType_false() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        ReminderList reminderList1 = new ReminderList(ReminderType.LAST_MET, remindersList);
        ReminderList reminderList2 = new ReminderList(ReminderType.SCHEDULES, remindersList);
        assertEquals(reminderList1.equals(reminderList2), false);
    }

    @Test
    public void equals_null_false() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        ReminderList reminderList = new ReminderList(ReminderType.LAST_MET, remindersList);
        assertEquals(reminderList.equals(null), false);
    }

    @Test
    public void equals_differentRemindersList_false() {
        ObservableList<Person> remindersList1 = FXCollections.observableArrayList();
        ObservableList<Person> remindersList2 = FXCollections.observableArrayList();
        remindersList2.add(ALICE);
        ReminderList reminderList1 = new ReminderList(ReminderType.LAST_MET, remindersList1);
        ReminderList reminderList2 = new ReminderList(ReminderType.LAST_MET, remindersList2);
        assertEquals(reminderList1.equals(reminderList2), false);
    }

    @Test
    public void equals_sameRemindersList_true() {
        ObservableList<Person> remindersList = FXCollections.observableArrayList();
        ReminderList reminderList1 = new ReminderList(ReminderType.LAST_MET, remindersList);
        ReminderList reminderList2 = new ReminderList(ReminderType.LAST_MET, remindersList);
        assertEquals(reminderList1.equals(reminderList2), true);
    }
}
