/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.viewer.wicket.ui.pages.accmngt;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.isis.applib.services.userreg.UserRegistrationService;
import org.apache.isis.core.metamodel.services.ServicesInjector;
import org.apache.isis.core.runtime.system.context.IsisContext;
import org.apache.isis.core.runtime.system.session.IsisSessionFactory;

/**
 * Validates that an username is or is not already in use by someone else
 */
public class UsernameAvailableValidator implements IValidator<String> {

    public static final UsernameAvailableValidator INSTANCE = new UsernameAvailableValidator();

    private UsernameAvailableValidator() {
    }

    @Override
    public void validate(final IValidatable<String> validatable) {
        getIsisSessionFactory().doInSession(new Runnable() {
            @Override
            public void run() {
                UserRegistrationService userRegistrationService = getServicesInjector().lookupServiceElseFail(UserRegistrationService.class);

                final String username = validatable.getValue();
                boolean usernameExists = userRegistrationService.usernameExists(username);
                if (usernameExists) {
                    validatable.error(new ValidationError().addKey("usernameIsNotAvailable"));
                }
            }
        });

    }

    ServicesInjector getServicesInjector() {
        return getIsisSessionFactory().getServicesInjector();
    }

    IsisSessionFactory getIsisSessionFactory() {
        return IsisContext.getSessionFactory();
    }
}
