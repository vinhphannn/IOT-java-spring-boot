package com.phanvanvinh.doan.config;

import com.phanvanvinh.doan.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; // 1. Inject bộ lọc JWT vào

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Mã hóa mật khẩu
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF
                .cors(cors -> cors.configure(http)) // Kích hoạt CORS

                // 2. QUAN TRỌNG: Chuyển sang chế độ Stateless (Không lưu session server)
                // Vì mình dùng JWT nên không cần JSESSIONID nữa.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Các API này cho phép truy cập thoải mái (Không cần Token)
                        .requestMatchers("/api/auth/**").permitAll() // Login, Signup, Social Login
                        .requestMatchers("/ws/**").permitAll() // WebSocket (để sau này kết nối realtime)

                        // Các API còn lại (bao gồm /api/user/setup) BẮT BUỘC phải có Token
                        .anyRequest().authenticated());

        // 3. Đặt bộ lọc JWT đứng trước bộ lọc Username/Password mặc định
        // Nghĩa là: Có request đến -> Check Token trước -> Nếu OK thì cho qua -> Nếu
        // không thì chặn.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}