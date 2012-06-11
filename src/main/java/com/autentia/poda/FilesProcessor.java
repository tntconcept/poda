/**
 * Poda by Autentia Real Business Solution S.L.
 * Copyright (C) 2012 Autentia Real Business Solution S.L.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.autentia.poda;

import com.autentia.poda.parser.FileParser;
import com.autentia.poda.parser.LineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FilesProcessor.class);

    private boolean alreadyScanned = false;
    private final FilesCollection filesToInspect;
    private final List<FileParser> fileParsers;
    private final List<LineParser> lineParsers;

    public FilesProcessor(FilesCollection filesToInspect, List<FileParser> fileParsers, List<LineParser> lineParsers) {
        this.filesToInspect = filesToInspect;
        this.fileParsers = new ArrayList<>(fileParsers);
        this.lineParsers = new ArrayList<>(lineParsers);
    }

    public void parseFiles() {
        if (!alreadyScanned) {
            logger.info("Parsing files. This could take quite long, please be patient ...");
            for (FileMetadata fileToInspect : filesToInspect) {
                callParsersBeforeParseFile(fileToInspect);
                parseFile(fileToInspect);
                callParsersAfterParseFile(fileToInspect);
            }
            logger.info("All files parsed.");
            alreadyScanned = true;
        }
    }

    private void callParsersBeforeParseFile(FileMetadata fileToParse) {
        for (FileParser fileParser : fileParsers) {
            fileParser.beforeParsingFile(fileToParse);
        }
    }

    private void parseFile(FileMetadata fileToInspect) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileToInspect.getFile()))) {
            boolean continueParsing = true;
            String line;
            while (continueParsing && (line = in.readLine()) != null) {

                for (LineParser lineParser : lineParsers) {
                    continueParsing = lineParser.parseLine(fileToInspect, line);
                    if (!continueParsing) {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            logger.error("Cannot read file: " + fileToInspect.getFile().getAbsolutePath(), e);
        }
    }

    private void callParsersAfterParseFile(FileMetadata parsedFile) {
        for (FileParser fileParser : fileParsers) {
            fileParser.afterParsingFile(parsedFile);
        }
    }
}
