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

import java.io.File;
import java.util.List;

public class Poda {

    public static void main(String[] args) {
        Poda poda = new Poda();
        String dirToScan = poda.parseCommandLineArguments(args);
        poda.printGraph(new FileParser(new FilesCollection().scanDirectory(dirToScan)).findRootOfTrees());
    }

    private void printGraph(List<FileMetadata> rootOfTrees) {
        System.out.println(new TextGraph(rootOfTrees));
    }

    private String parseCommandLineArguments(String[] args) {
        if (args.length == 0) {
            return ".";
        }
        String dirToScan = args[0];

        File fileToScan = new File(dirToScan);
        if (!fileToScan.exists() || !fileToScan.isDirectory()) {
            System.err.println("ERROR - The specify path doesn't exist or is not a directory.");
        }

        return dirToScan;
    }

}
