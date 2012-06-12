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

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.autentia.poda.TestEnvironment.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FilesCollectionTest {

    private static final File[] FILES_WITH_SAME_NAME = {
            new File(SRC_TEST_RESOURCES + "notReferenced.txt"),
            new File(SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt")};

    private static final File[] FILES_FOLLOWING_SYMBOLIC_LINKS = addToArray(FILES, new File(SRC_TEST_RESOURCES + "toLinkWithSymbolicLink/fileInsideSymbolicLinkDir.txt"));
    private static final File[] FILES_WITH_HIDDEN_FILES = addToArray(FILES, new File(SRC_TEST_RESOURCES + "/.htaccess"));

    private static File[] addToArray(File[] arrayWhereAdd, File... elementsToAdd) {
        List<File> list = new ArrayList<>(Arrays.asList(arrayWhereAdd));
        for (File element : elementsToAdd) {
            list.add(element);
        }
        return list.toArray(new File[list.size()]);
    }

    @Test
    public void scanAllFiles() throws Exception {
        assertFilesMetadata(FILES_TO_INSPECT.getAll(), FILES);
    }

    @Test
    public void scanAllFilesFollowingSymbolicLinks() throws Exception {
        FilesCollection filesFollowingSymbolicLinks = new FilesCollection().scanDirectory(SRC_TEST_RESOURCES, true, false);
        assertFilesMetadata(filesFollowingSymbolicLinks.getAll(), FILES_FOLLOWING_SYMBOLIC_LINKS);
    }

    @Test
    public void scanAllFilesWithHiddenFiles() throws Exception {
        FilesCollection filesFollowingSymbolicLinks = new FilesCollection().scanDirectory(SRC_TEST_RESOURCES, false, true);
        assertFilesMetadata(filesFollowingSymbolicLinks.getAll(), FILES_WITH_HIDDEN_FILES);
    }

    @Test
    public void getByPath() throws Exception {
        FileMetadata fileByPath = FILES_TO_INSPECT.getByPath(SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt");
        assertThat(fileByPath.getFile(), equalTo(new File(SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt")));
    }

    @Test
    public void getByName() throws Exception {
        Collection<FileMetadata> filesWithSameName = FILES_TO_INSPECT.getByName("notReferenced.txt");
        assertFilesMetadata(filesWithSameName, FILES_WITH_SAME_NAME);
    }

}
