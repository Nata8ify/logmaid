package xyz.n8ify.logmaid.utils;

import xyz.n8ify.logmaid.callback.LogCallback;
import xyz.n8ify.logmaid.enums.LogLevel;
import xyz.n8ify.logmaid.model.ExtractInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static xyz.n8ify.logmaid.constant.StringConstant.COMMA;
import static xyz.n8ify.logmaid.constant.StringConstant.NEW_LINE;

public class LogExtractorUtil {
    private static final String OUTPUT_LOG_FILENAME_FORMAT = "%s_ExtractedLog.log";

    public static void proceed(ExtractInfo extractInfo, LogCallback logCallback) throws IOException {

        /* Prepare file */
        File inputDir = new File(extractInfo.getInputLogDirPath());
        File outputDir = new File(extractInfo.getOutputLogDirPath());
        List<File> logFiles = getLogFiles(inputDir);
        ArrayList<File> extractedFileLists = new ArrayList<>();

        if (!inputDir.exists()) {
            logCallback.onLog(LogContentUtil.generate(LogLevel.ERROR, String.format("Input directory [%s] does not exists (Extraction abort)...", inputDir.getAbsolutePath())));
            return;
        }
        if (!outputDir.exists()) {
            logCallback.onLog(LogContentUtil.generate(LogLevel.ERROR, String.format("Output directory [%s] does not exists (Extraction abort)...", outputDir.getAbsolutePath())));
            return;
        }

        if (extractInfo.isAdHocKeywordProvided()) {
            logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Ad-hoc keyword is provided... Extract with ad-hoc keyword(s) [%s]", String.join(COMMA, extractInfo.getAdhocKeywordList()))));
            extractInfo.replaceInterestedKeywordToAdHocKeyword();
        }

        /* Iterate extract */
        for (String keyword : extractInfo.getInterestedKeywordList()) {

            if (keyword.isEmpty()) continue;

            File extractedFile = FileUtil.createFile(outputDir, String.format(OUTPUT_LOG_FILENAME_FORMAT, keyword));
            extractedFileLists.add(extractedFile);
            logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Create output extracted keyword file at [%s]", extractedFile.getAbsolutePath())));

            try (Writer writer = new BufferedWriter(new FileWriter(extractedFile))) {
                for (int i = 0; i < logFiles.size(); i++) {

                    final File logFile = logFiles.get(i);
                    final String logFileName = logFile.getName();
                    writer.append(StringUtil.generateHeaderString(logFileName));
                    logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Extracting keyword [%s] on log file [%s](%d of %d)... ", keyword, logFileName, i + 1, logFiles.size())));

                    List<String> lines = Files.readAllLines(logFile.toPath());
                    for (String line : lines) {
                        if (!extractInfo.isContainIgnoredKeyword(line) && line.contains(keyword)) {
                            writer.append(line).append(NEW_LINE);
                        }
                    }
                }
                writer.flush();
            }
            logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Extraction with keyword [%s] finished", keyword)));

            if (extractInfo.isGroupedThreadKeywordProvided()) {
                logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Start to extract keyword [%s] with grouped as single file", keyword)));
            }

        }
        logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, "Extraction completed"));

    }

    private static List<File> getLogFiles(File inputDir) {
        File[] files = inputDir.listFiles();
        if (files == null) {
            files = new File[0];
        }
        return Arrays.stream(files)
                .filter(it -> !it.isDirectory())
                .sorted(Comparator.comparing(file -> sumBytes(file.getName().getBytes())))
                .collect(Collectors.toList());
    }

    private static int sumBytes(byte[] bytes) {
        int result = 0;
        for (byte b : bytes) {
            result += b;
        }
        return result;
    }

}
