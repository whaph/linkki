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
package org.linkki.samples.appsample.state;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationFooter;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.samples.appsample.custom.CustomApplicationHeader;
import org.linkki.samples.appsample.nls.NlsText;
import org.linkki.util.Sequence;

/**
 * An {@link ApplicationConfig} using a {@link CustomApplicationHeader custom application header} and no
 * {@link ApplicationFooter application footer}.
 */
public class CustomApplicationConfig implements ApplicationConfig {

    @Override
    public String getApplicationName() {
        return NlsText.getString("CustomApplicationConfig.Name");
    }

    @Override
    public String getApplicationVersion() {
        return NlsText.getString("CustomApplicationConfig.Version");
    }

    @Override
    public String getApplicationDescription() {
        return NlsText.getString("CustomApplicationConfig.Description");
    }

    @Override
    public String getCopyright() {
        return "© " + LocalDate.now().getYear() + " Faktor Zehn GmbH - ConVista Consulting"; //$NON-NLS-1$
    }

    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.of();
    }

    @Override
    public ApplicationHeaderDefinition getHeaderDefinition() {
        return CustomApplicationHeader::new;
    }

    @Override
    public Optional<ApplicationFooterDefinition> getFooterDefinition() {
        return Optional.of(ApplicationFooter::new);
    }

    public String getLicense(Locale locale) {
        String licenseNoticeEN = "Copyright Faktor Zehn GmbH\r\n\r\n"
                + "Licensed under the Apache License, Version 2.0 (the \"License\");\r\n"
                + "you may not use this file except in compliance with the License.\r\n"
                + "You may obtain a copy of the License at\r\n\r\n"
                + " http://www.apache.org/licenses/LICENSE-2.0\r\n\r\n"
                + "Unless required by applicable law or agreed to in writing, software\r\n"
                + "distributed under the License is distributed on an \"AS IS\" BASIS,\r\n"
                + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express\r\n"
                + "or implied. See the License for the specific language governing\r\n"
                + "permissions and limitations under the License.";

        String licenseNoticeDE = "Copyright Faktor Zehn GmbH\r\n\r\n"
                + "Lizenziert gemäß Apache Licence Version 2.0 (die „Lizenz“);\r\n"
                + "Nutzung dieser Datei nur in Übereinstimmung mit der Lizenz erlaubt.\r\n"
                + "Eine Kopie der Lizenz erhalten Sie auf\r\n\r\n"
                + "http://www.apache.org/licenses/LICENSE-2.0.\r\n\r\n"
                + "Sofern nicht gemäß geltendem Recht vorgeschrieben oder schriftlich\r\n"
                + "vereinbart, erfolgt die Bereitstellung der im Rahmen der Lizenz\r\n"
                + "verbreiteten Software OHNE GEWÄHR ODER VORBEHALTE – ganz gleich,\r\n"
                + "ob ausdrücklich oder stillschweigend. Informationen über die jeweiligen\r\n"
                + "Bedingungen für Genehmigungen und Einschränkungen im Rahmen der\r\n"
                + "Lizenz finden Sie in der Lizenz.";

        return Locale.GERMAN.getLanguage().equals(locale.getLanguage()) ? licenseNoticeDE : licenseNoticeEN;
    }

}
