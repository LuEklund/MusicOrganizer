import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.*;

public class AlbumTest {
    private Album rootAlbum;
    private Album subAlbum1;
    private Album subAlbum2;
    private SoundClip clip1;
    private SoundClip clip2;

    @Before
    public void setUp() {
        rootAlbum = new Album("Root");
        subAlbum1 = new Album("Album-1");
        subAlbum2 = new Album("Album-2");


        clip1 = new SoundClip(new File("Music/Context Sensitive - 20XX - 01 Thermal.wav"));
        clip2 = new SoundClip(new File("Music/Context Sensitive - 20XX - 02 Fluoride.wav"));
    }

    // Validate default values
    @Test
    public void testConstructor() {
        Album album = new Album("Test Album");
        assertEquals("Test Album", album.name);
        assertNull(album.parent);
    }

    // add sound clip, should propagate to parent
    @Test
    public void testAddSoundClip() {
        rootAlbum.addSubAlbum(subAlbum1);

        subAlbum1.addSoundClip(clip1);
        assertTrue(subAlbum1.containsSoundClip(clip1));
        assertTrue(rootAlbum.containsSoundClip(clip1));
    }

    // remove sound clip, should not be removed from parent
    @Test
    public void testRemoveSoundClip() {
        rootAlbum.addSubAlbum(subAlbum1);

        subAlbum1.addSoundClip(clip1);
        subAlbum1.removeSoundClip(clip1);
        assertFalse(subAlbum1.containsSoundClip(clip1));
        assertTrue(rootAlbum.containsSoundClip(clip1));
    }

    // contains sound clip
    @Test
    public void testContainsSoundClip() {
        assertFalse(subAlbum1.containsSoundClip(clip1));
        subAlbum1.addSoundClip(clip1);
        assertTrue(subAlbum1.containsSoundClip(clip1));
    }

    // add sub album, album should be child of root AND sub album should have root as parent
    @Test
    public void testAddSubAlbum() {
        rootAlbum.addSubAlbum(subAlbum1);
        assertTrue(rootAlbum.containsSubAlbum(subAlbum1));
        assertNotNull(subAlbum1.parent);
    }

    // remove sub album
    @Test
    public void testRemoveSubAlbum() {
        rootAlbum.addSubAlbum(subAlbum1);
        rootAlbum.removeSubAlbum(subAlbum1);
        assertFalse(rootAlbum.containsSubAlbum(subAlbum1));
    }

    // contains sub album
    @Test
    public void testContainsSubAlbum() {
        assertFalse(rootAlbum.containsSubAlbum(subAlbum2));
        rootAlbum.addSubAlbum(subAlbum2);
        assertTrue(rootAlbum.containsSubAlbum(subAlbum2));
    }


    //BIG TEST
    @Test
    public void testSoundClipPropagation() {
        rootAlbum.addSubAlbum(subAlbum1);
        subAlbum1.addSubAlbum(subAlbum2);

        subAlbum2.addSoundClip(clip1);
        subAlbum1.addSoundClip(clip2);
        assertTrue(rootAlbum.soundClips.contains(clip1));
        assertTrue(subAlbum1.soundClips.contains(clip1));
        assertTrue(subAlbum2.soundClips.contains(clip1));
        assertTrue(rootAlbum.soundClips.contains(clip2));
        assertTrue(subAlbum1.soundClips.contains(clip2));
        assertFalse(subAlbum2.soundClips.contains(clip2));
    }

    @Test
    public void testSoundClipRemove() {
        rootAlbum.addSubAlbum(subAlbum1);
        subAlbum1.addSubAlbum(subAlbum2);

        subAlbum2.addSoundClip(clip1);
        subAlbum2.addSoundClip(clip2);
        rootAlbum.removeSoundClip(clip1);
        subAlbum1.removeSoundClip(clip2);
        assertFalse(rootAlbum.soundClips.contains(clip1));
        assertFalse(subAlbum1.soundClips.contains(clip1));
        assertFalse(subAlbum2.soundClips.contains(clip1));
        assertTrue(rootAlbum.soundClips.contains(clip2));
        assertFalse(subAlbum1.soundClips.contains(clip2));
        assertFalse(subAlbum2.soundClips.contains(clip2));
    }



}