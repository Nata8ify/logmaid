package xyz.n8ify.logmaid.utils;

import xyz.n8ify.logmaid.callback.LogCallback;
import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.enums.LogLevel;
import xyz.n8ify.logmaid.model.ExtractInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static xyz.n8ify.logmaid.constant.StringConstant.COMMA;
import static xyz.n8ify.logmaid.constant.StringConstant.NEW_LINE;

public class LogExtractorUtil {
    private static final String OUTPUT_LOG_FILENAME_SUFFIX = "ExtractedLog.log";
    private static final String OUTPUT_LOG_FILENAME_FORMAT = "%s_" + OUTPUT_LOG_FILENAME_SUFFIX;
    private static final String OUTPUT_LOG_GROUPED_FILENAME_SUFFIX = "ExtractedLog_Grouped.log";
    private static final String OUTPUT_LOG_GROUPED_FILENAME_FORMAT = "%s_" + OUTPUT_LOG_GROUPED_FILENAME_SUFFIX;
    private static final String DEFAULT_CLOSURE = "]";

    private static final List<String>  ALLOW_EXTENSIONS = Arrays.asList("log", "LOG", "txt", "TXT");
    private static final int MIN_GROUPED_KEYWORD_ALLOW_SIZE = 1;
    private static final int MAX_GROUPED_KEYWORD_ALLOW_SIZE = 100;

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
                groupedKeyword(extractedFile, extractInfo.getGroupedThreadKeywordList(), logCallback);
            }
            logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Extraction with keyword [%s] finished", keyword)));

            if (extractInfo.isGroupedThreadKeywordProvided()) {
                logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Start to extract keyword [%s] with grouped as single file", keyword)));
            }

        }
        logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, "Extraction completed"));

    }

    private static void groupedKeyword(File extractedFile, List<String> groupedThreadKeywordList, LogCallback logCallback) {
        if (groupedThreadKeywordList == null || groupedThreadKeywordList.isEmpty()) {
            logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, "Skip for grouping keyword"));
            return;
        }
        for (String groupedKeyword : groupedThreadKeywordList) {
            final File groupedKeywordFile = new File(extractedFile.getParent(), String.format(extractedFile.getName().replace(OUTPUT_LOG_FILENAME_SUFFIX, OUTPUT_LOG_GROUPED_FILENAME_FORMAT), groupedKeyword));
            final String[] groupedKeywordAndClosure = groupedKeyword.split(StringConstant.SPACE);
            final String keyword = groupedKeywordAndClosure[0];
            final String closure = getClosure(groupedKeywordAndClosure, logCallback);
            logCallback.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Start keyword grouping with \"%s\" and use \"%s\" as closure sign.", keyword, closure)));
            try {

                /* Prepare File */
                if (!groupedKeywordFile.exists()) {
                    if (!groupedKeywordFile.createNewFile()) {
                        throw new IOException("Something went wrong");
                    }
                }

                /* Grouped */
                final List<String> lines = Files.readAllLines(extractedFile.toPath());
                final Set<String> possibleGroupableKeywordSet = processAndGetGroupPossibleKeywords(lines, keyword, closure, logCallback);
                if (possibleGroupableKeywordSet.isEmpty()) {
                    logCallback.onLog(LogContentUtil.generate(LogLevel.WARN, String.format("No possible grouped keyword for extracting (Skip entire grouped keyword \"%s\")", possibleGroupableKeywordSet)));
                    if (groupedKeywordFile.delete()) {
                        logCallback.onLog(LogContentUtil.generate(LogLevel.WARN, String.format("[%s] file is deleted.", groupedKeywordFile.getName())));
                    }
                    continue;
                }
                try (Writer writer = new BufferedWriter(new FileWriter(groupedKeywordFile))) {
                    for (String possibleGroupableKeyword : possibleGroupableKeywordSet) {
                        final String header = String.format("[[%s]]", possibleGroupableKeyword);
                        writer.append(NEW_LINE).append(header);
                        lines.stream()
                                .filter(line -> line.contains(possibleGroupableKeyword))
                                .forEachOrdered(line -> {
                                    try { writer.append(NEW_LINE).append(line); }
                                    catch (IOException e) { throw new RuntimeException(e); }
                                });
                        writer.append(NEW_LINE);
                    }
                    writer.flush();
                }

            } catch (IOException e) {
                logCallback.onLog(LogContentUtil.generate(LogLevel.ERROR, String.format("Cannot create a grouped file by keyword [%s] at [%s]", groupedKeyword, groupedKeywordFile.getAbsolutePath())));
                logCallback.onLog(LogContentUtil.generate(LogLevel.ERROR, e.getMessage()));
            }
        }
    }

    private static Set<String> processAndGetGroupPossibleKeywords(List<String> lines, String keyword, String closure, LogCallback logCallback) {
        return lines.stream()
                .filter(line -> line.contains(keyword) && line.substring(line.indexOf(keyword)).contains(closure))
                .map(line -> {
                    final int indexOfKeyword = line.indexOf(keyword);
                    final int indexOfClosure = line.indexOf(closure, indexOfKeyword);
                    return line.substring(indexOfKeyword, indexOfClosure);
                })
                .filter(possibleKeyword -> {
                    boolean isPossibleKeywordMatchCriteria = possibleKeyword.trim().length() >= MIN_GROUPED_KEYWORD_ALLOW_SIZE && possibleKeyword.length() <= MAX_GROUPED_KEYWORD_ALLOW_SIZE;
                    if (!isPossibleKeywordMatchCriteria) {
                        logCallback.onLog(LogContentUtil.generate(LogLevel.WARN, String.format("\"%s\" was skipped for grouping keyword due to invalid calculated by closure output length [minimumLength=%d, maximumLength=%d, outputGroupedKeyword=%s]", keyword, MIN_GROUPED_KEYWORD_ALLOW_SIZE, MAX_GROUPED_KEYWORD_ALLOW_SIZE, possibleKeyword)));
                    }
                    return isPossibleKeywordMatchCriteria;
                })
                .collect(Collectors.toSet());
    }

    private static String getClosure(String[] groupedKeywordAndClosure, LogCallback logCallback) {
        if (groupedKeywordAndClosure.length == 2) {
            final String closure = groupedKeywordAndClosure[1];
            logCallback.onLog(LogContentUtil.generate(LogLevel.WARN, String.format("Use \"%s\" as closure", closure)));
            return closure;
        } else {
            logCallback.onLog(LogContentUtil.generate(LogLevel.WARN, String.format("Use default closure \"%s\"", DEFAULT_CLOSURE)));
            return DEFAULT_CLOSURE;
        }
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
