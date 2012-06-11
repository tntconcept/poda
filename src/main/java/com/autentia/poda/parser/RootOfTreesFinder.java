package com.autentia.poda.parser;

import com.autentia.poda.FileMetadata;
import com.autentia.poda.FilesCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RootOfTreesFinder implements FileParser {

    private final List<FileMetadata> roots = new ArrayList<>();

    public RootOfTreesFinder(FilesCollection filesToInspect) {
        roots.addAll(filesToInspect.getAll());
    }

    @Override
    public void afterParsingFile(FileMetadata parsedFile) {
        roots.removeAll(parsedFile.references());
    }

    public List<FileMetadata> rootOfTrees() {
        return Collections.unmodifiableList(roots);
    }

}
