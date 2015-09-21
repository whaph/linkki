/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.components;

import java.text.NumberFormat;

class IntegerFieldConverter extends AbstractNumberFieldConverter<Integer> {

    private static final long serialVersionUID = 6756969882235490962L;

    IntegerFieldConverter(NumberFormat format) {
        super(format);
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    protected Integer convertToModel(Number value) {
        return value.intValue();
    }

}