module MusicOrganizer {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;

    exports view;
    opens view to javafx.fxml;
}