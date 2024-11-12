package csd.backend.Admin.Model.Tournament;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentId;

    private int tournamentSize;
    private boolean status;
    private LocalDateTime timestampStart;
    private String timeStampEnd;

    // Getters and setters
    public Long getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }
    public int getTournamentSize() {
        return tournamentSize;
    }
    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
    public boolean getStatus(){
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
    public void markAsDone(){
        if (status == true){
            timeStampEnd = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        }
    }
    public LocalDateTime getTimestampStart() {
        return timestampStart;
    }
    public void setTimestampStart(LocalDateTime timestampStart) {
        this.timestampStart = timestampStart;
    }
    public String getTimestampEnd() {
        return timeStampEnd;
    }
    public void setTimestampEnd(String timeStampEnd) {
        this.timeStampEnd = timeStampEnd;
    }
}