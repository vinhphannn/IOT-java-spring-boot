package com.phanvanvinh.doan.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.phanvanvinh.doan.dto.request.LoginRequest;
import com.phanvanvinh.doan.dto.request.SignupRequest;
import com.phanvanvinh.doan.dto.request.SocialLoginRequest;
import com.phanvanvinh.doan.dto.response.JwtResponse;
import com.phanvanvinh.doan.model.AuthProvider;
import com.phanvanvinh.doan.model.House;
import com.phanvanvinh.doan.model.User;
import com.phanvanvinh.doan.repository.HouseRepository; // <--- Import mới
import com.phanvanvinh.doan.repository.UserRepository;
import com.phanvanvinh.doan.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HouseRepository houseRepository; // <--- Cần thêm cái này để check Setup

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${google.clientId}")
    private String googleClientId;

    // 1. API Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(user);

        // LOGIC MỚI: Check xem user đã có nhà nào chưa?
        List<House> houses = houseRepository.findByUserId(user.getId());
        boolean isSetup = !houses.isEmpty(); // Nếu list nhà không rỗng -> Đã setup

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getEmail(),
                isSetup));
    }

    // 2. API Đăng ký
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setProvider(AuthProvider.LOCAL);

        // LOGIC MỚI: Không set houseName nữa (để user tự setup sau)
        // user.setHouseName("My Smart Home"); <-- BỎ DÒNG NÀY

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    // 3. API Social Login
    @PostMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody SocialLoginRequest request) {
        try {
            String email = "";
            String name = "";
            String avatarUrl = "";

            if ("GOOGLE".equalsIgnoreCase(request.getProvider())) {
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                        new GsonFactory())
                        // .setAudience(Collections.singletonList(googleClientId))
                        .build();

                GoogleIdToken idToken = verifier.verify(request.getToken());

                if (idToken != null) {
                    GoogleIdToken.Payload payload = idToken.getPayload();
                    email = payload.getEmail();
                    name = (String) payload.get("name");
                    avatarUrl = (String) payload.get("picture");
                } else {
                    return ResponseEntity.badRequest().body("Invalid Google Token");
                }
            } else {
                return ResponseEntity.badRequest().body("Provider not supported yet");
            }

            String finalEmail = email;
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(finalEmail);
                newUser.setProvider(AuthProvider.GOOGLE);
                newUser.setPassword(passwordEncoder.encode("SOCIAL_LOGIN_PASS"));
                // LOGIC MỚI: Không tạo nhà mặc định nữa
                return userRepository.save(newUser);
            });

            // Update thông tin mới nhất
            if (user.getFullName() == null)
                user.setFullName(name);
            if (user.getAvatarUrl() == null)
                user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

            String jwt = jwtUtils.generateJwtToken(user);

            // LOGIC MỚI: Check isSetup
            List<House> houses = houseRepository.findByUserId(user.getId());
            boolean isSetup = !houses.isEmpty();

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    user.getId(),
                    user.getEmail(),
                    isSetup));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}