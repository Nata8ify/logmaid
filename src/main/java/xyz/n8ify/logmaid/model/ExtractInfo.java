package xyz.n8ify.logmaid.model;

import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static xyz.n8ify.logmaid.constant.StringConstant.NEW_LINE;

public class ExtractInfo {

    private static ExtractInfo instance = null;

    public static void setInstance(Preset preset) {
        Preset.Config presetConfig = preset.getConfig();
        instance = new ExtractInfo(
                presetConfig != null ? presetConfig.getInputLogDirectory() : StringConstant.EMPTY,
                presetConfig != null ? presetConfig.getOutputLogDirectory() : StringConstant.EMPTY,
                presetConfig != null ? presetConfig.getInterestedKeyWordValues() : Collections.<String>emptyList(),
                presetConfig != null ? presetConfig.getAdhocKeyWordValues() : Collections.<String>emptyList(),
                presetConfig != null ? presetConfig.getIgnoredKeyWordValues() : Collections.<String>emptyList(),
                presetConfig != null ? presetConfig.getGroupedThreadKeyWordValues() : Collections.<String>emptyList());
    }

    public static void setInstance(ExtractInfo extractInfo) {
        instance = extractInfo;
    }

    public static ExtractInfo getInstance() {
        return instance;
    }

    private final String IGNORED_SIGN = "#";

    private String rawInterestedKeywordString;
    private String rawAdhocKeywordString;
    private String rawIgnoredKeywordString;
    private String rawGroupedThreadKeywordString;

    List<String> interestedKeyWordValues;
    List<String> adhocKeyWordValues;
    List<String> ignoredKeyWordValues;
    List<String> groupedThreadKeyWordValues;

    private String inputLogDirPath = "";
    private String outputLogDirPath = "";

    public ExtractInfo() {
        this.inputLogDirPath = StringConstant.EMPTY;
        this.outputLogDirPath = StringConstant.EMPTY;
        this.rawInterestedKeywordString = StringConstant.EMPTY;
        this.rawAdhocKeywordString = StringConstant.EMPTY;
        this.rawIgnoredKeywordString = StringConstant.EMPTY;
        this.rawGroupedThreadKeywordString = StringConstant.EMPTY;
        this.interestedKeyWordValues = Collections.emptyList();
        this.adhocKeyWordValues = Collections.emptyList();
        this.ignoredKeyWordValues = Collections.emptyList();
        this.groupedThreadKeyWordValues = Collections.emptyList();
    }

    public ExtractInfo(String inputLogDirPath, String outputLogDirPath, String rawInterestedKeywordString, String rawAdhocKeywordString, String rawIgnoredKeywordString, String rawGroupedThreadKeywordString) {
        this.inputLogDirPath = inputLogDirPath;
        this.outputLogDirPath = outputLogDirPath;
        this.rawInterestedKeywordString = rawInterestedKeywordString;
        this.rawAdhocKeywordString = rawAdhocKeywordString;
        this.rawIgnoredKeywordString = rawIgnoredKeywordString;
        this.rawGroupedThreadKeywordString = rawGroupedThreadKeywordString;
        this.interestedKeyWordValues = parseRawKeywordList(this.rawInterestedKeywordString);
        this.adhocKeyWordValues = parseRawKeywordList(this.rawAdhocKeywordString);
        this.ignoredKeyWordValues = parseRawKeywordList(this.rawIgnoredKeywordString);
        this.groupedThreadKeyWordValues = parseRawKeywordList(this.rawGroupedThreadKeywordString);
    }

    public ExtractInfo(String inputLogDirPath, String outputLogDirPath, List<String> interestedKeyWordValues, List<String> adhocKeyWordValues, List<String> ignoredKeyWordValues, List<String> groupedThreadKeyWordValues) {
        this.interestedKeyWordValues = interestedKeyWordValues;
        this.adhocKeyWordValues = adhocKeyWordValues;
        this.ignoredKeyWordValues = ignoredKeyWordValues;
        this.groupedThreadKeyWordValues = groupedThreadKeyWordValues;
        this.rawInterestedKeywordString = parseToRawKeyword(this.interestedKeyWordValues);
        this.rawAdhocKeywordString = parseToRawKeyword(this.adhocKeyWordValues);
        this.rawIgnoredKeywordString = parseToRawKeyword(this.ignoredKeyWordValues);
        this.rawGroupedThreadKeywordString = parseToRawKeyword(this.groupedThreadKeyWordValues);
        this.inputLogDirPath = inputLogDirPath;
        this.outputLogDirPath = outputLogDirPath;
    }

    public String getInputLogDirPath() {
        return inputLogDirPath;
    }

    public void setInputLogDirPath(String inputLogDirPath) {
        this.inputLogDirPath = inputLogDirPath;
    }

    public String getOutputLogDirPath() {
        return outputLogDirPath;
    }

    public void setOutputLogDirPath(String outputLogDirPath) {
        this.outputLogDirPath = outputLogDirPath;
    }

    public String getRawInterestedKeywordString() {
        return rawInterestedKeywordString;
    }

    public List<String> getInterestedKeywordList() {
        return this.interestedKeyWordValues;
    }

    public void setRawInterestedKeywordString(String rawInterestedKeywordString) {
        this.rawInterestedKeywordString = rawInterestedKeywordString;
    }

    public String getRawAdhocKeywordString() {
        return rawAdhocKeywordString;
    }

    public List<String> getAdhocKeywordList() {
        return this.adhocKeyWordValues;
    }

    public void setRawAdhocKeywordString(String rawAdhocKeywordString) {
        this.rawAdhocKeywordString = rawAdhocKeywordString;
    }

    public String getRawIgnoredKeywordString() {
        return rawIgnoredKeywordString;
    }

    public List<String> getIgnoredKeywordList() {
        return this.ignoredKeyWordValues;
    }

    public void setRawIgnoredKeywordString(String rawIgnoredKeywordString) {
        this.rawIgnoredKeywordString = rawIgnoredKeywordString;
    }

    public String getRawGroupedThreadKeywordString() {
        return rawGroupedThreadKeywordString;
    }

    public List<String> getGroupedThreadKeywordList() {
        return this.groupedThreadKeyWordValues;
    }

    public void setRawGroupedThreadKeywordString(String rawGroupedThreadKeywordString) {
        this.rawGroupedThreadKeywordString = rawGroupedThreadKeywordString;
    }

    public boolean isRequiredDataProvided() {
        return StringUtil.isNotNullOrEmpty(this.rawInterestedKeywordString) || StringUtil.isNotNullOrEmpty(this.rawAdhocKeywordString);
    }

    private List<String> parseRawKeywordList(String rawData) {
        return Stream.of(rawData.split(NEW_LINE))
                .filter(it -> !it.isEmpty())
                .filter(it -> !it.contains(IGNORED_SIGN))
                .collect(Collectors.toList());
    }

    private String parseToRawKeyword(List<String> data) {
        return String.join(NEW_LINE, data);
    }

    public boolean isAdHocKeywordProvided() {
        return StringUtil.isNotNullOrEmpty(this.rawAdhocKeywordString);
    }

    public void replaceInterestedKeywordToAdHocKeyword() {
        this.interestedKeyWordValues = this.adhocKeyWordValues;
    }

    public boolean isContainIgnoredKeyword(String value) {
        return this.ignoredKeyWordValues.stream().anyMatch(value::contains);
    }

    public boolean isInputOutputDirectoryProvide() {
        return StringUtil.isNotNullOrEmpty(this.inputLogDirPath) && StringUtil.isNotNullOrEmpty(this.outputLogDirPath);
    }

}
