package com.ev.EvChargingStation.service;

import com.ev.EvChargingStation.dto.auth.AuthResponseDTO;
import com.ev.EvChargingStation.dto.auth.LoginRequestDTO;
import com.ev.EvChargingStation.dto.auth.LoginResponseDTO;
import com.ev.EvChargingStation.dto.auth.RegisterRequestDTO;
import com.ev.EvChargingStation.entity.User;
import com.ev.EvChargingStation.enums.Role;
import com.ev.EvChargingStation.exception.EmailAlreadyExistsException;
import com.ev.EvChargingStation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request){

        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw new EmailAlreadyExistsException("Email Already Exist" + request.getEmail());
        }

        User user = new User();
        user.setName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhoneNumber());
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setUserId(saved.getId());
        response.setFullName(saved.getName());
        response.setEmail(saved.getEmail());
        response.setRole(saved.getRole());

        return response;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        LoginResponseDTO response = new LoginResponseDTO();

        response.setToken(token);
        response.setType("Bearer");
        response.setUserId(user.getId());
        response.setFullName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}
