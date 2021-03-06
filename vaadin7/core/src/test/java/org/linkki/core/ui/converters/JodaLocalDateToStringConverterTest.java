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
package org.linkki.core.ui.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Locale;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.util.DateFormats;

public class JodaLocalDateToStringConverterTest {

    @Test
    public void testConvertToModel() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            new JodaLocalDateToStringConverter().convertToModel(null, null, null);
        });
    }

    @Test
    public void testConvertToPresentation_null_shouldReturnEmptyString() {
        String presentation = new JodaLocalDateToStringConverter().convertToPresentation(null, String.class, null);
        assertNotNull(presentation);
        assertThat(presentation, is(""));
    }

    @Test
    public void testConvertToPresentation() {
        LocalDate date = LocalDate.now();
        String presentation = new JodaLocalDateToStringConverter().convertToPresentation(date, null, Locale.GERMANY);
        assertNotNull(presentation);
        assertThat(presentation, is(date.toString(DateFormats.PATTERN_DE)));
    }

}