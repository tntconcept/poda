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
import com.autentia.poda.parser.FileReferencesFinder;
import com.autentia.poda.parser.MockFileParser;
import com.autentia.poda.parser.RootOfTreesFinder;
import com.autentia.poda.parser.Statistician;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.*;

public class TestEnvironment {

    private static final Logger logger = LoggerFactory.getLogger(TestEnvironment.class);

//    public static final String SRC_TEST_RESOURCES = "src/test/resources/dummyFileSystem/";
    public static final String SRC_TEST_RESOURCES = "target/test-classes/dummyFileSystem/";

    static final File[] FILES = {
            new File(SRC_TEST_RESOURCES + "notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/main.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/Utils.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/model/Negocio.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/alejandropgarci.jpg"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/autentia.png"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/prohibidos-monos-lagartos-150x150.gif")};

    public static final File[] ROOT_OF_TREES = {
            new File(SRC_TEST_RESOURCES + "notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/main.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/autentia.png")};

    public static final File[] BINARY_FILES = {
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/alejandropgarci.jpg"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/autentia.png"),
            new File(SRC_TEST_RESOURCES + "com/autentia/resources/prohibidos-monos-lagartos-150x150.gif")};


    static final FilesCollection FILES_TO_INSPECT = new FilesCollection();

    public static final RootOfTreesFinder ROOT_OF_TREES_FINDER;

    public static final BinaryFileFinder BINARY_FILE_FINDER = new BinaryFileFinder();

    static final MockFileParser MOCK_FILE_PARSER = new MockFileParser();

    public static final Statistician STATISTICIAN;



    /**
     * This is not very orthodox, but such as integration tests can consume time and resources,
     * here is done the environment initialization; and then, on every test, the corresponding 'asserts' are done.
     */
    static {
       createSymbolicLink();

        FILES_TO_INSPECT.scanDirectory(SRC_TEST_RESOURCES, false, false);
        ROOT_OF_TREES_FINDER = new RootOfTreesFinder(FILES_TO_INSPECT);
        STATISTICIAN = new Statistician(FILES_TO_INSPECT.getAll().size());

        FilesProcessor filesProcessor = new FilesProcessor(
                FILES_TO_INSPECT,
                new ArrayList<>(Arrays.asList(ROOT_OF_TREES_FINDER, MOCK_FILE_PARSER, STATISTICIAN)),
                new ArrayList<>(Arrays.asList(BINARY_FILE_FINDER, new FileReferencesFinder(FILES_TO_INSPECT))));

        filesProcessor.parseFiles();
    }

    private static void createSymbolicLink() {
        Path link = Paths.get(SRC_TEST_RESOURCES + "toLinkWithSymbolicLink");
        try {
            Files.createSymbolicLink(link, Paths.get(SRC_TEST_RESOURCES + "../toLinkWithSymbolicLink").toRealPath());
        } catch (UnsupportedOperationException e) {
            logger.warn("Your environment doesn't support link creation. Be careful you can't test all features.");
        } catch (FileAlreadyExistsException e) {
            if (!Files.isSymbolicLink(link)) {
                fail(link + " should be a symbolic link.");
            }
        } catch (IOException e) {
            fail("Cannot create symbolic link: " + link);
        }
    }

    public static void assertFilesMetadata(Collection<FileMetadata> actualFilesMetadata, File... expectedFiles) {
        assertThat(actualFilesMetadata, hasSize(expectedFiles.length));
        for (FileMetadata actualFileMetadata : actualFilesMetadata) {
            assertThat(actualFileMetadata.getFile(), isIn(expectedFiles));
        }
    }

    private TestEnvironment() {
        // Utility class
    }
}
