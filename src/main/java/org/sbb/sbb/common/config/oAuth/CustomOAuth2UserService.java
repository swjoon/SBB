package org.sbb.sbb.common.config.oAuth;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.common.config.auth.LoginUser;
import org.sbb.sbb.common.config.oAuth.provider.GoogleUserInfo;
import org.sbb.sbb.common.config.oAuth.provider.OAuth2UserInfo;
import org.sbb.sbb.domain.user.entity.Role;
import org.sbb.sbb.domain.user.entity.User;
import org.sbb.sbb.domain.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest,oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = processOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        Optional<User> userOp = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(),oAuth2UserInfo.getProviderId());

        User user = userOp.orElseGet(()-> createUser(oAuth2UserInfo)); ;

        if(!user.getEmail().equals(oAuth2UserInfo.getEmail())) {
            user.setEmail(oAuth2UserInfo.getEmail());
        }

        return new LoginUser(user, oAuth2User.getAttributes());
    }

    private OAuth2UserInfo processOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if ("google".equalsIgnoreCase(registrationId)) {
            return new GoogleUserInfo(attributes);
        }

        throw new OAuth2AuthenticationException("지원하지 않는 로그인 공급자 입니다: " + registrationId);
    }

    private User createUser(OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .username(oAuth2UserInfo.getEmail())
                .nickname(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .role(Role.ROLE_USER)
                .provider(oAuth2UserInfo.getProvider())
                .providerId(oAuth2UserInfo.getProviderId())
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }
}
