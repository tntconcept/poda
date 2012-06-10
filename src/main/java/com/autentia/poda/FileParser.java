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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileParser {

    private static final Logger logger = LoggerFactory.getLogger(FileParser.class);

    private boolean alreadyScanned = false;
    private final FilesCollection filesToInspect;
    private List<FileMetadata> roots = new ArrayList<>();
    private List<FileMetadata> binaryFiles = new ArrayList<>() ;

    public FileParser(FilesCollection filesToInspect) {
        this.filesToInspect = filesToInspect;
    }

    public List<FileMetadata> findRootOfTrees() {
        scanFiles();
        return roots;
    }

    private void scanFiles() {
        if (!alreadyScanned) {
            parseFiles();
            convertResultToUnmodifiableCollections();

            alreadyScanned = true;
        }
    }

    private void parseFiles() {
        roots.addAll(filesToInspect.getAll());
        for (FileMetadata fileToInspect : filesToInspect) {
            filesReferencedIn(fileToInspect);
            roots.removeAll(fileToInspect.references());
            logger.debug("{}", fileToInspect);
        }
    }

    private void convertResultToUnmodifiableCollections() {
        roots = Collections.unmodifiableList(roots);
        binaryFiles = Collections.unmodifiableList(binaryFiles);
    }

    private void filesReferencedIn(FileMetadata fileToInspect) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileToInspect.getFile()))) {
            String line;
            while ((line = in.readLine()) != null) {

                if (!isTextLine(line)) {
                    fileToInspect.setBinary(true);
                    binaryFiles.add(fileToInspect);
                    break;
                }

                filesReferencedIn(line, fileToInspect);
            }

        } catch (IOException e) {
            logger.error("Cannot read file: " + fileToInspect.getFile().getAbsolutePath(), e);
        }
    }

    private boolean isTextLine(String line) {
        int binaryCharactersCount = 0;
        for (int i = 0; i < line.length(); i++) {
            char it = line.charAt(i);
            if (!Character.isWhitespace(it) && Character.isISOControl(it)) {
                binaryCharactersCount++;
                if (binaryCharactersCount >= 5) {
                    return false;
                }
            }
        }
        return true;
    }

    private void filesReferencedIn(String line, FileMetadata fileToInspect) {
        for (FileMetadata fileToSearch : filesToInspect) {
            if (line.contains(fileToSearch.getName())) {
                fileToInspect.addReference(fileToSearch);
            }
        }
    }

    public List<FileMetadata> findBinaryFiles() {
        scanFiles();
        return binaryFiles;
    }
}
