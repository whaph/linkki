
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

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public abstract class SuppressedEnabledWarningPmo {

    private final Report report;

    public SuppressedEnabledWarningPmo(Report report) {
        this.report = requireNonNull(report, "report must not be null");
    }

    @ModelObject
    public Report getReport() {
        return report;
    }

    @UIComboBox(position = 20, label = "Type", modelAttribute = "type", required = RequiredType.REQUIRED, content = AvailableValuesType.DYNAMIC, enabled = EnabledType.DYNAMIC)
    @SuppressWarnings("linkki")
    public void type() {
        // model binding
    }

    public List<ReportType> getTypeAvailableValues() {
        return Arrays.asList(ReportType.values());
    }
}