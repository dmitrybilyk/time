package com.count.time.security;

import com.count.time.model.User;
import com.count.time.repositories.RoleRepository;
import com.count.time.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes = oidcUser.getAttributes();
//        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo();
        String googleEmail = (String) attributes.get("email");
        User user = userRepository.findByLogin(googleEmail);
        if (user == null) {
            User newUser = new User();
            newUser.setEnabled(true);
            newUser.setEmail(googleEmail);
            newUser.setLogin(googleEmail);
            newUser.setRoles(Collections.singletonList(roleRepository.findByName("USER")));
            userRepository.save(newUser);
        }
//        userInfo.setId((String) attributes.get("sub"));
//        userInfo.setImageUrl((String) attributes.get("picture"));
//        userInfo.setName((String) attributes.get("name"));
//        updateUser(userInfo);
        return oidcUser;
    }

//    private void updateUser(GoogleOAuth2UserInfo userInfo) {
//        User user = userRepository.findByEmail(userInfo.getEmail());
//        if(user == null) {
//            user = new User();
//        }
//        user.setEmail(userInfo.getEmail());
//        user.setImageUrl(userInfo.getImageUrl());
//        user.setName(userInfo.getName());
//        user.setUserType(UserType.google);
//		userRepository.save(user);
//    }
}

