package xyz.n8ify.logmaid.storage;

import xyz.n8ify.logmaid.utils.StringUtil;

import java.util.ArrayList;

public class DataStorage {

    public static DataStorage newInstance() {
        return new DataStorage();
    }

    private String inputLogDirPath = "";
    private String outputLogDirPath = "";

    private ArrayList<String> ignoredKeywords = new ArrayList<>();
    private ArrayList<String> adhocKeywords = new ArrayList<>();
    private ArrayList<String> interestedKeywords = new ArrayList<>();
    private ArrayList<String> groupedByThreadKeywords = new ArrayList<>();


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

    public ArrayList<String> getIgnoredKeywords() {
        return ignoredKeywords;
    }

    public void setIgnoredKeywords(ArrayList<String> ignoredKeywords) {
        this.ignoredKeywords = ignoredKeywords;
    }

    public ArrayList<String> getAdhocKeywords() {
        return adhocKeywords;
    }

    public void setAdhocKeywords(ArrayList<String> adhocKeywords) {
        this.adhocKeywords = adhocKeywords;
    }

    public ArrayList<String> getInterestedKeywords() {
        return interestedKeywords;
    }

    public void setInterestedKeywords(ArrayList<String> interestedKeywords) {
        this.interestedKeywords = interestedKeywords;
    }

    public ArrayList<String> getGroupedByThreadKeywords() {
        return groupedByThreadKeywords;
    }

    public void setGroupedByThreadKeywords(ArrayList<String> groupedByThreadKeywords) {
        this.groupedByThreadKeywords = groupedByThreadKeywords;
    }

    public boolean isInputOutputDirectoryProvide() {
        return StringUtil.isNotNullOrEmpty(this.inputLogDirPath) && StringUtil.isNotNullOrEmpty(this.outputLogDirPath);
    }

}
