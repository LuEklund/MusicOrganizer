package Interface;

import model.Album;
import java.io.File;


public interface ExportStrategy {
    void export(Album rootAlbum, File file) throws Exception;
}
