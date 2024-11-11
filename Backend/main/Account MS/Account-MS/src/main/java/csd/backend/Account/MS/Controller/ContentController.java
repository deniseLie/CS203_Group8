package csd.backend.Account.MS.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import csd.backend.Account.MS.Model.Player;
import csd.backend.Account.MS.Repository.PlayerRepository;
import csd.backend.Account.MS.Service.PlayerService;


@Controller
public class ContentController {

    private final PlayerService userService;

    @Autowired
    private PlayerRepository userRepository;

    public ContentController(PlayerService userService) {
        this.userService = userService;
    }

}
