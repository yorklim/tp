package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays detailed information of a {@code Person}.
 */
public class ClientDetailsCard extends UiPart<Region> {

    private static final String FXML = "ClientDetailsCard.fxml";

    public final Person person;

    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label birthday;
    @FXML
    private Label priority;
    @FXML
    private Label remark;
    @FXML
    private Label lastMet;
    @FXML
    private Label schedule;
    @FXML
    private FlowPane tags;

    /**
     * Creates an empty {@code ClientDetailsCard}.
     */
    public ClientDetailsCard() {
        super(FXML);
        this.person = null;
    }

    /**
     * Creates a {@code ClientDetailsCard} with the given {@code Person}.
     */
    public ClientDetailsCard(Person person) {
        super(FXML);
        this.person = person;
        name.setText(person.getName().fullName);
        phone.setText("Phone Number: " + person.getPhone().value);
        address.setText("Address: " + person.getAddress().value);
        email.setText("Email: " + person.getEmail().value);
        birthday.setText("Date of Birth: " + person.getBirthday().toString());
        priority.setText("Priority: " + person.getPriority().toString());
        remark.setText("Remarks: " + person.getRemark().value);
        lastMet.setText(person.getLastMet().showLastMet());
        schedule.setText(person.getSchedule().showSchedule());

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
