package csd.backend.Matching.MS;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matchmaking")
public class HealthCheckController {

    @CrossOrigin
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
