package org.linkki.core.ui.section.annotations.adapters;

import java.util.function.Function;

import org.linkki.core.exception.LinkkiRuntimeException;
import org.linkki.core.ui.components.SubsetChooser;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UISubsetChooser;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

public class UISubsetChooserAdapter implements UIFieldDefinition {

    private final UISubsetChooser uiSubsetChooser;

    public UISubsetChooserAdapter(UISubsetChooser uiTwinColSelect) {
        this.uiSubsetChooser = uiTwinColSelect;
    }

    @Override
    public SubsetChooser newComponent() {
        SubsetChooser subsetChooser = ComponentFactory.newSubsetChooser();
        subsetChooser.setItemCaptionProvider(getItemCaptionProvider());
        return subsetChooser;
    }

    @Override
    public int position() {
        return uiSubsetChooser.position();
    }

    @Override
    public String label() {
        return uiSubsetChooser.label();
    }

    @Override
    public EnabledType enabled() {
        return uiSubsetChooser.enabled();
    }

    @Override
    public RequiredType required() {
        return uiSubsetChooser.required();
    }

    @Override
    public VisibleType visible() {
        return uiSubsetChooser.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return uiSubsetChooser.content();
    }

    @Override
    public String modelAttribute() {
        return uiSubsetChooser.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiSubsetChooser.noLabel();
    }

    private Function<Object, String> getItemCaptionProvider() {
        try {
            return uiSubsetChooser.itemCaptionProvider().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiRuntimeException(e);
        }
    }

}