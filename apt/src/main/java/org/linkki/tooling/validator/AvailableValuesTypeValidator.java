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

package org.linkki.tooling.validator;

import static java.util.Arrays.asList;
import static org.linkki.tooling.util.ClassNotFoundMessageUtils.printAnnotationNotFoundWarning;
import static org.linkki.tooling.util.Constants.CONTENT;
import static org.linkki.tooling.util.Constants.MODEL_ATTRIBUTE;
import static org.linkki.tooling.util.Constants.MODEL_OBJECT;
import static org.linkki.tooling.util.SuppressedWarningsUtils.isSuppressed;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;
import org.linkki.tooling.model.AptAttribute;
import org.linkki.tooling.model.AptComponentDeclaration;
import org.linkki.tooling.model.AptModelAttribute;
import org.linkki.tooling.model.AptPmo;
import org.linkki.tooling.util.ElementUtils;
import org.linkki.tooling.util.ModelUtils;
import org.linkki.tooling.util.ReflectionUtils;

/**
 * A Validator to ensure that when a UI-Annotation has the field content and the value is either
 * {@link AvailableValuesType#ENUM_VALUES_EXCL_NULL} or
 * {@link AvailableValuesType#ENUM_VALUES_INCL_NULL} that the type of the model attribute is either Enum
 * or Boolean.
 */
@MessageCodes(AvailableValuesTypeValidator.WRONG_CONTENT_TYPE)
public class AvailableValuesTypeValidator implements Validator {

    public static final String WRONG_CONTENT_TYPE = "WRONG_CONTENT_TYPE";
    private static final HashSet<AvailableValuesType> TYPE_SENSITIVE_AVAILABLE_VALUES_TYPES = new HashSet<>(asList(
                                                                                                                   AvailableValuesType.ENUM_VALUES_EXCL_NULL,
                                                                                                                   AvailableValuesType.ENUM_VALUES_INCL_NULL));

    private static final String MSG_TEMPLATE = Messages.getString("AvailableValuesType_error") //$NON-NLS-1$
            + Messages.getString("AnnotationInfo")
            + Messages.getString("MSG_CODE");


    private final Kind wrongContentTypeSeverity;
    private final ElementUtils elementUtils;

    public AvailableValuesTypeValidator(
            Map<String, String> options,
            ElementUtils elementUtils) {
        this.elementUtils = elementUtils;
        this.wrongContentTypeSeverity = Severity.of(options, WRONG_CONTENT_TYPE, Kind.ERROR);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {
        if (wrongContentTypeSeverity == Kind.OTHER) {
            return;
        }

        if (isSuppressed(pmo.getElement(), wrongContentTypeSeverity)) {
            return;
        }

        pmo.getComponents().stream()
                .flatMap(it -> it.getComponentDeclarations().stream())
                .filter(it -> it.isModelBinding())
                .filter(it -> !isSuppressed(it.getElement(), wrongContentTypeSeverity))
                .forEach(componentDeclaration -> {
                    try {
                        TypeElement annotationElement = (TypeElement)componentDeclaration.getAnnotationMirror()
                                .getAnnotationType()
                                .asElement();

                        Class<? extends Annotation> annotationType = elementUtils.getAnnotationType(annotationElement);
                        Annotation annotation = componentDeclaration.getElement().getAnnotation(annotationType);
                        Objects.requireNonNull(annotation);

                        checkAvailableValues(messager, pmo, componentDeclaration, annotation);

                    } catch (ClassNotFoundException e) {
                        printAnnotationNotFoundWarning(messager,
                                                       componentDeclaration.getElement(),
                                                       componentDeclaration.getAnnotationMirror());
                    }
                });
    }

    private void checkAvailableValues(
            Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration,
            Annotation annotation) {
        List<LinkkiAspectDefinition> aspectDefinitions = AspectAnnotationReader
                .createAspectDefinitionsFrom(annotation);

        aspectDefinitions.stream()
                .filter(it -> it instanceof AvailableValuesAspectDefinition<?>)
                .map(it -> (AvailableValuesAspectDefinition<?>)it)
                .map(it -> ReflectionUtils.getAnnotationProperty(annotation, CONTENT))
                .flatMap(it -> it.map(Stream::of).orElse(Stream.empty()))
                .filter(it -> it instanceof AvailableValuesType)
                .map(it -> (AvailableValuesType)it)
                .filter(TYPE_SENSITIVE_AVAILABLE_VALUES_TYPES::contains)
                .forEach(availableValuesType -> {
                    ReflectionUtils
                            .getAnnotationProperty(annotation, MODEL_OBJECT)
                            .filter(it -> it instanceof String)
                            .map(Object::toString)
                            .ifPresent(modelObjectName -> {
                                ReflectionUtils
                                        .getAnnotationProperty(annotation, MODEL_ATTRIBUTE)
                                        .filter(it -> it instanceof String)
                                        .map(Object::toString)
                                        .ifPresent(modelAttributeName -> {
                                            checkModelAttribute(messager,
                                                                pmo,
                                                                componentDeclaration,
                                                                availableValuesType,
                                                                modelObjectName,
                                                                modelAttributeName);
                                        });
                            });
                });
    }

    private void checkModelAttribute(
            Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration,
            AvailableValuesType availableValuesType,
            String modelObjectName,
            String modelAttributeName) {

        String propertyName = componentDeclaration.getPropertyName();

        // property name as fallback for model attribute name
        String attributeName = modelAttributeName.isEmpty() ? propertyName : modelAttributeName;

        pmo.getModelObjects().stream()
                .filter(it -> it.getAnnotation().name().equals(modelObjectName))
                .flatMap(it -> it.getModelAttributes().stream())
                .filter(it -> it.getName().equals(attributeName))
                .map(AptModelAttribute::getElement)
                .filter(it -> !isEnumOrBoolean(it.getReturnType()))
                .findFirst()
                .ifPresent(attributeMethod -> {
                    String message = String.format(MSG_TEMPLATE,
                                                   propertyName,
                                                   availableValuesType,
                                                   componentDeclaration.getAnnotationMirror(),
                                                   WRONG_CONTENT_TYPE);

                    Optional<AnnotationValue> attribute = ModelUtils.findAttribute(componentDeclaration.getAttributes(),
                                                                                   CONTENT)
                            .map(AptAttribute::getAnnotationValue);

                    if (attribute.isPresent()) {
                        messager.printMessage(wrongContentTypeSeverity,
                                              message,
                                              componentDeclaration.getElement(),
                                              componentDeclaration.getAnnotationMirror(),
                                              attribute.get());

                    } else {
                        messager.printMessage(wrongContentTypeSeverity,
                                              message,
                                              componentDeclaration.getElement(),
                                              componentDeclaration.getAnnotationMirror());
                    }
                });
    }

    private boolean isEnumOrBoolean(TypeMirror returnType) {
        Types types = elementUtils.getTypes();
        return returnType.getKind() == TypeKind.BOOLEAN
                || types.isSameType(returnType, asType(Boolean.class))
                || types.asElement(returnType).getKind() == ElementKind.ENUM;
    }

    private TypeMirror asType(Class<?> clazz) {
        return elementUtils.getElements().getTypeElement(clazz.getCanonicalName()).asType();
    }

}
