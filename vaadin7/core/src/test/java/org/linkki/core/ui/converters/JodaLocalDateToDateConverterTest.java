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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class JodaLocalDateToDateConverterTest {

    public static Object[][] data() {
        final String shortYearPattern = "yy.MM.dd";
        final String longYearPattern = "yyyy.MM.dd";
        final String germanLongPattern = "dd.MM.yyyy";
        final int deltaDays = 1000;
        final int nearly19Years = 19 * 365;
        final int nearly80Years = 80 * 365;

        return new Object[][] {
                /* [0] */ { null, "yy.MM.dd", null },
                /* [1] */ { "2016.12.29", longYearPattern, new LocalDate(2016, 12, 29) },
                /* [2] */ { "01.01.1969", germanLongPattern, new LocalDate(1969, 01, 01) },
                /* [3] */ { "01.01.1900", germanLongPattern, new LocalDate(1900, 01, 01) },
                /* [4] */ { "01.01.1899", germanLongPattern, new LocalDate(1899, 01, 01) },
                /* [5] */ { "02.04.1893", germanLongPattern, new LocalDate(1893, 04, 02) },
                /* [6] */ { "01.04.1893", germanLongPattern, new LocalDate(1893, 04, 01) },
                /* [7] */ { "01.01.1800", germanLongPattern, new LocalDate(1800, 01, 01) },
                /* [8] */ { "01.01.1800", germanLongPattern, new LocalDate(1800, 01, 01) },
                /* [9] */ { "28.02.1700", germanLongPattern, new LocalDate(1700, 02, 28) },
                /* [10] */ { "29.02.1600", germanLongPattern, new LocalDate(1600, 02, 29) },
                /* [11] */ { "04.02.1598", germanLongPattern, new LocalDate(1598, 02, 04) },
                /* [12] */ { "04.02.1598", germanLongPattern, new LocalDate(1598, 02, 04) },
                // @formatter:off
                /* [13] */ { getDateFromNow(deltaDays).toString(shortYearPattern), shortYearPattern,getDateFromNow(deltaDays) },
                /* [14] */ { getDateFromNow(-deltaDays).toString(shortYearPattern), shortYearPattern,getDateFromNow(-deltaDays) },
                /* [15] */ { "00" + getDateFromNow(deltaDays).toString(shortYearPattern), longYearPattern,getDateFromNow(deltaDays) },
                /* [16] */ { "00" + getDateFromNow(-deltaDays).toString(shortYearPattern), longYearPattern,getDateFromNow(-deltaDays) },
                /* [17] */ { "00" + getDateFromNow(-nearly80Years).toString(shortYearPattern), longYearPattern,getDateFromNow(-nearly80Years) },
                /* [18] */ { "00" + getDateFromNow(nearly19Years).toString(shortYearPattern), longYearPattern,getDateFromNow(nearly19Years) },
                /* [19] */ { getDateFromNow(0).toString(shortYearPattern), shortYearPattern, getDateFromNow(0) }, // today
                /* [20] */ { "00" + getDateFromNow(0).toString(shortYearPattern), longYearPattern, getDateFromNow(0) }, // today
                // @formatter:on
                /* [21] */ { "04.02.15981", germanLongPattern, new LocalDate(15981, 02, 04) },
                /* [22] */ { "04.02.15981654", germanLongPattern, new LocalDate(15981654, 02, 04) }
        };
    }

    private static final LocalDate getDateFromNow(int days) {
        return LocalDate.now().plusDays(days);

    }


    @ParameterizedTest
    @MethodSource("data")
    public void testConvertToModel(String date, String pattern, LocalDate localDate) throws Exception {
        assertThat(convert(date, pattern), is(localDate));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testConvertToPresentation(String date, String pattern, LocalDate localDate) throws Exception {
        Date converted = convert(localDate);

        assertThat(converted, is(convertToDate(getDateToConvert(date, pattern), pattern)));
    }

    @CheckForNull
    private static LocalDate convert(String toConvert, String pattern) throws Exception {
        // we do not need type and locale
        return new JodaLocalDateToDateConverter().convertToModel(convertToDate(toConvert, pattern), null, null);
    }

    @CheckForNull
    private static Date convert(LocalDate toConvert) {
        return new JodaLocalDateToDateConverter().convertToPresentation(toConvert, null, null);
    }

    @CheckForNull
    private static Date convertToDate(@CheckForNull String date, String pattern) throws Exception {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(pattern).parse(date);
    }

    /**
     * we fake the java heuristic for 2 digit years in dates currently it works like a charm but who
     * knows - sometime it will be broken
     */
    @CheckForNull
    private static String getDateToConvert(@CheckForNull String date, String pattern) throws Exception {

        if (date == null || !date.startsWith("00")) {
            return date;
        }

        Date fixedDate = new SimpleDateFormat("yy.MM.dd").parse(date.substring(2));
        return new SimpleDateFormat(pattern).format(fixedDate);
    }
}