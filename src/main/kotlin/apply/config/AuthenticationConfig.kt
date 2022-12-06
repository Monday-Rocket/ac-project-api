package apply.config

import apply.security.LoginUserResolver
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
//class AuthenticationConfig : WebMvcConfigurer {
//
//    @Bean
//    fun firebaseAuth(): FirebaseAuth {
//        FirebaseApp.initializeApp()
//        return FirebaseAuth.getInstance()
//    }
//
//    @Bean
//    fun loginUserResolver(): LoginUserResolver {
//        return LoginUserResolver(firebaseAuth())
//    }
//
//    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
//        resolvers.add(loginUserResolver())
//    }
//
//}
