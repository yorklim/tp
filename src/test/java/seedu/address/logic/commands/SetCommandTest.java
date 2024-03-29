package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.SetCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.LastMet;
import seedu.address.model.person.Person;
import java.util.ArrayList;
import java.util.List;

public class SetCommandTest {

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

    @Test
    public void toString_validCommand_returnsExpectedString() {
        SetCommand setCommand = new SetCommand(90);
        String expected = "SetCommand{overdueTimePeriod=90}";
        assertEquals(expected, setCommand.toString());
    }
}

