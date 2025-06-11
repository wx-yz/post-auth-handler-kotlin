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

package org.wso2.carbon.identity.post.authn.handler.disclaimer

import org.wso2.carbon.identity.application.authentication.framework.config.ConfigurationFacade
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext
import org.wso2.carbon.identity.application.authentication.framework.exception.PostAuthenticationFailedException
import org.wso2.carbon.identity.application.authentication.framework.handler.request.AbstractPostAuthnHandler
import org.wso2.carbon.identity.application.authentication.framework.handler.request.PostAuthnHandlerFlowStatus
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class DisclaimerPostAuthenticationHandler : AbstractPostAuthnHandler() {

    companion object {
        private const val CONSENT_POPPED_UP = "consentPoppedUp"
    }

    override fun handle(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        authenticationContext: AuthenticationContext
    ): PostAuthnHandlerFlowStatus {
        if (getAuthenticatedUser(authenticationContext) == null) {
            return PostAuthnHandlerFlowStatus.SUCCESS_COMPLETED
        }

        return if (isConsentPoppedUp(authenticationContext)) {
            handleConsentResponse(httpServletRequest)
        } else {
            handleInitialRequest(httpServletResponse, authenticationContext)
        }
    }

    private fun handleConsentResponse(request: HttpServletRequest): PostAuthnHandlerFlowStatus {
        return if (request.getParameter("consent").equals("approve", ignoreCase = true)) {
            PostAuthnHandlerFlowStatus.SUCCESS_COMPLETED
        } else {
            throw PostAuthenticationFailedException(
                "Cannot access this application : Consent Denied",
                "Consent denied"
            )
        }
    }

    private fun handleInitialRequest(
        response: HttpServletResponse,
        context: AuthenticationContext
    ): PostAuthnHandlerFlowStatus {
        try {
            val urlEncodedApplicationName = URI(
                null,
                null,
                context.sequenceConfig.applicationConfig.applicationName,
                null
            ).toASCIIString()

            val redirectUrl = ConfigurationFacade.getInstance()
                .authenticationEndpointURL
                .replace("/login.do", "") +
                    "/disclaimer.jsp?sessionDataKey=${context.contextIdentifier}" +
                    "&application=$urlEncodedApplicationName"

            response.sendRedirect(redirectUrl)
            setConsentPoppedUpState(context)
            return PostAuthnHandlerFlowStatus.INCOMPLETE
        } catch (e: IOException) {
            throw PostAuthenticationFailedException("Invalid Consent", "Error while redirecting", e)
        } catch (e: URISyntaxException) {
            throw PostAuthenticationFailedException(
                "Invalid Application Name",
                "Error encoding application name",
                e
            )
        }
    }

    override fun getName(): String = "DisclaimerHandler"

    private fun getAuthenticatedUser(authenticationContext: AuthenticationContext): AuthenticatedUser? =
        authenticationContext.sequenceConfig.authenticatedUser

    private fun setConsentPoppedUpState(authenticationContext: AuthenticationContext) {
        authenticationContext.addParameter(CONSENT_POPPED_UP, true)
    }

    private fun isConsentPoppedUp(authenticationContext: AuthenticationContext): Boolean =
        authenticationContext.getParameter(CONSENT_POPPED_UP) != null
} 