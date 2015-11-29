package com.sqrt4.cubja;

import com.sqrt4.cubja.model.GameCubeImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by James on 28/07/2014.
 */
public class Test {
    private static final String path = "C:\\Users\\James\\Documents\\Star Fox Adventures.iso";

    public static void main(String[] args) throws IOException {
        new GameCubeImage(Files.newByteChannel(Paths.get(path), StandardOpenOption.READ));
    }
}