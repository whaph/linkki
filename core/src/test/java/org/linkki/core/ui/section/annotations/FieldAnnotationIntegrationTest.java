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

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import com.vaadin.ui.AbstractField;

public abstract class FieldAnnotationIntegrationTest<F extends AbstractField<?>, P extends AnnotationTestPmo>
        extends ComponentAnnotationIntegrationTest<F, P> {

    @SuppressWarnings("null")
    public FieldAnnotationIntegrationTest(Supplier<Object> modelObjectSupplier,
            Function<Object, ? extends P> pmoCreator) {
        super(modelObjectSupplier, pmoCreator);
    }


    @Test
    public void testDynamicEnabledType() {
        testDynamicBinding(F::isEnabled, AnnotationTestPmo::setEnabled, true);
    }

    @Test
    public void testDynamicRequiredType() {
        testDynamicBinding(F::isRequired, AnnotationTestPmo::setRequired, false);
    }

    @Test
    public void testReadonlyWithoutSetter() {
        assertThat(getStaticComponent().isReadOnly(), is(true));
        assertThat(getDynamicComponent().isReadOnly(), is(false));
    }


}
