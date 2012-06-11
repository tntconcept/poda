package com.autentia.poda.parser;

import com.autentia.poda.FileMetadata;
import com.autentia.poda.FilesCollection;

public class FileReferencesFinder implements LineParser {

    private final FilesCollection filesToSearch;

    public FileReferencesFinder(FilesCollection filesToSearch) {
        this.filesToSearch = filesToSearch;
    }

    @Override
    public boolean parseLine(FileMetadata file, String line) {
        filesReferencedIn(line, file);
        return true;
    }

    private void filesReferencedIn(String line, FileMetadata file) {
        for (FileMetadata fileToSearch : filesToSearch) {
            if (line.contains(fileToSearch.getName())) {
                file.addReference(fileToSearch);
            }
        }
    }
}
