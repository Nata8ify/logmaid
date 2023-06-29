package xyz.n8ify.logmaid.component;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import xyz.n8ify.logmaid.BaseApplication;
import xyz.n8ify.logmaid.constant.CommonConstant;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.constant.UIConstant;
import xyz.n8ify.logmaid.enums.Widget;
import xyz.n8ify.logmaid.fatory.control.DefaultTabFactory;
import xyz.n8ify.logmaid.fatory.control.DefaultTextAreaFactory;

public class CenterComponent extends AbstractComponent {

    public static VBox init(BaseApplication application) {
        VBox container = new VBox(UIConstant.SM_INSET);
        container.setPadding(new Insets(UIConstant.M_INSET, 0, UIConstant.M_INSET, 0));

        TabPane tp = new TabPane();
        tp.getTabs().addAll(
                initInterestedKeywordTab(),
                initAdhocKeywordTab(),
                initIgnoredKeywordTab(),
                initGroupedThreadKeywordTab()
        );

        TextArea taLog = DefaultTextAreaFactory.newInstance(Widget.LogTextArea, StringConstant.BROWSE);
        taLog.setEditable(false);

        container.getChildren().addAll(
                tp,
                taLog);
        return container;
    }

    private static Tab initInterestedKeywordTab() {
        TextArea ta = DefaultTextAreaFactory.newInstance(Widget.InterestedKeyWordTextArea, LabelConstant.INTERESTED_KEYWORD_INPUT_HINT);
        return DefaultTabFactory.newInstance(LabelConstant.INTERESTED_KEYWORD_TAB_LABEL, ta);
    }

    private static Tab initAdhocKeywordTab() {
        TextArea ta = DefaultTextAreaFactory.newInstance(Widget.AdhocKeyWordTextArea, LabelConstant.ADHOC_KEYWORD_INPUT_HINT);
        return DefaultTabFactory.newInstance(LabelConstant.ADHOC_KEYWORD_TAB_LABEL, ta);
    }

    private static Tab initIgnoredKeywordTab() {
        TextArea ta = DefaultTextAreaFactory.newInstance(Widget.IgnoredKeyWordTextArea, LabelConstant.IGNORED_KEYWORD_INPUT_HINT);
        return DefaultTabFactory.newInstance(LabelConstant.IGNORED_KEYWORD_TAB_LABEL, ta);
    }

    private static Tab initGroupedThreadKeywordTab() {
        TextArea ta = DefaultTextAreaFactory.newInstance(Widget.GroupedThreadKeyWordTextArea, LabelConstant.GROUPED_THREAD_INPUT_HINT);
        return DefaultTabFactory.newInstance(LabelConstant.GROUPED_THREAD_TAB_LABEL, ta);
    }

}
