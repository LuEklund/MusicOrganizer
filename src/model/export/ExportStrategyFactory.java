package model.export;

import Interface.ExportStrategy;

import java.io.File;

public class ExportStrategyFactory {
    public static ExportStrategy getExportStrategy(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".html")) {
            return new HTMLExportStrategy();
        } else if (fileName.endsWith(".ser")) {
            return new SerializedExportStrategy();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }
    }

}
