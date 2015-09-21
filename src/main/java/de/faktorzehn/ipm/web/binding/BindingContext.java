package de.faktorzehn.ipm.web.binding;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

/**
 * A binding context binds fields in a section of the user interface like a page or a dialog to
 * properties of a single presentation model object. If the value in one of the fields is changed,
 * all fields in the context are updated from the presentation model object via their bindings.
 */
public class BindingContext {

    private String name;
    private List<FieldBinding<?>> fieldBindings = new LinkedList<>();
    private Set<PropertyDispatcher> propertyDispatchers = new HashSet<>();

    public BindingContext() {
        this("DefaultContext");
    }

    public BindingContext(String contextName) {
        this.name = contextName;
    }

    public String getName() {
        return name;
    }

    public BindingContext add(FieldBinding<?> binding) {
        fieldBindings.add(binding);
        propertyDispatchers.add(binding.getPropertyDispatcher());
        return this;
    }

    public List<FieldBinding<?>> getFieldBindings() {
        return Collections.unmodifiableList(fieldBindings);
    }

    public void removeBindingsForPmo(PresentationModelObject pmo) {
        List<FieldBinding<?>> toRemove = fieldBindings.stream()
                .filter(fb -> fb.getPropertyDispatcher().getPmo() == pmo).collect(Collectors.toList());
        toRemove.stream().map(fb -> fb.getPropertyDispatcher()).forEach(propertyDispatchers::remove);
        fieldBindings.removeAll(toRemove);
    }

    public void updateUI() {
        propertyDispatchers.forEach(pd -> pd.prepareUpdateUI());
        fieldBindings.forEach(binding -> binding.updateFieldFromPmo());
    }

    @Override
    public String toString() {
        return "BindingContext [name=" + name + "]";
    }

}