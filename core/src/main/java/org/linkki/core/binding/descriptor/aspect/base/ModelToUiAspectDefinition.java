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

package org.linkki.core.binding.descriptor.aspect.base;

import java.util.function.Consumer;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * A convenient implementation for {@link LinkkiAspectDefinition}s that only updates upon model changes.
 * <p>
 * Implementations need to specify the {@link Aspect} by implementing {@link #createAspect()} and
 * provide a consumer that is able to set the value to the component.
 * 
 * @param <V> the class for values updated with {@link Aspect Aspects} created from this
 *            {@link LinkkiAspectDefinition}
 */
public abstract class ModelToUiAspectDefinition<V> implements LinkkiAspectDefinition {

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Consumer<V> setter = createComponentValueSetter(componentWrapper);
        Aspect<V> aspect = createAspect();
        return () -> {
            try {
                setter.accept(propertyDispatcher.pull(aspect));
                // CSOFF: IllegalCatch
            } catch (RuntimeException e) {
                handleUiUpdateException(e, propertyDispatcher, aspect);
                // CSON: IllegalCatch
            }
        };
    }

    /**
     * Handles a caught {@link RuntimeException} during UI update. Giving the {@link PropertyDispatcher}
     * and the aspect this method creates a useful exception and throws a {@link LinkkiBindingException}
     * wrapping the exception.
     * <p>
     * Subclasses may change the behavior, throw more useful exceptions or silently ignore the exception
     * (beware this might lead to very difficult search for failures).
     * 
     * @param e the exception that was caught
     * @param propertyDispatcher the {@link PropertyDispatcher} that was used to update the UI
     * @param aspect the aspect that was used to update the UI
     */
    protected void handleUiUpdateException(RuntimeException e,
            PropertyDispatcher propertyDispatcher,
            Aspect<V> aspect) {
        Object boundObject = propertyDispatcher.getBoundObject();
        throw new LinkkiBindingException(
                e.getMessage() +
                        " while applying " +
                        (aspect.getName().isEmpty() ? "value" : "aspect " + aspect.getName()) +
                        " of " +
                        (boundObject != null ? boundObject.getClass() : "<no object>") +
                        "#" + propertyDispatcher.getProperty(),
                e);
    }

    /**
     * Returns an {@link Aspect} for this {@link LinkkiAspectDefinition}.
     * <p>
     * This class assumes that the value of the {@link Aspect} does not depend on the type of model
     * value. If this is the case, use the {@link LinkkiAspectDefinition} interface directly.
     * 
     * @return a new {@link Aspect} of this definition
     */
    public abstract Aspect<V> createAspect();

    /**
     * Defines how the value of the value of the {@link ComponentWrapper} is set.
     * 
     * @param componentWrapper UI component of which the value has to be
     * @return setter for the value of the {@link ComponentWrapper}
     */
    public abstract Consumer<V> createComponentValueSetter(ComponentWrapper componentWrapper);
}
