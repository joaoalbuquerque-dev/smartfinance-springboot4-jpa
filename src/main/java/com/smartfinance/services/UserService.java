package com.smartfinance.services;

import com.smartfinance.dto.UserRequestDTO;
import com.smartfinance.dto.UserResponseDTO;
import com.smartfinance.entities.Role;
import com.smartfinance.entities.User;
import com.smartfinance.projections.UserDetailsProjection;
import com.smartfinance.repositories.RoleRepository;
import com.smartfinance.repositories.UserRepository;
import com.smartfinance.services.exceptions.DataBaseException;
import com.smartfinance.services.exceptions.ResourceNotFoundExcepetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
       return new UserResponseDTO(findEntityById(id));
    }

    @Transactional
    public UserResponseDTO insert(UserRequestDTO dto) {
        User user = new User(null, dto.name(), dto.email(), passwordEncoder.encode(dto.password()));
        Role role = roleRepository.findByAuthority("ROLE_USER")
                .orElseThrow(()-> new IllegalStateException("ROLE_USER not found"));
        user.addRole(role);
        return new UserResponseDTO(repository.save(user));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
       findEntityById(id);
       try {
           repository.deleteById(id);
       } catch (DataIntegrityViolationException e) {
           throw new DataBaseException(e.getMessage());
       }
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User entity = findEntityById(id);
        updateData(entity, dto);
        return new UserResponseDTO(repository.save(entity));
    }

    private User findEntityById(Long id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundExcepetion(id));
    }

    private void updateData(User entity, UserRequestDTO dto) {
        entity.setName(dto.name());

        if (dto.password() != null && !dto.password().isBlank()) {
            entity.setPassword(passwordEncoder.encode(dto.password()));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if (result.isEmpty()) {
            throw new ResourceNotFoundExcepetion("User not found");
        }
        User user = new User();
        user.setEmail(username);
        user.setPassword(result.getFirst().getPassword());
        for(UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return repository.findByEmail(username).get();
        }
        catch (Exception e) {
            throw new ResourceNotFoundExcepetion("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getMe() {
        User user = authenticated();
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateMe(UserRequestDTO dto) {
        User user = authenticated();
        updateData(user, dto);
        return new UserResponseDTO(repository.save(user));
    }
}
