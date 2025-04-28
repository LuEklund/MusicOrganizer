package view;

import Interface.Listener;
import controller.MusicOrganizerController;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Album;

public class AlbumWindow implements Listener {
    private Album album;
    private MusicOrganizerController controller;
    private BorderPane bord = new BorderPane();
    private SoundClipListView soundClipTable = new SoundClipListView();;
    private Stage window = new Stage();



    public AlbumWindow(Album album, MusicOrganizerController controller) {
    this.album = album;
    this.controller = controller;
    window.setWidth(300);
    window.setHeight(600);


    bord.setCenter(soundClipTable);
    Scene scene = new Scene(bord);
    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

    window.setTitle(album.toString());
    window.setScene(scene);

    update();


    window.show();
    }
    public void update(){
        soundClipTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        soundClipTable.display(album);
        soundClipTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    // This code gets invoked whenever the user double clicks in the sound clip table
                    controller.playSoundClips(soundClipTable.getSelectedClips());
                }

            }

        });
        soundClipTable.display(album);
    };

    public boolean shouldDestroy() {
        return album.isRemoved;
    }
    public void destroy() {
        window.close();
    }
}

