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
package org.linkki.test.cdi;


import javax.enterprise.context.spi.CreationalContext;

import org.apache.commons.lang3.NotImplementedException;

/**
 * A {@link CreationalContext} implementation for tests.
 */
public class TestCreationalContext<T> implements CreationalContext<T> {

    @Override
    public void push(T incompleteInstance) {
        throw new NotImplementedException("");
    }

    @Override
    public void release() {
        throw new NotImplementedException("");
    }

}
