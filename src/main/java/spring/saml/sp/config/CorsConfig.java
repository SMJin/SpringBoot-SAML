package spring.saml.sp.config;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); // 쿠키와 세션 허용
        config.setAllowedOrigins(List.of("*"));  // 모든 도메인 허용
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));  // 모든 HTTP 메서드 허용
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0); // 가장 먼저 실행되도록 우선순위 설정
        return bean;
    }
}
