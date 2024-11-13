package csd.backend.Account.MS.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class HealthCheckController {

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}