package csd.backend.Matching.MS;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.ResponseBody;

import csd.backend.Matching.MS.Player.Player;
import csd.backend.Matching.MS.Player.PlayerRepository; 

@Controller
public class MatchingMSController {
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/players")
    public @ResponseBody Iterable<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
}
