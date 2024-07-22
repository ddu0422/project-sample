package per.study.sample.apis

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthApis(
    @Value("\${spring.profiles.active}")
    val active: String
) {

    @GetMapping("/health")
    fun health(): String {
        return active
    }
}
