package radiata.service.gateway.core.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackResource {

    @GetMapping("/Fallback")
    public String Fallback() {
        return "Fallback Controller...!";
    }


    @PostMapping("/Fallback")
    public String Fallback2() {
        return "Fallback Controller...!";
    }
}
