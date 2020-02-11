/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qintingfm.web.security;

import com.qintingfm.web.jpa.LoginTokenJpa;
import com.qintingfm.web.jpa.entity.LoginToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * JDBC based persistent login token repository implementation.
 *
 * @author Luke Taylor
 * @since 2.0
 */
@Service
@Slf4j
public class JpaTokenRepositoryImpl implements PersistentTokenRepository {

    LoginTokenJpa loginTokenJpa;
    @Autowired
    public void setLoginTokenJpa(LoginTokenJpa loginTokenJpa) {
        this.loginTokenJpa = loginTokenJpa;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        LoginToken loginToken = new LoginToken();
        loginToken.setLastUsed(token.getDate());
        loginToken.setSeries(token.getSeries());
        loginToken.setToken(token.getTokenValue());
        loginToken.setUsername(token.getUsername());
        loginTokenJpa.save(loginToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        Optional<LoginToken> byId = loginTokenJpa.findById(series);
        if (byId.isPresent()) {
            LoginToken loginToken = byId.get();
            loginToken.setToken(tokenValue);
            loginToken.setLastUsed(lastUsed);
            loginTokenJpa.save(loginToken);
        } else {
            log.error("未找到需要更新的token");
        }
    }

    /**
     * Loads the token data for the supplied series identifier.
     * <p>
     * If an error occurs, it will be reported and null will be returned (since the result
     * should just be a failed persistent login).
     *
     * @param seriesId ID
     * @return the token matching the series, or null if no match found or an exception
     * occurred.
     */
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Optional<LoginToken> byId = loginTokenJpa.findById(seriesId);
        if (byId.isPresent()) {
            LoginToken loginToken = byId.get();
            return new PersistentRememberMeToken(loginToken.getUsername(), loginToken.getSeries(), loginToken.getToken(), loginToken.getLastUsed());
        }
        log.debug("Querying token for series '" + seriesId
                + "' returned no results.");
        return null;
    }
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void removeUserTokens(String username) {
        loginTokenJpa.deleteByUsername(username);
    }
}
