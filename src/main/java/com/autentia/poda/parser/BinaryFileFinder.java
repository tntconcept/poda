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
