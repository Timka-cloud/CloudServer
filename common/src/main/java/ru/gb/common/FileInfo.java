package ru.gb.common;

import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;


@Getter
@ToString
public class FileInfo implements Serializable {

    public enum FileType implements Serializable {

        FILE("F"), DIRECTORY("D");

        private String name;
        public String getName() {
            return name;
        }

        FileType(String name) {
            this.name = name;
        }
    }

    private final String fileName;
    private final String path;

    private final FileType fileType;

    private long size;

    public FileInfo(Path path) {
        try {
            this.path = path.toString();
            this.fileName = path.getFileName().toString();
            this.size = Files.size(path);
            this.fileType = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            if (this.fileType == FileType.DIRECTORY) {
                this.size = -1L;
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось собрать информацию о файле по пути " + path);
        }
    }

}
