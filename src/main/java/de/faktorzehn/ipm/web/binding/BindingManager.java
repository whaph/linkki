package de.faktorzehn.ipm.web.binding;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages a set of binding contexts.
 */
public abstract class BindingManager {

    private Map<String, BindingContext> contextsByName = new HashMap<>();

    /**
     * Starts a new binding context and uses the class' qualified name as context name.
     */
    public BindingContext startNewContext(Class<?> clazz) {
        return startNewContext(clazz.getName());
    }

    public BindingContext startNewContext(String name) {
        checkState(!contextsByName.containsKey(name), "BindingManager already contains a BindingContext '%s'.", name);
        BindingContext newContext = newBindingContext(name);
        contextsByName.put(name, newContext);
        return newContext;
    }

    protected abstract BindingContext newBindingContext(String name);

    public Optional<BindingContext> getExistingContext(Class<?> clazz) {
        return getExistingContext(clazz.getName());
    }

    public Optional<BindingContext> getExistingContext(String name) {
        return Optional.ofNullable(contextsByName.get(name));
    }

    public BindingContext getExistingContextOrStartNewOne(Class<?> clazz) {
        return getExistingContextOrStartNewOne(clazz.getName());
    }

    public BindingContext getExistingContextOrStartNewOne(String name) {
        BindingContext context = contextsByName.get(name);
        if (context == null) {
            context = startNewContext(name);
        }
        return context;
    }

    public void removeContext(BindingContext context) {
        contextsByName.remove(context.getName());
    }

    public void removeAllContexts() {
        contextsByName.clear();
    }
}
