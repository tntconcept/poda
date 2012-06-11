package com.autentia.poda.parser;

import com.autentia.poda.FileMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryFileFinder implements LineParser {
    private final List<FileMetadata> binaryFiles = new ArrayList<>();

    @Override
    public boolean parseLine(FileMetadata file, String line) {
        if (isBinaryLine(line)) {
            file.setBinary(true);
            binaryFiles.add(file);
            return false;
        }
        return true;
    }

    private boolean isBinaryLine(String line) {
        int binaryCharactersCount = 0;
        for (int i = 0; i < line.length(); i++) {
            char it = line.charAt(i);
            if (!Character.isWhitespace(it) && Character.isISOControl(it)) {
                binaryCharactersCount++;
                if (binaryCharactersCount >= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<FileMetadata> binaryFiles() {
        return Collections.unmodifiableList(binaryFiles);
    }
}
