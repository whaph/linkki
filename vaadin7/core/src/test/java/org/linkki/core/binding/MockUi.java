/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.binding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;

import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class MockUi {
    private MockUi() {
        // utility
    }

    /**
     * Remember to call {@link UI#setCurrent(UI) UI#setCurrent(null)} after your tests.
     */
    public static UI mockUi() {
        UI ui = mock(UI.class, Mockito.RETURNS_DEEP_STUBS);
        UI.setCurrent(ui);
        VaadinSession session = mock(VaadinSession.class);
        when(ui.getSession()).thenReturn(session);
        when(session.hasLock()).thenReturn(true);
        DefaultConverterFactory converterFactory = new DefaultConverterFactory();
        when(session.getConverterFactory()).thenReturn(converterFactory);
        return ui;
    }
}