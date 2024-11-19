package csd.backend.Account.MS.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerRegisterExisted.class)
    public ResponseEntity<String> handlePlayerRegisterExisted(PlayerRegisterExisted ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlayerStatsNotFoundException.class)
    public ResponseEntity<String> handlePlayerStatsNotFoundException(PlayerStatsNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerChampionStatsNotFoundException.class)
    public ResponseEntity<String> handlePlayerChampionStatsNotFoundException(PlayerChampionStatsNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RankNotFoundException.class)
    public ResponseEntity<String> handleRankNotFoundException(RankNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle RankNotFoundException
    @ExceptionHandler(RankPointNotFoundException.class)
    public ResponseEntity<String> hanldeRankPointNotFoundException(RankPointNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); 
    }
}
