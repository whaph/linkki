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

package org.linkki.core.defaults.columnbased;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiLayoutComponent;

public class TestColumnBasedComponentCreator implements ColumnBasedComponentCreator {

    @Override
    public void initColumn(ContainerPmo<?> containerPmo,
            ComponentWrapper parentWrapper,
            BindingContext bindingContext,
            PropertyElementDescriptors elementDesc) {
        TestColumnBasedComponent<?> table = (TestColumnBasedComponent<?>)parentWrapper.getComponent();
        TestUiLayoutComponent column = new TestUiLayoutComponent();
        table.addChild(column);
        String propertyName = elementDesc.getPmoPropertyName();
        bindingContext.bind(containerPmo, BoundProperty.of(propertyName), elementDesc.getAllAspects(),
                            new TestComponentWrapper(column));
    }

    @Override
    public ComponentWrapper createComponent(ContainerPmo<?> containerPmo) {
        return new TestComponentWrapper(new TestColumnBasedComponent<>());
    }
}