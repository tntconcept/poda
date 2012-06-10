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
import java.util.List;

public class TextGraph {
    private final List<FileMetadata> rootOfTrees;
    private String toStringCache;

    public TextGraph(List<FileMetadata> rootOfTrees) {
        this.rootOfTrees = rootOfTrees;
    }

    @Override
    public String toString() {
        if (toStringCache == null) {
            StringBuilder textGraph = new StringBuilder();
            recursivePrint(textGraph, "", rootOfTrees);
            toStringCache = textGraph.toString();
        }
        return toStringCache;
    }

    private void recursivePrint(StringBuilder textGraph, String indentation, Collection<FileMetadata> files) {
        for (FileMetadata file : files) {
            textGraph.append(indentation).append(file.getPath());
            if (file.isBinary()) {
                textGraph.append("(B)");
            }
            textGraph.append(System.lineSeparator());

            recursivePrint(textGraph, incrementIndentation(indentation), file.references());
        }
    }

    private String incrementIndentation(String indentation) {
        return indentation + "    ";
    }
}
