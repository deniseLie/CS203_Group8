package csd.backend.Account.MS.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import csd.backend.Account.MS.Model.AppUser;
import csd.backend.Account.MS.Model.AppUserRepository;
import csd.backend.Account.MS.Model.AppUserService;


@Controller
public class ContentController {

    private final AppUserService userService;

    @Autowired
    private AppUserRepository userRepository;

    public ContentController(AppUserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/req/login")
    public String login() {
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }

    // @GetMapping("/index")
    // public String index(Authentication authentication, Model model) {
    //     //If login by Google
    //     if (authentication.getPrincipal() instanceof OAuth2User) {
    //         OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    //         userService.processOAuthLogin(oAuth2User);        
    //     } else if (authentication.getPrincipal() instanceof UserDetails) {
    //         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    //         String email = userDetails.getUsername();
    //         Optional<AppUser> user = userRepository.findByEmail(email);
    //     }
    //     return "index";
    // }

    @GetMapping("/dashboard")
    public ResponseEntity<String> login (Authentication authentication, Model model) {
        //If login by Google
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            userService.processOAuthLogin(oAuth2User); 
            return ResponseEntity.status(HttpStatus.OK).body("HTTP Status:200. Google OAuth2 login successful!");   
        //Normal login       
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Optional<AppUser> user = userRepository.findByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body("HTTP Status:200. Login successful!");   
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("HTTP Status:401. Login unsuccessful!");   
        }
    }

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public String createUser(@RequestBody AppUser user){
        userService.registerUser(user);
        return "redirect:/req/login?registerSuccess";
    }
    

}
