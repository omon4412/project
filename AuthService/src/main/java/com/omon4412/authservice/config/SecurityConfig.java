package com.omon4412.authservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.time.Duration;

/**
 * Основная конфигурация Spring security.
 */
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@EnableJdbcHttpSession
@Slf4j
@Configuration
public class SecurityConfig {
    /**
     * Сервис пользователей.
     */
    private final UserDetailsService userService;
    /**
     * Сервисный интерфейс для шифрования паролей.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Реестр всех сессий.
     */
    private final SessionRegistry sessionRegistry;
    private static final int duration = 100;
    /**
     * Продолжительность сессии.
     */
    private static final Duration maxInactiveInterval = Duration.ofMinutes(duration);

    /**
     * Этот метод определяет набор конфигураций безопасности, используя предоставленный объект {@link HttpSecurity}.
     *
     * @param http Объект {@link HttpSecurity} для конфигурации.
     * @return Сконфигурированный {@link SecurityFilterChain} экземпляр.
     * @throws Exception Если во время настройки возникает ошибка.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .anonymous(AbstractHttpConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                .formLogin(form -> form.usernameParameter("usernameOrEmail").loginPage("/login").disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(l -> l.logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true))
                .securityContext((securityContext) -> securityContext.requireExplicitSave(false))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(2)
                        .sessionRegistry(sessionRegistry)
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/users/**").authenticated()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/todos/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/test").authenticated()
                        .requestMatchers("/test2").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }

    /**
     * Это кастомный AuthenticationProvider, реализованный в Spring Security.
     * Он опирается на UserDetailsService для поиска почты имени пользователя, пароля и GrantedAuthority.
     * DaoAuthenticationProvider идентифицирует пользователей, сравнивая почту, пароль, присланный в UsernamePasswordAuthenticationToken, с паролем, который был загружен UserDetailsService.
     *
     * @return Экземпляр DaoAuthenticationProvider.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new CustomAuthenticationProvider(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    /**
     * Конфигурирует реестр сессий для использования в приложении.
     * Этот метод создает и настраивает реестр сессий с использованием предоставленного объекта
     * {@link JdbcIndexedSessionRepository}. Устанавливается максимальный интервал неактивности сессии,
     * а также создается и возвращается экземпляр {@link SpringSessionBackedSessionRegistry}.
     *
     * @param sessionRepository объект {@link JdbcIndexedSessionRepository}, предоставляющий репозиторий сессий
     * @return экземпляр {@link SessionRegistry}, используемый для отслеживания активных сессий
     */
    @Bean
    public static SessionRegistry sessionRegistry(JdbcIndexedSessionRepository sessionRepository) {
        sessionRepository.setDefaultMaxInactiveInterval(maxInactiveInterval);
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    /**
     * Возвращает объект управления аутентификацией ({@link AuthenticationManager}), используемый в приложении.
     * <p>
     * Этот метод получает конфигурацию аутентификации и извлекает из неё объект управления аутентификацией.
     *
     * @param authenticationConfiguration объект {@link AuthenticationConfiguration}, предоставляющий конфигурацию аутентификации.
     * @return объект {@link AuthenticationManager}, управляющий аутентификацией в приложении.
     * @throws Exception если произойдет ошибка при получении объекта управления аутентификацией.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
