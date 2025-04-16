package controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Album;
import model.SoundClip;
import model.SoundClipBlockingQueue;
import model.SoundClipLoader;
import model.SoundClipPlayer;
import view.MusicOrganizerWindow;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	private Album root;
	
	public MusicOrganizerController() {

		root = new Album("All Sound Clips");

		// Create the blocking queue
		queue = new SoundClipBlockingQueue();
				
		// Create a separate thread for the sound clip player and start it
		
		(new Thread(new SoundClipPlayer(queue))).start();
	}
	
	/**
	 * Load the sound clips found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	public Set<SoundClip> loadSoundClips(String path) {
		Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);
		root.addSoundClips(clips);

		return clips;
	}
	
	public void registerView(MusicOrganizerWindow view) {
		this.view = view;
	}
	
	/**
	 * Returns the root album
	 */
	public Album getRootAlbum(){
		return root;
	}
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(){
		Album selectedAlbum = view.getSelectedAlbum();
		if (selectedAlbum == null) return;

		String newAlbumName = view.promptForAlbumName();
		if (newAlbumName == null) return;

		Album newAlbum = new Album(newAlbumName);
		selectedAlbum.addSubAlbum(newAlbum);
		view.onAlbumAdded(newAlbum);
	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(){
		Album selectedAlbum = view.getSelectedAlbum();
		if (selectedAlbum == null || selectedAlbum.getParent() == null) return;

		selectedAlbum.getParent().removeAlbum(selectedAlbum);
		view.onAlbumRemoved();
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(){
		Album selectedAlbum = view.getSelectedAlbum();
		if (selectedAlbum == null) return;

		List<SoundClip> selectedClips = view.getSelectedSoundClips();
		selectedAlbum.addSoundClips(new HashSet<SoundClip>(selectedClips));
	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(){
		// TODO Save activeAlbum instead of using selectedAlbum to remove sound clips?
		Album selectedAlbum = view.getSelectedAlbum();
		if (selectedAlbum == null) return;

		List<SoundClip> selectedClips = view.getSelectedSoundClips();
		selectedAlbum.removeSoundClips(new HashSet<SoundClip>(selectedClips));
		view.onClipsUpdated();
	}
	
	/**
	 * Puts the selected sound clips on the queue and lets
	 * the sound clip player thread play them. Essentially, when
	 * this method is called, the selected sound clips in the 
	 * SoundClipTable are played.
	 */
	public void playSoundClips(){
		List<SoundClip> l = view.getSelectedSoundClips();
		queue.enqueue(l);
		for(int i=0;i<l.size();i++) {
			view.displayMessage("Playing " + l.get(i));
		}
	}
}
