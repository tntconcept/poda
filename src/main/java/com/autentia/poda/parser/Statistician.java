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

public class Statistician implements FileParser {
    private int filesCount = 0;
    private long startMillis = 0L;
    private long millisToProcessFilesSum = 0L;
    private final int totalFilesCount;
    private final ChangeValueListener changeValueListener;

    public interface ChangeValueListener {
        void changeValue(Statistician statistician);
    }

    public static final ChangeValueListener NULL_CHANGE_VALUE_LISTENER = new ChangeValueListener() {
        @Override
        public void changeValue(Statistician statistician) {
        }
    };

    public Statistician(int totalFilesCount) {
        this(totalFilesCount, NULL_CHANGE_VALUE_LISTENER);
    }

    public Statistician(int totalFilesCount, ChangeValueListener changeValueListener) {
        this.totalFilesCount = totalFilesCount;
        this.changeValueListener = changeValueListener;
    }

    @Override
    public void beforeParsingFile(FileMetadata fileToParse) {
        startMillis = System.currentTimeMillis();
    }

    @Override
    public void afterParsingFile(FileMetadata parsedFile) {
        long endMillis = System.currentTimeMillis();
        long millisToProcessFile = endMillis - startMillis;
        millisToProcessFilesSum += millisToProcessFile;
        filesCount++;
        changeValueListener.changeValue(this);
    }

    public int filesCount() {
        return filesCount;
    }

    public long averageMillisToProcessFiles() {
        return millisToProcessFilesSum / filesCount;
    }

    public int progressPercentage() {
        return filesCount * 100 / totalFilesCount;
    }

    public long approximateRemainingMillis() {
        return (long) averageMillisToProcessFiles() * (totalFilesCount - filesCount);
    }

    @Override
    public String toString() {
        return "Statistician{" +
                "files=" + filesCount + "/" + totalFilesCount +
                ", averageMillisToProcessFiles=" + averageMillisToProcessFiles() +
                ", progressPercentage=" + progressPercentage() +
                "%, remainingMillis=" + approximateRemainingMillis() + "}";
    }
}

