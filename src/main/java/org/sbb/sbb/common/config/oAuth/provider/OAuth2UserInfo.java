package org.sbb.sbb.common.config.oAuth.provider;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
}
