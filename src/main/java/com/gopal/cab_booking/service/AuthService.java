package com.gopal.cab_booking.service;

import com.gopal.cab_booking.dto.LoginRequest;
import com.gopal.cab_booking.dto.LoginResponse;
import com.gopal.cab_booking.dto.RegisterDriverRequest;
import com.gopal.cab_booking.dto.RegisterRiderRequest;
import com.gopal.cab_booking.dto.UserResponse;
import com.gopal.cab_booking.entity.Driver;
import com.gopal.cab_booking.entity.Role;
import com.gopal.cab_booking.entity.User;
import com.gopal.cab_booking.exception.EmailAlreadyExistsException;
import com.gopal.cab_booking.exception.InvalidCredentialsException;
import com.gopal.cab_booking.repository.DriverRepository;
import com.gopal.cab_booking.repository.UserRepository;
import com.gopal.cab_booking.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void registerRider(RegisterRiderRequest req) {
        createUser(req.getName(), req.getEmail(), req.getPhone(),
                req.getPassword(), Role.RIDER);
    }

    @Transactional
    public void registerDriver(RegisterDriverRequest req) {
        User user = createUser(req.getName(), req.getEmail(), req.getPhone(),
                req.getPassword(), Role.DRIVER);

        Driver driver = Driver.builder()
                .user(user)
                .vehicleModel(req.getVehicleModel().trim())
                .vehicleNumber(req.getVehicleNumber().trim())
                .licenseNumber(req.getLicenseNumber().trim())
                .build();
        driverRepository.save(driver);
    }

    public LoginResponse login(LoginRequest req) {
        String email = req.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new LoginResponse(token);
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
        return UserResponse.from(user);
    }

    private User createUser(String name, String email, String phone,
                            String rawPassword, Role role) {
        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = User.builder()
                .name(name.trim())
                .email(normalizedEmail)
                .phone(phone)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();

        return userRepository.save(user);
    }
}