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

package org.linkki.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SequenceTest {
    private static final String DUMMY_ARG = "dummy";
    private static final String FAKE_ARG = "fake";

    @Test
    public void testEmpty() {
        Sequence<Object> sequence = Sequence.empty();
        assertThat(sequence.list(), is(Collections.emptyList()));
    }

    @Test
    public void testWith_VarArgs() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);

        assertThat(sequence.list().size(), is(1));
        assertThat(sequence.list(), hasItem(not(FAKE_ARG)));

        sequence = sequence.with(FAKE_ARG);

        assertThat(sequence.list().size(), is(2));
        assertThat(sequence.list(), hasItems(DUMMY_ARG, FAKE_ARG));
    }

    @Test
    public void testWith_Sequence() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);

        assertThat(sequence.list().size(), is(1));
        assertThat(sequence.list(), hasItem(not(FAKE_ARG)));

        Sequence<String> newSequence = Sequence.of(FAKE_ARG);
        sequence = sequence.with(newSequence);

        assertThat(sequence.list().size(), is(2));
        assertThat(sequence.list(), hasItems(DUMMY_ARG, FAKE_ARG));
    }

    @Test
    public void testToString() {
        Sequence<Integer> sequence = Sequence.of(1, 2, 3);
        assertThat(sequence.toString(), is("[1, 2, 3]"));
    }

    @Test
    public void testToString_ensureNoNPEifEmpty() {
        assertThat(Sequence.empty().toString(), is("[]"));
    }

    public void testWith_Collection() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);
        sequence = sequence.with(Arrays.asList(FAKE_ARG));

        assertThat(sequence.list().size(), is(2));
        assertThat(sequence.list(), hasItems(DUMMY_ARG, FAKE_ARG));
    }

    @Test
    public void testList_returnsUnmodifiableList() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG, FAKE_ARG);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            sequence.list().add("foo");
        });

    }

    @Test
    public void testList_returnsValuesInOrderOfSequence() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG, FAKE_ARG);
        List<String> list = sequence.list();
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(DUMMY_ARG));
        assertThat(list.get(1), is(FAKE_ARG));
    }

}
