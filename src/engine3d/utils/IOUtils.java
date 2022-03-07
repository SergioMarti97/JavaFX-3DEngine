package engine3d.utils;

import engine3d.mesh.ObjReader;

import java.io.InputStream;
import java.net.URISyntaxException;

public class IOUtils {

    /**
     * This method loads a file from the resource folder
     * @param fileName the path where is the object
     * @return an input stream with all the file data
     * @throws URISyntaxException if the file name is wrong
     */
    public static InputStream getFileFromResource(String fileName) throws URISyntaxException {
        InputStream resource = ObjReader.class.getResourceAsStream(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return resource;
        }
    }

}
