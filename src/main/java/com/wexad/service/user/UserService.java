package com.wexad.service.user;

import com.wexad.daos.user.UserDAO;
import com.wexad.domains.user.AuthUser;
import com.wexad.dto.LoginDTO;
import com.wexad.dto.SignupDTO;
import com.wexad.service.BaseService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserService implements BaseService<AuthUser, UUID> {
    private final UserDAO dao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDAO dao,@Lazy PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UUID save(AuthUser domain) {
        return dao.save(domain);
    }

    @Override
    public void update(AuthUser domain) {
        dao.update(domain);
    }

    @Override
    public void delete(UUID id) {
        dao.delete(id);
    }

    @Override
    public AuthUser findById(UUID id) {
        return dao.findById(id);
    }

    @Override
    public List<AuthUser> findAll() {
        return dao.findAll();
    }
    public Optional<AuthUser> findByEmail(String email){
        return dao.findByEmail(email);
    }
    public UUID signup(SignupDTO dto) {
        if (Objects.isNull(dto) || Objects.isNull(dto.getName()) || Objects.isNull(dto.getPhoneNumber())
                || Objects.isNull(dto.getPassword()) || Objects.isNull(dto.getEmail()))
            return null;
        AuthUser user = AuthUser.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber())
                .build();
        return save(user);
    }
    public Optional<AuthUser> login(LoginDTO dto) {
        if (Objects.isNull(dto.getEmail()) || Objects.isNull(dto.getPassword()))
            return Optional.empty();
        return Optional.ofNullable(dao.login(dto.getEmail(), dto.getPassword()));
    }

    public void makeAdmin(UUID id) {
        dao.makeAdmin(String.valueOf(id));
    }

    public void blockUser(UUID id) {
        dao.blockUser(String.valueOf(id));
    }
}
