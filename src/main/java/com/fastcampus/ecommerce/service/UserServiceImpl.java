package com.fastcampus.ecommerce.service;

import com.fastcampus.ecommerce.common.errors.*;
import com.fastcampus.ecommerce.entity.Role;
import com.fastcampus.ecommerce.entity.User;
import com.fastcampus.ecommerce.entity.UserRole;
import com.fastcampus.ecommerce.model.UserRegisterRequest;
import com.fastcampus.ecommerce.model.UserResponse;
import com.fastcampus.ecommerce.model.UserUpdateRequest;
import com.fastcampus.ecommerce.repository.RoleRepository;
import com.fastcampus.ecommerce.repository.UserRepository;
import com.fastcampus.ecommerce.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest registerRequest) {
        if (existsByUsername(registerRequest.getUsername())) {
            throw new UsernameNotFoundException("Username is already taken: ");
        }
        if (existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already taken: ");
        }

        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirmation())) {
            throw new BadRequestException("Password and password confirmation do not match");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .enabled(true)
                .password(encodedPassword)
                .build();

        userRepository.save(user);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));

        UserRole userRoleRelation = UserRole.builder()
                .id(new UserRole.UserRoleId(user.getUserId(), userRole.getRoleId()))
                .build();

        userRoleRepository.save(userRoleRelation);

        return UserResponse.fromUserAndRoles(user, List.of(userRole));
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        List<Role> roles = roleRepository.findByUserId(id);

        return UserResponse.fromUserAndRoles(user, roles);
    }

    @Override
    public UserResponse findByKeyword(String keyword) {
        User user = userRepository.findByKeyword(keyword)
                .orElseThrow(() -> new UserNotFoundException("User not found with username/email " + keyword));

        List<Role> roles = roleRepository.findByUserId(user.getUserId());

        return UserResponse.fromUserAndRoles(user, roles);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        // if user want to change it's password
        if (request.getCurrentPassword() != null && request.getNewPassword() != null) {
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new InvalidPasswordException("Current password is incorrect");
            }

            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encodedPassword);
        }

        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistsException("Username is already taken: " + request.getUsername());
            }

            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException("Email " + request.getEmail() + " is already taken");
            }

            user.setEmail(request.getEmail());
        }

        userRepository.save(user);
        List<Role> roles = roleRepository.findByUserId(user.getUserId());
        return UserResponse.fromUserAndRoles(user, roles);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        userRoleRepository.deleteByUserId(id);

        userRepository.delete(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
