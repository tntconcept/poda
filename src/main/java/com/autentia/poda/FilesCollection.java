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

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FilesCollection implements Iterable<FileMetadata> {
    private static final Logger logger = LoggerFactory.getLogger(FilesCollection.class);

    private Map<String, FileMetadata> filesByPath = Collections.emptyMap();
    private Map<String, List<FileMetadata>> filesByName = Collections.emptyMap();

    public FilesCollection scanDirectory(String path) {
        File directoryToScan = new File(path);
        logger.info("Searching files in directory: {}", directoryToScan.getAbsolutePath());
        Collection<File> scannedFiles = FileUtils.listFiles(directoryToScan, null, true);
        logger.info("Found " + scannedFiles.size() + " files.");

        filesByPath = new HashMap<>(scannedFiles.size());
        filesByName = new HashMap<>(scannedFiles.size() / 2);
        for (File scannedFile : scannedFiles) {
            FileMetadata scannedFileMetadata = new FileMetadata(scannedFile);
            filesByPath.put(scannedFile.getPath(), scannedFileMetadata);
            putByName(scannedFileMetadata);
        }

        return this;
    }

    private void putByName(FileMetadata scannedFileMetadata) {
        List<FileMetadata> filesWithSameName = filesByName.get(scannedFileMetadata.getName());
        if (filesWithSameName == null) {
            filesWithSameName = new ArrayList<>();
            filesByName.put(scannedFileMetadata.getName(), filesWithSameName);
        }
        filesWithSameName.add(scannedFileMetadata);
    }

    @Override
    public Iterator<FileMetadata> iterator() {
        return Collections.unmodifiableCollection(filesByPath.values()).iterator();
    }

    public FileMetadata getByPath(String path) {
        return filesByPath.get(path);
    }

    public Collection<FileMetadata> getByName(String name) {
        return Collections.unmodifiableList(filesByName.get(name));
    }

    public Collection<FileMetadata> getAll() {
        return Collections.unmodifiableCollection(filesByPath.values());
    }
}
