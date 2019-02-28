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
package org.linkki.doc;

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;

@UISection
public class PersonSectionPmo {

    private final Person person;

    public PersonSectionPmo(Person person) {
        this.person = person;
    }

    // tag::modelobject[]
    @ModelObject
    public Person getPerson() {
        return person;
    }
    // end::modelobject[]

    // tag::pojo-binding[]
    @UITextField(position = 10, label = "First Name")
    public String getFirstName() {
        return getPerson().getFirstname();
    }
    // end::pojo-binding[]

    // tag::model-binding[]
    @UITextField(position = 10, label = "First Name", modelAttribute = "firstname")
    public void firstName() {
    }
    // end::model-binding[]
}
