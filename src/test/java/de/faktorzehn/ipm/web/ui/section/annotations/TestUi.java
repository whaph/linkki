package de.faktorzehn.ipm.web.ui.section.annotations;

import java.util.Collections;

import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.ui.section.BaseSection;
import de.faktorzehn.ipm.web.ui.section.PmoBasedSectionFactory;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;
import elemental.json.impl.JreJsonObject;

/** A Vaadin UI for tests. */
public class TestUi extends UI {

    /** A ConnectorTracker needed for our TestUi. */
    static class TestConnectorTracker extends ConnectorTracker {

        private static final long serialVersionUID = 1L;

        public TestConnectorTracker(UI uI) {
            super(uI);
        }

        @Override
        public JsonObject getDiffState(ClientConnector connector) {
            return new JreJsonObject(new JreJsonFactory());
        }
    }

    /** A section factory for tests. */
    static class TestSectionFactory extends PmoBasedSectionFactory {

        @Override
        public PropertyBehaviorProvider getPropertyBehaviorProvider() {
            return () -> Collections.emptyList();
        }
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected void init(VaadinRequest request) {
        // Nothing to do
    }

    @Override
    public ConnectorTracker getConnectorTracker() {
        return new TestConnectorTracker(this);
    }

    /**
     * Returns a component that is bound to the given PMO using the IPM data binder. The component
     * is part of a TestUI so that a rudimentary Vaadin environment is in place.
     * 
     * @param pmo the PMO to which the component is bound is bound
     * @return a {@code CheckBox} that is bound to the model object
     */
    public static Component componentBoundTo(PresentationModelObject pmo) {
        TestUi testUi = new TestUi();
        TestSectionFactory sectionFactory = new TestSectionFactory();
        BindingContext bindingContext = new BindingContext();
        BaseSection section = sectionFactory.createSection(pmo, bindingContext);

        testUi.setContent(section);

        bindingContext.updateUI();

        Panel panel = (Panel)section.getComponent(1);
        GridLayout gridLayout = (GridLayout)panel.getContent();
        return gridLayout.getComponent(1, 0);
    }
}