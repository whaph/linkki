/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding;

import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

public interface ElementBinding {

    void updateFromPmo();

    PropertyDispatcher getPropertyDispatcher();

}