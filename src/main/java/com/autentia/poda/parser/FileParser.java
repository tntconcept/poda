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

public interface FileParser {

    /**
     * Method called for each file, before begin parsing the file.
     *
     * @param fileToParse file to be parsed.
     */
    void beforeParsingFile(FileMetadata fileToParse);

    /**
     * Method called for each file, after parsing the file.
     *
     * @param parsedFile parsed file.
     */
    void afterParsingFile(FileMetadata parsedFile);

}
