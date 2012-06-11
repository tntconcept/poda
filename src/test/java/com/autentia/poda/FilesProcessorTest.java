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

import static com.autentia.poda.TestCommons.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FilesProcessorTest {

    @Test
    public void findRootOfTrees() throws Exception {
        assertFilesMetadata(ROOT_OF_TREES_FINDER.rootOfTrees(), ROOT_OF_TREES);
    }

    @Test
    public void findBinaryFiles() throws Exception {
        assertFilesMetadata(BINARY_FILE_FINDER.binaryFiles(), BINARY_FILES);
    }

    @Test
    public void findTrees() throws Exception {
        List<FileMetadata> rootOfTrees = ROOT_OF_TREES_FINDER.rootOfTrees();
        for (FileMetadata root : rootOfTrees) {
            switch (root.getPath()) {
                case SRC_TEST_RESOURCES + "notReferenced.txt":
                    assertThat(root.references(), hasSize(1));
                    assertThat(root.references().iterator().next().getName(), equalTo("alejandropgarci.jpg"));
                    break;

                case SRC_TEST_RESOURCES + "com/autentia/notReferenced.txt":
                    assertTrue(root.references().isEmpty());
                    break;

                case SRC_TEST_RESOURCES + "com/autentia/main.txt":
                    assertThat(root.references(), hasSize(2));
                    for (FileMetadata reference : root.references()) {
                        switch (reference.getName()) {
                            case "Utils.txt":
                                assertTrue(reference.references().isEmpty());
                                break;

                            case "Negocio.txt":
                                assertThat(root.references(), hasSize(2));
                                for (FileMetadata reference2 : reference.references()) {
                                    assertThat(reference2.getName(), isIn(new ArrayList<>(Arrays.asList("Utils.txt", "prohibidos-monos-lagartos-150x150.gif"))));
                                    assertTrue(reference2.references().isEmpty());
                                }
                                break;

                            default:
                                fail();
                        }
                    }
                    break;

                case SRC_TEST_RESOURCES + "com/autentia/resources/autentia.png":
                    assertTrue(root.references().isEmpty());
                    break;
                default:
                    fail();
            }
        }
    }

}
