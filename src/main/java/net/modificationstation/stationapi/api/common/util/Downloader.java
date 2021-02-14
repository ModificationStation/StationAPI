package net.modificationstation.stationapi.api.common.util;

import net.modificationstation.stationapi.api.common.StationAPI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Downloader {

    /**
     * Downloads given URL to target path.
     * @param url URL to download.
     * @param path Path to save the file to.
     * @return false if no file was downloaded if it was meant to be, true if otherwise.
     */
    public static File downloadFile(URL url, File path, String filename, boolean replace) throws IOException {
        path.mkdirs();
        File file = new File(path + "/" + filename);
        if (file.exists() && !replace) {
            return file;
        }
        else {
            file.delete();
        }

        StationAPI.LOGGER.info("Downloading \"" + url + "\".");
        BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
        FileOutputStream fileOS = new FileOutputStream(file);
        byte[] data = new byte[1024];
        int byteContent;
        while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
            fileOS.write(data, 0, byteContent);
        }
        fileOS.close();
        return file;
    }
}
