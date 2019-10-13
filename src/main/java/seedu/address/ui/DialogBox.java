package seedu.address.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * This class is the Ui for DialogBox
 */
class DialogBox extends HBox {

    @FXML
    private Label dialog;

    private DialogBox(String text) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
    }

    /**
     * Flips the dialog box;
     */
    private void flip() {
        Node box = this.getChildren().get(0);
        ObservableList<Node> tmp;
        if (box instanceof HBox) {
            tmp = FXCollections.observableArrayList(((HBox) box).getChildren());
            Collections.reverse(tmp);
            HBox tmpBox = (HBox) box;
            tmpBox.getChildren().setAll(tmp);
            tmpBox.setAlignment(Pos.TOP_LEFT);
            setAlignment(Pos.TOP_LEFT);
        }
    }

    static DialogBox getUserDialog(String text) {
        return new DialogBox(text);
    }

    static DialogBox getElisaDialog(String text) {
        var db = new DialogBox(text);
        db.flip();
        return db;
    }
}