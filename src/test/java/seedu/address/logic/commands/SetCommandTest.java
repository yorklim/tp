package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;


public class SetCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SetCommand(null));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        SetCommand setCommand = new SetCommand(90);
        assertTrue(setCommand.equals(setCommand));
    }

    @Test
    public void equals_equalObjects_returnsTrue() {
        SetCommand setCommand1 = new SetCommand(90);
        SetCommand setCommand2 = new SetCommand(90);
        assertTrue(setCommand1.equals(setCommand2));
    }

    @Test
    public void equals_differentObjects_returnsFalse() {
        SetCommand setCommand1 = new SetCommand(90);
        SetCommand setCommand2 = new SetCommand(100);
        assertFalse(setCommand1.equals(setCommand2));
    }

}
