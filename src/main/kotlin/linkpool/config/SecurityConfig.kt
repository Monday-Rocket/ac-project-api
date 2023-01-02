package linkpool.config

import linkpool.security.ProdLoginUserResolver
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@EnableWebSecurity
@Profile("prod")
class SecurityConfig : WebSecurityConfigurerAdapter(), WebMvcConfigurer {

    override fun configure(http: HttpSecurity) {
        http {
            headers {
                frameOptions { disable() }
            }
            csrf { disable() }

        }
    }

    @Bean
    fun firebaseAuth(): FirebaseAuth {
        FirebaseApp.initializeApp()
        return FirebaseAuth.getInstance()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun loginUserResolver(): ProdLoginUserResolver {
        return ProdLoginUserResolver(firebaseAuth())
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginUserResolver())
    }

}
