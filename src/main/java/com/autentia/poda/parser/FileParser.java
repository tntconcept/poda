package com.autentia.poda.parser;

import com.autentia.poda.FileMetadata;

public interface FileParser {

    /**
     * Method called for each file, after parsing the file.
     *
     * @param parsedFile parsed file.
     */
    void afterParsingFile(FileMetadata parsedFile);

}
