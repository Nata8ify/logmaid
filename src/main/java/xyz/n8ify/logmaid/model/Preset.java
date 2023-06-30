package xyz.n8ify.logmaid.model;

import java.util.List;

public class Preset {

    private int id;
    private String name;
    private Config config;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public static class Config {

        private String inputLogDirectory;
        private String outputLogDirectory;
        private List<String> interestedKeyWordValues;
        private List<String> adhocKeyWordValues;
        private List<String> ignoredKeyWordValues;
        private List<String> groupedThreadKeyWordValues;

        public String getInputLogDirectory() {
            return inputLogDirectory;
        }

        public void setInputLogDirectory(String inputLogDirectory) {
            this.inputLogDirectory = inputLogDirectory;
        }

        public String getOutputLogDirectory() {
            return outputLogDirectory;
        }

        public void setOutputLogDirectory(String outputLogDirectory) {
            this.outputLogDirectory = outputLogDirectory;
        }

        public List<String> getInterestedKeyWordValues() {
            return interestedKeyWordValues;
        }

        public void setInterestedKeyWordValues(List<String> interestedKeyWordValues) {
            this.interestedKeyWordValues = interestedKeyWordValues;
        }

        public List<String> getAdhocKeyWordValues() {
            return adhocKeyWordValues;
        }

        public void setAdhocKeyWordValues(List<String> adhocKeyWordValues) {
            this.adhocKeyWordValues = adhocKeyWordValues;
        }

        public List<String> getIgnoredKeyWordValues() {
            return ignoredKeyWordValues;
        }

        public void setIgnoredKeyWordValues(List<String> ignoredKeyWordValues) {
            this.ignoredKeyWordValues = ignoredKeyWordValues;
        }

        public List<String> getGroupedThreadKeyWordValues() {
            return groupedThreadKeyWordValues;
        }

        public void setGroupedThreadKeyWordValues(List<String> groupedThreadKeyWordValues) {
            this.groupedThreadKeyWordValues = groupedThreadKeyWordValues;
        }
    }

}
