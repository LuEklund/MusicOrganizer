package model.export;

import Interface.ExportStrategy;
import model.Album;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerializedExportStrategy implements ExportStrategy {
    @Override
    public void export(Album rootAlbum, File file) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(rootAlbum);
        }
    }

}
