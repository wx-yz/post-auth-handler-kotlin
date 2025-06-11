/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.identity.post.authn.handler.disclaimer.internal

import org.osgi.service.component.ComponentContext
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.ReferenceCardinality
import org.osgi.service.component.annotations.ReferencePolicy
import org.wso2.carbon.identity.application.authentication.framework.handler.request.PostAuthenticationHandler
import org.wso2.carbon.identity.core.util.IdentityCoreInitializedEvent
import org.wso2.carbon.identity.post.authn.handler.disclaimer.DisclaimerPostAuthenticationHandler

@Component(name = "identity.post.authn.disclaimer.handler", immediate = true)
class DisclaimerPostAuthnHandlerServiceComponent {
    @Activate
    protected fun activate(context: ComponentContext) {
        log.info("### Inside DisclaimerPostAuthnHandlerServiceComponent, trying to activate")
        try {
            val disclaimerPostAuthenticationHandler: DisclaimerPostAuthenticationHandler =
                DisclaimerPostAuthenticationHandler()
            context.getBundleContext().registerService(
                PostAuthenticationHandler::class.java.getName(),
                disclaimerPostAuthenticationHandler, null
            )
        } catch (e: Throwable) {
            org.wso2.carbon.identity.post.authn.handler.disclaimer.internal.DisclaimerPostAuthnHandlerServiceComponent.Companion.log.error(
                "Error while activating disclaimer post authentication handler.",
                e
            )
            log.info("### DisclaimerPostAuthnHandlerServiceComponent, activation error", e)
        }
        log.info("### DisclaimerPostAuthnHandlerServiceComponent, activation successful")
    }

    protected fun unsetIdentityCoreInitializedEventService(identityCoreInitializedEvent: IdentityCoreInitializedEvent?) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
    }

    @Reference(
        name = "identity.core.init.event.service",
        service = IdentityCoreInitializedEvent::class,
        cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.DYNAMIC,
        unbind = "unsetIdentityCoreInitializedEventService"
    )
    protected fun setIdentityCoreInitializedEventService(identityCoreInitializedEvent: IdentityCoreInitializedEvent?) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
    }

    companion object {
        private val log: org.apache.commons.logging.Log =
            org.apache.commons.logging.LogFactory.getLog(org.wso2.carbon.identity.post.authn.handler.disclaimer.internal.DisclaimerPostAuthnHandlerServiceComponent::class.java)
    }
}