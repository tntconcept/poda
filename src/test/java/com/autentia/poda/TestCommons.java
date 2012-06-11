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
import com.autentia.poda.parser.RootOfTreesFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class TestCommons {
    static final String SRC_TEST_RESOURCES = "src/test/resources/dummyFileSystem/";

    static final File[] FILES = {
            new File(SRC_TEST_RESOURCES + "notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/main.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/Utils.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/model/Negocio.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/alejandropgarci.jpg"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/autentia.png"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/prohibidos-monos-lagartos-150x150.gif")};

    static final File[] FILES_WITH_SAME_NAME = {
            new File(SRC_TEST_RESOURCES + "notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt")};

    static final File[] ROOT_OF_TREES = {
            new File(SRC_TEST_RESOURCES + "notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/main.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/autentia.png")};

    static final File[] BINARY_FILES = {
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/alejandropgarci.jpg"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/autentia.png"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/prohibidos-monos-lagartos-150x150.gif")};

    static final String EXPECTED_GRAPH =
            SRC_TEST_RESOURCES + "notReferenced.txt\n" +
            "    " + SRC_TEST_RESOURCES + "com/autentia/resources/alejandropgarci.jpg(B)\n" +
            SRC_TEST_RESOURCES + "com/autentia/resources/autentia.png(B)\n" +
            SRC_TEST_RESOURCES + "com/autentia/main.txt\n" +
            "    " + SRC_TEST_RESOURCES + "com/autentia/model/Negocio.txt\n" +
            "        " + SRC_TEST_RESOURCES + "com/autentia/resources/prohibidos-monos-lagartos-150x150.gif(B)\n" +
            "        " + SRC_TEST_RESOURCES + "com/autentia/Utils.txt\n" +
            "    " + SRC_TEST_RESOURCES + "com/autentia/Utils.txt\n" +
            SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt\n";

    static final FilesCollection files = new FilesCollection().scanDirectory(SRC_TEST_RESOURCES);

    static final RootOfTreesFinder ROOT_OF_TREES_FINDER = new RootOfTreesFinder(files);

    static final BinaryFileFinder BINARY_FILE_FINDER = new BinaryFileFinder();

    static final FilesProcessor FILES_PROCESSOR = new FilesProcessor(
            files,
            new ArrayList<>(Arrays.asList(new FileParser[] {ROOT_OF_TREES_FINDER})),
            new ArrayList<>(Arrays.asList(BINARY_FILE_FINDER, new FileReferencesFinder(files))));

    static {
        FILES_PROCESSOR.parseFiles();
    }

    private TestCommons() {
        // Utility class
    }
}
