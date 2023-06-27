package xyz.n8ify.logmaid.utils;

import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.model.ExtractProperty;
import xyz.n8ify.logmaid.storage.DataStorage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static xyz.n8ify.logmaid.constant.StringConstant.NEW_LINE;

public class LogExtractorUtil {
    private static final String OUTPUT_LOG_FILENAME_FORMAT = "%s_ExtractedLog.log";

    public static void proceed(ExtractProperty extractProperty, DataStorage dataStorage) throws IOException {

        /* Prepare file */
        File inputDir = new File(dataStorage.getInputLogDirPath());
        File outputDir = new File(dataStorage.getOutputLogDirPath());
        List<File> logFiles = getLogFiles(inputDir);

        if (extractProperty.isAdHocKeywordProvided()) {
            extractProperty.replaceInterestedKeywordToAdHocKeyword();
        }

        /* Iterate extract */
        for (String keyword : extractProperty.getInterestedKeywordList()) {

            if (keyword.isEmpty()) continue;

            File extractedFile = FileUtil.createFile(outputDir, String.format(OUTPUT_LOG_FILENAME_FORMAT, keyword));
            try (FileWriter writer = new FileWriter(extractedFile)) {
                for (File logFile : logFiles) {
                    final String logFileName = logFile.getName();
                    writer.write(StringUtil.generateHeaderString(logFileName));

                    List<String> lines = Files.readAllLines(logFile.toPath());
                    for (String line : lines) {
                        if (!extractProperty.isContainIgnoredKeyword(line) && line.contains(keyword)) {
                            writer.write(line);
                            writer.write(NEW_LINE);
                        }
                    }
                }
                writer.flush();
            }
        }

    }

    private static List<File> getLogFiles(File inputDir) {
        File[] files = inputDir.listFiles();
        if (files == null) {
            files = new File[0];
        }
        return Arrays.stream(files)
                .filter(it -> !it.isDirectory())
                .sorted(Comparator.comparing(File::lastModified))
                .toList();
    }

}
