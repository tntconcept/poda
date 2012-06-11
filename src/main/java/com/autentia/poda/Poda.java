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
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Poda {

    private static final Logger logger = LoggerFactory.getLogger(Poda.class);

    public static void main(String... args) throws IOException {
        Poda poda = new Poda();

        JCommander jCommander = new JCommander(poda, args);
        jCommander.setProgramName(Poda.class.getSimpleName());

        if (poda.showUsage) {
            jCommander.usage();
            return;
        }

        poda.launchProcess();
    }

    @Parameter(names = {"-fsl", "--follow-symblinks"}, description = "Follow symbolic links.")
    private boolean followSymbolicLinks = false;

    @Parameter(description = "directory-to-scan")
    private List<String> commandLineArgs = new ArrayList<>();

    @Parameter(names = {"-h", "--help"}, description = "Show this help and exit.")
    private boolean showUsage = false;

    private final List<FileParser> fileParsers = new ArrayList<>();
    private final List<LineParser> lineParsers = new ArrayList<>();

    private void launchProcess() throws IOException {
        FilesCollection filesToInspect = searchFiles();

        configureParsers(filesToInspect);

        System.out.println("Parsing files. This could take quite long, please be patient ...");
        new FilesProcessor(filesToInspect, fileParsers, lineParsers).parseFiles();

        System.out.println();
        printGraph();
    }

    private FilesCollection searchFiles() throws IOException {
        String dirToScan = dirToScan();
        System.out.println("Searching files in directory: " + new File(dirToScan).getCanonicalPath() + " ...");
        FilesCollection filesToInspect = new FilesCollection().scanDirectory(dirToScan, followSymbolicLinks);
        System.out.println("Found " + filesToInspect.getAll().size() + " files.");
        return filesToInspect;
    }

    private String dirToScan() {
        if (commandLineArgs.isEmpty()) {
            return ".";
        }
        String dirToScan = commandLineArgs.get(0);

        File fileToScan = new File(dirToScan);
        if (!fileToScan.exists() || !fileToScan.isDirectory()) {
            System.err.println("ERROR - The specify path doesn't exist or is not a directory.");
            System.exit(-1);
        }

        return dirToScan;
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
        TextGraph textGraph = new TextGraph(rootOfTreesFinder.rootOfTrees());
        logger.info("\n{}", textGraph);
        logger.info("{}", fileParsers.get(1));
        System.out.println(textGraph);
    }

}
