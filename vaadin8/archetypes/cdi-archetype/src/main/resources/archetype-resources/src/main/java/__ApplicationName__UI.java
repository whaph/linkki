package \${package};

import javax.inject.Inject;

import org.linkki.framework.ui.application.LinkkiUi;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDINavigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.UI;

/**
 * Base class for {@link UI} implementations.
 */
@SuppressWarnings("serial")
@Theme("\${theme-name}")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
@CDIUI(\${ApplicationName}UI.URL)
@PreserveOnRefresh
public class \${ApplicationName}UI extends LinkkiUi {

    public static final String URL = ""; //$NON-NLS-1$

    private final CDINavigator cdiNavigator;

    @Inject
    public \${ApplicationName}UI(CDINavigator cdiNavigator) {
        super(new \${ApplicationName}ApplicationConfig());
        this.cdiNavigator = cdiNavigator;
    }

    @Override
    protected void configureNavigator(ViewDisplay applicationLayout) {
        cdiNavigator.init(this, applicationLayout);
        setNavigator(cdiNavigator);
    }
}
