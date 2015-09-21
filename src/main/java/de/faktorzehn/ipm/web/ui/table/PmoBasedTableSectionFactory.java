/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import de.faktorzehn.ipm.web.ButtonPmo;
import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

public class PmoBasedTableSectionFactory<T extends PresentationModelObject> {

    private ContainerPmo<T> containerPmo;
    private BindingContext bindingContext;
    private PropertyBehaviorProvider propertyBehaviorProvider;

    public PmoBasedTableSectionFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext,
            PropertyBehaviorProvider propertyBehaviorProvider) {

        this.containerPmo = containerPmo;
        this.bindingContext = bindingContext;
        this.propertyBehaviorProvider = propertyBehaviorProvider;
    }

    public TableSection<T> createSection() {
        PmoBasedTable<T> table = createTable();
        Optional<ButtonPmo> addItemPmo = table.addItemButtonPmo(bindingContext);
        TableSection<T> section = createEmptySection(addItemPmo);
        section.setTable(table);
        return section;
    }

    private PmoBasedTable<T> createTable() {
        PmoBasedTableFactory<T> tableFactory = new PmoBasedTableFactory<>(containerPmo, bindingContext,
                propertyBehaviorProvider);
        return tableFactory.createTable();
    }

    private TableSection<T> createEmptySection(Optional<ButtonPmo> addItemButtonPmo) {
        UISection sectionDefinition = containerPmo.getClass().getAnnotation(UISection.class);
        checkNotNull(sectionDefinition, "PMO " + containerPmo.getClass() + " must be annotated with @UISection!");
        return new TableSection<>(sectionDefinition.caption(), addItemButtonPmo);
    }

}