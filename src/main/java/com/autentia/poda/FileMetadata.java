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

import org.apache.commons.lang.Validate;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class FileMetadata {
    private final File originalFile;
    private boolean binary = false;
    private final Collection<FileMetadata> references = new HashSet<>();

    public FileMetadata(File originalFile) {
        Validate.notNull(originalFile, "Cannot create a file metadata with null original file");
        this.originalFile = originalFile;
    }

    public String getName() {
        return originalFile.getName();
    }

    public String getPath() {
        return originalFile.getPath();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FileMetadata that = (FileMetadata) obj;
        return getPath().equals(that.getPath());
    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    public boolean isBinary() {
        return binary;
    }

    public void addReference(FileMetadata reference) {
        references.add(reference);
    }

    public Collection<FileMetadata> references() {
        return Collections.unmodifiableCollection(references);
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder(getClass().getSimpleName() + "{file=").append(originalFile.getPath());
        if (binary) {
            toString.append("(B)");
        }
        if (!references.isEmpty()) {
            toString.append(", references=[");
            for (FileMetadata fileReferenced : references) {
                toString.append(fileReferenced.getPath()).append(", ");
            }
            toString.setLength(toString.length() - 2);
            toString.append("]}");
        }
        return toString.toString();
    }

    public File getFile() {
        return originalFile;
    }
}
