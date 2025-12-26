package com.phanvanvinh.doan.controller;

import com.phanvanvinh.doan.dto.request.LoginRequest;
import com.phanvanvinh.doan.dto.request.SignupRequest;
import com.phanvanvinh.doan.dto.response.JwtResponse;
import com.phanvanvinh.doan.model.AuthProvider;
import com.phanvanvinh.doan.model.User;
import com.phanvanvinh.doan.repository.UserRepository;
import com.phanvanvinh.doan.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// ... Các import cũ
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.phanvanvinh.doan.dto.request.SocialLoginRequest;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    // 1. API Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Authenticate bằng EMAIL
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal(); // Ép kiểu về User model của mình

        String jwt = jwtUtils.generateJwtToken(user);
        boolean isSetup = (user.getHouseName() != null && !user.getHouseName().isEmpty());
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(), // getUsername() chính là email
                user.getEmail(),
                isSetup));
    }

    // 2. API Đăng ký
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Tạo user mới chỉ với Email + Pass
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setProvider(AuthProvider.LOCAL);
        user.setHouseName("My Smart Home"); // Tên mặc định

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @Value("${google.clientId}")
    private String googleClientId;

    @PostMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody SocialLoginRequest request) {
        try {
            String email = "";
            String name = "";
            String avatarUrl = "";

            // 1. Kiểm tra Token của GOOGLE
            if ("GOOGLE".equalsIgnoreCase(request.getProvider())) {

                // <--- QUAN TRỌNG: PHẢI CÓ CHỮ ELSE NÀY
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                        new GsonFactory())
                        // .setAudience(Collections.singletonList(googleClientId))
                        .build();

                // Dòng này gây lỗi nếu token là fake mà không nằm trong else
                GoogleIdToken idToken = verifier.verify(request.getToken());

                if (idToken != null) {
                    GoogleIdToken.Payload payload = idToken.getPayload();
                    email = payload.getEmail();
                    name = (String) payload.get("name");
                    avatarUrl = (String) payload.get("picture");
                } else {
                    return ResponseEntity.badRequest().body("Invalid Google Token");
                }

            }
            // TODO: Sau này thêm else if (FACEBOOK) ở đây
            else {
                return ResponseEntity.badRequest().body("Provider not supported yet");
            }

            // 2. Xử lý User trong Database (Find or Create)
            String finalEmail = email;
            User user = userRepository.findByEmail(email).orElseGet(() -> {
                // Nếu chưa có thì TẠO MỚI (Đăng ký ngầm)
                User newUser = new User();
                newUser.setEmail(finalEmail);
                newUser.setProvider(AuthProvider.GOOGLE);
                newUser.setPassword(passwordEncoder.encode("SOCIAL_LOGIN_PASS")); // Set pass ngẫu nhiên để không bị lỗi
                                                                                  // null
                newUser.setHouseName("My Smart Home");
                return userRepository.save(newUser);
            });

            // 3. Nếu user đã tồn tại nhưng trước đó đăng ký thường, giờ muốn link Google?
            // Tạm thời mình cho qua, chỉ cần lấy được user là được.

            // Cập nhật thông tin mới nhất từ Google (Avatar, Tên) nếu cần
            if (user.getFullName() == null)
                user.setFullName(name);
            if (user.getAvatarUrl() == null)
                user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

            // 4. Sinh JWT Token trả về (giống hệt API login thường)
            String jwt = jwtUtils.generateJwtToken(user);
            boolean isSetup = (user.getHouseName() != null && !user.getHouseName().isEmpty());
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    user.getId(),
                    user.getEmail(),
                    isSetup));

        } catch (Exception e) {
            e.printStackTrace(); // <--- THÊM DÒNG NÀY (Để in lỗi đỏ ra màn hình console)
            return ResponseEntity.badRequest().body("Error during social login: " + e.getMessage());
        }
    }
}