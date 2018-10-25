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
package org.linkki.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.ui.section.annotations.BindTooltipType;

// tag::BindTooltip[]
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
// end::BindTooltip[]
// tag::BindTooltipLinkkiAspect[]
@LinkkiAspect(BindTooltipAspectDefinition.class)
// end::BindTooltipLinkkiAspect[]
// tag::BindTooltip[]
public @interface BindTooltip {
    
    BindTooltipType tooltipType() default BindTooltipType.STATIC;

    String value() default StringUtils.EMPTY;

}
// end::BindTooltip[]