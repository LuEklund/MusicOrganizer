package model;

import java.util.HashSet;
import java.util.Set;

public class Album {
    Album parent = null;
    String name = "Default";
    String description = "Description";
    Set<Album> subAlbums = new HashSet<Album>();
    Set<SoundClip> soundClips = new HashSet<SoundClip>();;

    // Constructors intialize name
    public Album(String inName) {
        name = inName;
    }

    // Add sound clip to album and propagate to parent
    void addSoundClip(SoundClip soundClip) {
        soundClips.add(soundClip);
        if (parent != null)
            parent.addSoundClip(soundClip);
    }

    public void addSoundClips(Set<SoundClip> inSoundClips) {
        Set<SoundClip> temp = new HashSet<SoundClip>(inSoundClips);
        this.soundClips.addAll(temp);
        if (parent != null)
            parent.addSoundClips(inSoundClips);
    }

    // Remove sound clip from album and propagate to each sub album
    void removeSoundClip(SoundClip soundClip) {
        if (soundClips.contains(soundClip))
        {
            soundClips.remove(soundClip);
            for (Album album : subAlbums) {
                album.removeSoundClip(soundClip);
            }
        }

    }

    //Check if album contains sound clip
    boolean containsSoundClip(SoundClip soundClip) {
        return soundClips.contains(soundClip);
    }

    // Add sub album to album and set parent to this album
   public void addSubAlbum(Album album) {
        album.parent = this;
        subAlbums.add(album);
    }

    // Remove sub album from album
    void removeAlbum(Album album) {
        if (subAlbums.contains(album))
        {
            album.removeSubAlbums();
            subAlbums.remove(album);
        }
    }

    // Remove all sub albums
    void removeSubAlbums() {
        for (Album album : subAlbums) {
            album.removeSubAlbums();
        }
        subAlbums.clear();
        soundClips.clear();
    }

    // Check if album contains sub album
    boolean containsSubAlbum(Album album) {
        return subAlbums.contains(album);
    }
    public String toString()
    {
        return name;
    }
}
