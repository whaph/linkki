/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.ui.table;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TableBinding;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.container.LinkkiInMemoryContainer;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;

public class PmoBasedTableSectionFactoryTest {

    @Test
    public void testCreateSection_TableIsAddedAndBound() {
        TestTablePmo containerPmo = new TestTablePmo();
        BindingContext bindingContext = TestBindingContext.create();
        PmoBasedTableSectionFactory<TestRowPmo> factory = new PmoBasedTableSectionFactory<TestRowPmo>(
                containerPmo, bindingContext);

        TableSection<TestRowPmo> tableSection = factory.createSection();

        assertThat(tableSection, is(notNullValue()));
        assertThat(tableSection.getComponentCount(), is(2)); // header and table
        assertThat(tableSection.getComponent(1), is(instanceOf(Table.class)));
        Table table = (Table)tableSection.getComponent(1);
        assertThat(table.getContainerDataSource(), is(instanceOf(LinkkiInMemoryContainer.class)));
        LinkkiInMemoryContainer<?> container = (LinkkiInMemoryContainer<?>)table.getContainerDataSource();
        assertThat(bindingContext, hasTableBindingWith(container));
    }

    private Matcher<BindingContext> hasTableBindingWith(LinkkiInMemoryContainer<?> container) {
        return new TypeSafeMatcher<BindingContext>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("a BindingContext containing a TableBinding using the table container ");
                description.appendValue(container);
            }

            @Override
            protected boolean matchesSafely(BindingContext bindingContext) {
                return bindingContext.getBindings().stream()
                        .filter(TableBinding.class::isInstance)
                        .map(TableBinding.class::cast)
                        .map(TableBinding::getTableContainer)
                        .anyMatch(Predicate.isEqual(container));
            }
        };
    }

    @Test
    public void testCreateSection_SectionHasAddButtonInHeader() {
        TestTablePmo containerPmo = new TestTablePmo();
        BindingContext bindingContext = TestBindingContext.create();
        PmoBasedTableSectionFactory<TestRowPmo> factory = new PmoBasedTableSectionFactory<TestRowPmo>(
                containerPmo, bindingContext);

        TableSection<TestRowPmo> tableSection = factory.createSection();

        assertThat(tableSection, is(notNullValue()));
        assertThat(tableSection.getComponentCount(), is(2)); // header and table
        assertThat(tableSection.getComponent(0), is(instanceOf(HorizontalLayout.class)));
        HorizontalLayout header = (HorizontalLayout)tableSection.getComponent(0);
        assertThat(header.getComponentCount(), is(3)); // caption, add button and close button
        assertThat(header.getComponent(1), is(instanceOf(Button.class)));
        Button addButton = (Button)header.getComponent(1);
        assertThat(addButton.getIcon(), is(FontAwesome.PLUS));
    }

    @Test
    public void testCreateSection_NoAnnotation() {
        NoAnnotationTablePmo containerPmo = new NoAnnotationTablePmo();
        BindingContext bindingContext = TestBindingContext.create();
        PmoBasedTableSectionFactory<TestRowPmo> factory = new PmoBasedTableSectionFactory<TestRowPmo>(
                containerPmo, bindingContext);

        TableSection<TestRowPmo> tableSection = factory.createSection();

        assertThat(tableSection, is(notNullValue()));
        assertThat(tableSection.getComponentCount(), is(2)); // header and table
        assertThat(tableSection.getComponent(1), is(instanceOf(Table.class)));
        Table table = (Table)tableSection.getComponent(1);
        assertThat(table.getContainerDataSource(), is(instanceOf(LinkkiInMemoryContainer.class)));
        LinkkiInMemoryContainer<?> container = (LinkkiInMemoryContainer<?>)table.getContainerDataSource();
        assertThat(bindingContext, hasTableBindingWith(container));
    }

    public static class NoAnnotationTablePmo implements ContainerPmo<TestRowPmo> {

        @Override
        public List<TestRowPmo> getItems() {
            return new ArrayList<>();
        }

    }
}
