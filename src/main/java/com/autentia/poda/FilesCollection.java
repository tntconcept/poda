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
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FilesCollection implements Iterable<FileMetadata> {
    private static final Logger logger = LoggerFactory.getLogger(FilesCollection.class);

    private static final IOFileFilter NOT_SYMBOLIC_LINK = new AbstractFileFilter() {
        @Override
        public boolean accept(File file) {
            return !Files.isSymbolicLink(file.toPath());
        }
    };

    private Map<String, FileMetadata> filesByPath = Collections.emptyMap();
    private Map<String, List<FileMetadata>> filesByName = Collections.emptyMap();

    public FilesCollection scanDirectory(String path, boolean followSymbolicLinks, boolean parseHiddenFiles) {
        Collection<File> files = listFiles(path, followSymbolicLinks, parseHiddenFiles);
        filesByPath = new HashMap<>(files.size());
        filesByName = new HashMap<>(files.size() / 2);
        for (File scannedFile : files) {
            FileMetadata scannedFileMetadata = new FileMetadata(scannedFile);
            filesByPath.put(scannedFile.getPath(), scannedFileMetadata);
            putByName(scannedFileMetadata);
        }
        return this;
    }

    private Collection<File> listFiles(String path, boolean followSymbolicLinks, boolean parseHiddenFiles) {
        File directoryToScan = new File(path);
        logger.info("Searching files in directory: {}", directoryToScan.getAbsolutePath());

        IOFileFilter fileFilter = parseHiddenFiles ? TrueFileFilter.INSTANCE : HiddenFileFilter.VISIBLE;
        IOFileFilter dirFilter = followSymbolicLinks ? TrueFileFilter.INSTANCE : NOT_SYMBOLIC_LINK;
        dirFilter = andFiltersIgnoringTrueFilter(dirFilter, fileFilter);

        Collection<File> scannedFiles = FileUtils.listFiles(directoryToScan, fileFilter, dirFilter);

        logger.info("Found " + scannedFiles.size() + " files.");
        return scannedFiles;
    }

    private IOFileFilter andFiltersIgnoringTrueFilter(IOFileFilter filter1, IOFileFilter filter2) {
        if (filter1 == TrueFileFilter.INSTANCE) return filter2;
        if (filter2 == TrueFileFilter.INSTANCE) return filter1;
        return new AndFileFilter(filter1, filter2);
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
