package xyz.n8ify.logmaid.enums;

public enum Widget {

    InterestedKeyWordTextArea("taInterestedKeyWord", "#taInterestedKeyWord"),
    AdhocKeyWordTextArea("taAdhocKeyWord", "#taAdhocKeyWord"),
    IgnoredKeyWordTextArea("taIgnoredKeyWord", "#taIgnoredKeyWord"),
    GroupedThreadKeyWordTextArea("taGroupedThreadKeyWord", "#taGroupedThreadKeyWord"),
    InputLogDirectoryTextField("tfInputLogDirectoryTextField", "#tfInputLogDirectoryTextField"),
    OutputLogDirectoryTextField("tfOutputLogDirectoryTextField", "#tfOutputLogDirectoryTextField"),
    LogTextArea("taLog", "#taLog"),
    PresetComboBox("cbPreset", "#cbPreset"),
    ;

    Widget(String id, String qualifiedId) {
        this.id = id;
        this.qualifiedId = qualifiedId;
    }

    private final String id;
    private final String qualifiedId;

    public String getId() {
        return id;
    }

    public String getQualifiedId() {
        return qualifiedId;
    }
}
