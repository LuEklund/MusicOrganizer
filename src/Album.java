import java.util.HashSet;
import java.util.Set;

public class Album {
    Album parent = null;
    String name = "Default";
    String description = "Description";
    Set<Album> subAlbums = new HashSet<Album>();
    Set<SoundClip> soundClips = new HashSet<SoundClip>();;

    public Album(String inName) {
        name = inName;
    }

    void addSoundClip(SoundClip soundClip) {
        soundClips.add(soundClip);
        if (parent != null)
            parent.addSoundClip(soundClip);
    }
    void removeSoundClip(SoundClip soundClip) {
        if (soundClips.contains(soundClip))
        {
            soundClips.remove(soundClip);
            for (Album album : subAlbums) {
                album.removeSoundClip(soundClip);
            }
        }

    }
    boolean containsSoundClip(SoundClip soundClip) {
        return soundClips.contains(soundClip);
    }

    void addSubAlbum(Album album) {
        album.parent = this;
        subAlbums.add(album);
    }
    void removeSubAlbum(Album album) {
        if (subAlbums.contains(album))
            subAlbums.remove(album);
    }
    boolean containsSubAlbum(Album album) {
        return subAlbums.contains(album);
    }

    void removeAlbum(Album album) {
        for (Album album : subAlbums) {
            album.removeSubAlbum();
            subAlbums.remove(album);
        }
    }
}
