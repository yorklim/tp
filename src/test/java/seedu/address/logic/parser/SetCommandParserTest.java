package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class SetCommandParserTest {

    private final SetCommandParser parser = new SetCommandParser();

    @Test
    public void parse_validArgs_returnsSetCommand() throws ParseException {
        String args = "90";
        SetCommand expectedCommand = new SetCommand(90);

        assertEquals(expectedCommand, parser.parse(args));
    }

    @Test
    public void parse_missingArgs_throwsParseException() {
        // Missing arguments
        String args = "";
        assertThrows(ParseException.class, () -> parser.parse(args));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Invalid arguments
        String args = "invalid";
        assertThrows(ParseException.class, () -> parser.parse(args));
    }

    @Test
    public void parse_extraArgs_throwsParseException() {
        // Extra arguments
        String args = "90 extra";
        assertThrows(ParseException.class, () -> parser.parse(args));
    }
}
