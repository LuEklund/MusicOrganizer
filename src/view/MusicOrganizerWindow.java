package view;
	
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Interface.Listener;
import Interface.Subject;
import controller.MusicOrganizerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Album;
import model.SoundClip;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;



public class MusicOrganizerWindow extends Application implements Subject {
	
	private BorderPane bord;
	private static MusicOrganizerController controller;
	private TreeItem<Album> rootNode;
	private TreeView<Album> tree;
	private ButtonPaneHBox buttons;
	private SoundClipListView soundClipTable;
	private TextArea messages;


	public static void main(String[] args) {
		controller = new MusicOrganizerController();
		if (args.length == 0) {
			controller.loadSoundClips("sample-sound");
		} else if (args.length == 1) {
			controller.loadSoundClips(args[0]);
		} else {
			System.err.println("too many command-line arguments");
			System.exit(0);
		}
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
				
		try {
			controller.registerView(this);
			primaryStage.setTitle("Music Organizer");
			
			bord = new BorderPane();

			VBox topContainer = new VBox();
			topContainer.getChildren().add(CreeateMenuBar());
			// Create buttons in the top of the GUI
			buttons = new ButtonPaneHBox(controller, this);
			topContainer.getChildren().add(buttons);
			bord.setTop(topContainer);

			// Create the tree in the left of the GUI
			tree = createTreeView();
			bord.setLeft(tree);
			
			// Create the list in the right of the GUI
			soundClipTable = createSoundClipListView();
			bord.setCenter(soundClipTable);
						
			// Create the text area in the bottom of the GUI
			bord.setBottom(createBottomTextArea());
			
			Scene scene = new Scene(bord);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					Platform.exit();
					System.exit(0);
					
				}
				
			});

			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	private MenuBar CreeateMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");

		MenuItem saveMenuItem = new MenuItem("Save as...");
		saveMenuItem.setOnAction(event -> openSaveDialog());
		fileMenu.getItems().add(saveMenuItem);

		MenuItem loadMenuItem = new MenuItem("Load Hierarchy");
		loadMenuItem.setOnAction(event -> openLoadDialog());
		fileMenu.getItems().add(loadMenuItem);

		menuBar.getMenus().add(fileMenu);
		return menuBar;
	}
	private void openSaveDialog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As...");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("HTML File", "*.html"),
				new FileChooser.ExtensionFilter("Serialized Object", "*.ser")
		);

		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			if (file.getName().endsWith(".html")) {
				exportToHTML(file);
			} else if (file.getName().endsWith(".ser")) {
				exportToObject(file);
			}

		}
	}

	private void exportToHTML(File file) {
		Album root = controller.getRootAlbum();
		StringBuilder htmlContent = new StringBuilder();
		htmlContent.append("<!DOCTYPE html>" +
				"\n<html><head><title>Music Organizer</title>" +
				"</head><body>");
		root.GenerateHTMLStructure(htmlContent);
		htmlContent.append("</body></html>");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(htmlContent.toString());
			displayMessage("Exported to HTML Successfully!");
		} catch (IOException e) {
			displayMessage("Error exporting to HTML: " + e.getMessage());
		}

	}
	private void exportToObject(File file) {
		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file))) {
			Album rootAlbum = controller.getRootAlbum();
			os.writeObject(rootAlbum);
			displayMessage("Hierarchy Exported Successfully!");
		} catch (IOException e) {
			displayMessage("Error Exporting Hierarchy: " + e.getMessage());
		}
	}

	private void openLoadDialog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load Hierarchy");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Serialized Object", "*.ser")
		);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			importFromSerializedObject(file);
		}

	}
	private void importFromSerializedObject(File file) {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			Album importedRoot = (Album) in.readObject();

			controller.getRootAlbum().getSubAlbums().clear();
			for (Album subAlbum : importedRoot.getSubAlbums()) {
				controller.getRootAlbum().addSubAlbum(subAlbum);
			}

			displayMessage("Loaded hierarchy successfully!");
			createTreeView();
//			onClipsUpdated();
		} catch (IOException | ClassNotFoundException e) {
			displayMessage("Error loading hierarchy: " + e.getMessage());
		}

	}

		private TreeView<Album> createTreeView(){
		rootNode = new TreeItem<>(controller.getRootAlbum());
		TreeView<Album> v = new TreeView<>(rootNode);
		
		v.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if(e.getClickCount()==2) {
					// This code gets invoked whenever the user double clicks in the TreeView
					onClipsUpdated();
				}
			}
		});
		return v;
	}
	
	private SoundClipListView createSoundClipListView() {
		SoundClipListView v = new SoundClipListView();
		v.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		v.display(controller.getRootAlbum());
		
		v.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if(e.getClickCount() == 2) {
					// This code gets invoked whenever the user double clicks in the sound clip table
					controller.playSoundClips();
				}
				
			}
			
		});
		
		return v;
	}
	
	private ScrollPane createBottomTextArea() {
		messages = new TextArea();
		messages.setPrefRowCount(3);
		messages.setWrapText(true);
		messages.prefWidthProperty().bind(bord.widthProperty());
		messages.setEditable(false); // don't allow user to edit this area
		
		// Wrap the TextArea in a ScrollPane, so that the user can scroll the 
		// text area up and down
		ScrollPane sp = new ScrollPane(messages);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		return sp;
	}
	
	/**
	 * Displays the message in the text area at the bottom of the GUI
	 * @param message the message to display
	 */
	public void displayMessage(String message) {
		messages.appendText(message + "\n");
	}
	
	public Album getSelectedAlbum() {
		TreeItem<Album> selectedItem = getSelectedTreeItem();
		return selectedItem == null ? null : selectedItem.getValue();
	}
	
	private TreeItem<Album> getSelectedTreeItem(){
		return tree.getSelectionModel().getSelectedItem();
	}
	
	
	
	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel
	 */
	public String promptForAlbumName() {
		TextInputDialog dialog = new TextInputDialog();
		
		dialog.setTitle("Enter album name");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the name for the album");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}
	
	/**
	 * Return all the sound clips currently selected in the clip table.
	 */
	public List<SoundClip> getSelectedSoundClips(){
		return soundClipTable.getSelectedClips();
	}
	
	
	
	/**
	 * *****************************************************************
	 * Methods to be called in response to events in the Music Organizer
	 * *****************************************************************
	 */	
	
	
	
	/**
	 * Updates the album hierarchy with a new album
	 * @param newAlbum
	 */
	public void onAlbumAdded(Album parent, Album newAlbum){
		TreeItem<Album> root = tree.getRoot();
		TreeItem<Album> parentNode = findAlbumNode(parent, root);

		parentNode.getChildren().add(new TreeItem<>(newAlbum));
		parentNode.setExpanded(true); // automatically expand the parent node in the tree
	}
	
	/**
	 * Updates the album hierarchy by removing an album from it
	 */
	public void onAlbumRemoved(Album toRemove){

		TreeItem<Album> root = tree.getRoot();

		TreeItem<Album> nodeToRemove = findAlbumNode(toRemove, root);
		nodeToRemove.getParent().getChildren().remove(nodeToRemove);

	}

	private TreeItem<Album> findAlbumNode(Album albumToFind, TreeItem<Album> root) {

		// recursive method to locate a node that contains a specific album in the TreeView

		if(root.getValue().equals(albumToFind)) {
			return root;
		}

		for(TreeItem<Album> node : root.getChildren()) {
			TreeItem<Album> item = findAlbumNode(albumToFind, node);
			if(item != null)
				return item;
		}

		return null;
	}
	
	/**
	 * Refreshes the clipTable in response to the event that clips have
	 * been modified in an album
	 */
	public void onClipsUpdated(){
		Album a = getSelectedAlbum();
		soundClipTable.display(a);
		updateisteners();
	}


	public void createNewWindow() {
		if (getSelectedAlbum() == null) return;
		Listeners.add(new AlbumWindow(getSelectedAlbum(), controller));
	}

	public void updateisteners() {
		List<Listener> windowsToRemove = new ArrayList<>();

		for(Listener listener : Listeners) {
			if (listener.shouldDestroy()) {
				listener.destroy();
				windowsToRemove.add(listener);
			}
			else
				listener.update();
		}
		Listeners.removeAll(windowsToRemove);
	}


}
