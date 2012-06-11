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

import com.autentia.poda.parser.BinaryFileFinder;
import com.autentia.poda.parser.FileParser;
import com.autentia.poda.parser.FileReferencesFinder;
import com.autentia.poda.parser.LineParser;
import com.autentia.poda.parser.RootOfTreesFinder;
import com.autentia.poda.parser.Statistician;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Poda {

    private final List<FileParser> fileParsers = new ArrayList<>();
    private final List<LineParser> lineParsers = new ArrayList<>();
    private final String dirToScan;

    public static void main(String... args) throws IOException {
        String dirToScan = parseCommandLineArguments(args);
        new Poda(dirToScan).launchProcess();
    }

    private static String parseCommandLineArguments(String... args) {
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

    public Poda(String dirToScan) {
        this.dirToScan = dirToScan;
    }

    private void launchProcess() throws IOException {
        FilesCollection filesToInspect = searchFiles();

        configureParsers(filesToInspect);

        System.out.println("Parsing files. This could take quite long, please be patient ...");
        new FilesProcessor(filesToInspect, fileParsers, lineParsers).parseFiles();

        System.out.println();
        printGraph();
    }

    private FilesCollection searchFiles() throws IOException {
        System.out.println("Searching files in directory: " + new File(dirToScan).getCanonicalPath() + " ...");
        FilesCollection filesToInspect = new FilesCollection().scanDirectory(dirToScan);
        System.out.println("Found " + filesToInspect.getAll().size() + " files.");
        return filesToInspect;
    }

    private void configureParsers(FilesCollection filesToInspect) {
        fileParsers.add(new RootOfTreesFinder(filesToInspect));
        fileParsers.add(new Statistician(filesToInspect.getAll().size(), new Statistician.ChangeValueListener() {
            @Override
            public void changeValue(Statistician statistician) {
                System.out.print(statistician + "                            \r");
            }
        }));

        lineParsers.add(new BinaryFileFinder());
        lineParsers.add(new FileReferencesFinder(filesToInspect));
    }

    private void printGraph() {
        RootOfTreesFinder rootOfTreesFinder = (RootOfTreesFinder) fileParsers.get(0);
        System.out.println(new TextGraph(rootOfTreesFinder.rootOfTrees()));
    }

}
