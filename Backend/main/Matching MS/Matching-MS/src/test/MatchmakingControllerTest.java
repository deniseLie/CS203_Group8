import csd.backend.Matching.MS.MatchmakingController;
import csd.backend.Matching.MS.MatchmakingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchmakingController.class)
class MatchmakingControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MatchmakingService matchmakingService;

    @InjectMocks
    private MatchmakingController matchmakingController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(matchmakingController).build();
    }

    @Test
    void joinMatchmaking_Success() throws Exception {
        when(matchmakingService.checkForMatch(anyInt())).thenReturn(true);

        mockMvc.perform(post("/matchmaking/join")
                .param("playerName", "Player1")
                .param("rank", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Match created successfully."));

        verify(matchmakingService, times(1)).updatePlayerStatus("Player1", "queue");
        verify(matchmakingService, times(1)).checkForMatch(1);
    }

    @Test
    void processSqsMessages_Success() throws Exception {
        doNothing().when(matchmakingService).processSqsMessages();

        mockMvc.perform(post("/matchmaking/processSqs"))
                .andExpect(status().isOk())
                .andExpect(content().string("SQS Messages processed!"));

        verify(matchmakingService, times(1)).processSqsMessages();
    }
}
