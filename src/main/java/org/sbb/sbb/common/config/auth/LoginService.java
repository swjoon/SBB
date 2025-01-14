package org.sbb.sbb.common.config.auth;

import lombok.RequiredArgsConstructor;
import org.sbb.sbb.domain.user.entity.User;
import org.sbb.sbb.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을수 없습니다."));

        return new LoginUser(user);
    }
}
