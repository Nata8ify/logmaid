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
    private static final List<String>  ALLOW_EXTENSIONS = Arrays.asList("log", "LOG", "txt", "TXT");

    public static void proceed(ExtractInfo extractInfo, LogCallback logCallback) throws IOException {

        /* Prepare file */
        File inputDir = new File(extractInfo.getInputLogDirPath());
        File outputDir = new File(extractInfo.getOutputLogDirPath());
        List<File> logFiles = getLogFiles(inputDir, logCallback);

        if (!inputDir.exists()) {
            logCallback.onLog(LogContentUtil.generate(LogLevel.ERROR, String.format("Input directory [%s] does not exists (Extraction abort)...", inputDir.getAbsolutePath())));
            return;
        }
        if (!outputDir.exists() && outputDir.mkdirs()) {
            logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Output directory [%s] does not exists and cannot be create (Extraction abort)...", outputDir.getAbsolutePath())));
        }

        if (extractInfo.isAdHocKeywordProvided()) {
            logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Ad-hoc keyword is provided... Extract with ad-hoc keyword(s) [%s]", String.join(COMMA, extractInfo.getAdhocKeywordList()))));
            extractInfo.replaceInterestedKeywordToAdHocKeyword();
        }

        /* Iterate extract */
        for (String keyword : extractInfo.getInterestedKeywordList()) {

            if (keyword.isEmpty()) continue;

            File extractedFile = FileUtil.createFile(outputDir, String.format(OUTPUT_LOG_FILENAME_FORMAT, keyword.trim()));
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

    private static List<File> getLogFiles(File inputDir, LogCallback logCallback) {
        File[] files = inputDir.listFiles();
        if (files == null) {
            files = new File[0];
        }
        return Arrays.stream(files)
                .filter(it -> !it.isDirectory())
                .filter(it -> ALLOW_EXTENSIONS.contains(it.getName().substring(it.getName().lastIndexOf(".") + 1)))
                .sorted(Comparator.comparing(file -> fileNameOnlyDigit(file.getName(), logCallback)))
                .collect(Collectors.toList());
    }

    private static int sumBytes(byte[] bytes) {
        int result = 0;
        for (byte b : bytes) {
            result += b;
        }
        return result;
    }

    private static long fileNameOnlyDigit(String fileName, LogCallback logCallback) {
        try {
            String replacementResult = fileName.replaceAll("\\.", "").replaceAll("\\D", "");
            return StringUtil.isNotNullOrEmpty(replacementResult) ? Long.parseLong(replacementResult) : Long.MAX_VALUE;
        } catch (Exception e) {
            logCallback.onLog(LogContentUtil.generate(LogLevel.ERROR, String.format("Parse file name for digit error, Use legacy logic for ordering... [Caused by %s]", e.getMessage())));
            return 0L;
        }
    }

}
