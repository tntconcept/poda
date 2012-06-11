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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class TextGraph {
    private final List<FileMetadata> rootOfTrees;
    private String toStringCache = null;
    private int lineNumber = 0;
    private Map<String, Integer> visitedFilesInLine = new HashMap<>();

    public TextGraph(List<FileMetadata> rootOfTrees) {
        this.rootOfTrees = rootOfTrees;
    }

    @Override
    public String toString() {
        if (toStringCache == null) {
            StringBuilder textGraph = new StringBuilder();
            recursivePrint(textGraph, " - ", rootOfTrees);
            toStringCache = textGraph.toString();
        }
        return toStringCache;
    }

    private void recursivePrint(StringBuilder textGraph, String indentation, Collection<FileMetadata> files) {
        SortedSet<FileMetadata> sortedFiles = new TreeSet<>(files);
        for (FileMetadata file : sortedFiles) {
            lineNumber++;
            int previousLineNumber = putInVisitedFilesInLine(file);
            textGraph.append(lineNumber).append(indentation).append(file.toStringShortFormat());
            if (previousLineNumber == lineNumber) {
                textGraph.append(System.lineSeparator());
                recursivePrint(textGraph, incrementIndentation(indentation), file.references());
            } else {
                textGraph.append(" --> ").append(previousLineNumber).append(System.lineSeparator());
            }
        }
    }

    private int putInVisitedFilesInLine(FileMetadata file) {
        Integer visitedFileInLine = visitedFilesInLine.get(file.getPath());
        if (visitedFileInLine == null) {
            visitedFilesInLine.put(file.getPath(), Integer.valueOf(lineNumber));
            return lineNumber;
        }
        return visitedFileInLine.intValue();
    }

    private String incrementIndentation(String indentation) {
        return indentation + "    ";
    }
}
