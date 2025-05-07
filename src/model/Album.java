package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Album implements Serializable {
    public  Boolean isRemoved = false;
    private Album parent = null;
    private String name = "Default";
    private String description = "Description";
    private Set<Album> subAlbums = new HashSet<Album>();
    private Set<SoundClip> soundClips = new HashSet<SoundClip>();;

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
    public void removeSoundClips(Set<SoundClip> inSoundClips) {
        this.soundClips.removeAll(inSoundClips);
        for (Album album : subAlbums) {
            album.removeSoundClips(inSoundClips);
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
    public void removeAlbum(Album album) {
        if (subAlbums.contains(album))
        {
            album.removeSubAlbums();
            subAlbums.remove(album);
        }
    }

    // Remove all sub albums
    public void removeSubAlbums() {
        isRemoved = true;
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
    public Album getParent() { return parent; }
    public Set<SoundClip> getSoundClips() {return soundClips;}


    public void GenerateHTMLStructure(StringBuilder htmlContent) {
        htmlContent.append("<h2>").append(name).append("</h2>\n<ul>\n");
        for (SoundClip clip : soundClips) {
            htmlContent.append("<li>").append(clip.toString()).append("</li>\n");
        }

        for (Album subAlbum : subAlbums) {
            subAlbum.GenerateHTMLStructure(htmlContent);
        }
        htmlContent.append("</ul>\n");
    }

    public Set<Album> getSubAlbums() {
        return subAlbums;
    }
}
