package com.autentia.poda.parser;

import com.autentia.poda.FileMetadata;

public interface LineParser {

    /**
     * Method called for each line of the file.
     *
     * @param file parsed file.
     * @param line line of the parsed file.
     * @return true to continue parsing the line with the rest of the parsers. false to stop parsing this line.
     */
    boolean parseLine(FileMetadata file, String line);

}
