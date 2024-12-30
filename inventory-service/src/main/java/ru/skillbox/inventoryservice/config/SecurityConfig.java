package ru.skillbox.inventoryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomerSecurityFilter filter = new CustomerSecurityFilter();

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET, "/category/view/**").permitAll()
                .antMatchers(HttpMethod.GET, "/product/view/**").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/prometheus").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(filter, BasicAuthenticationFilter.class);

        return http.build();
    }
}
