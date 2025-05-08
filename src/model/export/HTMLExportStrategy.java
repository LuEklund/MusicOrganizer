package model.export;

import Interface.ExportStrategy;
import model.Album;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLExportStrategy implements ExportStrategy {
    @Override
    public void export(Album rootAlbum, File file) throws Exception {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>" +
                "\n<html><head><title>Music Organizer</title>" +
                "</head><body>");
        rootAlbum.GenerateHTMLStructure(htmlContent);
        htmlContent.append("</body></html>");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(htmlContent.toString());
        }
    }
}
