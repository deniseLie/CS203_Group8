package csd.backend.Penalty.MS.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/penalty")
public class HealthCheckController {
    
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
    
}
