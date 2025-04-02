import java.io.File;

public class Main {



    public static void main(String[] args) {
        Album rootAlbum = new Album("Root");
        rootAlbum.name = "hello";

        System.out.println("LEN " + args.length);
        File file = new File("Music/Context Sensitive - 20XX - 01 Thermal.wav");
        SoundClip soundClip = new SoundClip(file);

        System.out.println("file: " + soundClip.toString());
        System.out.println("Hello, World!");
    }

}