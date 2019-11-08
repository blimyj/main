package seedu.elisa.ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.elisa.commons.core.item.Event;
import seedu.elisa.commons.core.item.Item;

import java.time.format.DateTimeFormatter;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class EventListCard extends UiPart<Region> {

    private static final String FXML = "EventListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Item item;

    @FXML
    private HBox cardPane;
    @FXML
    private Label date;
    @FXML
    private Label description;
    @FXML
    private Label id;
    @FXML
    private Label priorityLabel;
    @FXML
    private Label time;

    public EventListCard(Item item, int displayedIndex) {
        super(FXML);
        this.item = item;
        id.setText(displayedIndex + ". ");
        description.setText(item.getItemDescription().toString());
        Event event = item.getEvent().get();
        date.setText(String.valueOf(event.getStartDateTime().getDayOfMonth())
                + " " + String.valueOf(event.getStartDateTime().getMonth()).substring(0, 3));
        time.setText(String.valueOf(event.getStartDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))));

        String priority = item.getPriority().toString();
        priorityLabel.setText(priority);
        priorityLabel.setAlignment(Pos.CENTER);
        priorityLabel.setPadding(new Insets(5, 10, 5, 10));
        switch(priority) {
        case "HIGH":
            priorityLabel.setStyle("-fx-font-family: 'Arial Black'; "
                    + "-fx-background-color: red; "
                    + "-fx-background-radius: 15, 15, 15, 15");
            break;
        case "MEDIUM":
            priorityLabel.setStyle("-fx-font-family: 'Arial Black'; "
                    + "-fx-background-color: orange; "
                    + "-fx-background-radius: 15, 15, 15, 15");
            priorityLabel.setText("MED");
            break;
        case "LOW":
            priorityLabel.setStyle("-fx-font-family: 'Arial Black'; "
                    + "-fx-background-color: green; "
                    + "-fx-background-radius: 15, 15, 15, 15");
            break;
        default:
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ItemCard)) {
            return false;
        }

        // state check
        EventListCard card = (EventListCard) other;
        return id.getText().equals(card.id.getText())
                && item.equals(card.item);
    }
}
