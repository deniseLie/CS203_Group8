// package csd.backend.Account.MS;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;

// import csd.backend.Account.MS.Model.Player.*;
// import csd.backend.Account.MS.Service.*;
// import csd.backend.Account.MS.Service.Player.*;
// import software.amazon.awssdk.services.sqs.model.Message;
// import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

// import static org.mockito.Mockito.*;

// import java.util.*;

// class AccountListenerTest {

//     @Mock
//     private SqsService sqsService;

//     @Mock
//     private PlayerService playerService;

//     @InjectMocks
//     private AccountListener accountListener;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testProcessAddPlayer() {
//         String messageBody = "{ \"playerId\": \"12345\", \"username\": \"player1\" }";
//         Map<String, MessageAttributeValue> attributes = new HashMap<>();
//         attributes.put("actionType", MessageAttributeValue.builder().stringValue("addPlayer").build());

//         // Simulate receiving a message from the queue
//         accountListener.processAccountMessage(messageBody, attributes);

//         // Verify that the playerService registerUser method is called
//         verify(playerService, times(1)).registerUser(any(Player.class));
//     }

//     @Test
//     void testProcessAddTournament() {
//         String messageBody = "{ \"playerId\": \"12345\", \"championId\": \"101\", \"kdRate\": \"2.0\", \"finalPlacement\": \"1\", \"rankPoints\": \"50\", \"isWin\": \"true\" }";
//         Map<String, MessageAttributeValue> attributes = new HashMap<>();
//         attributes.put("actionType", MessageAttributeValue.builder().stringValue("addMatch").build());

//         // Simulate receiving a message from the queue
//         accountListener.processAccountMessage(messageBody, attributes);

        // Verify that the tournamentService createAndSaveTournament method is called
    //     verify(tournamentService, times(1)).createAndSaveTournament(anyMap());
    // }

//     @Test
//     void testMessageWithoutActionType() {
//         String messageBody = "{ \"playerId\": \"12345\", \"username\": \"player1\" }";
//         Map<String, MessageAttributeValue> attributes = new HashMap<>();

//         // Simulate receiving a message without actionType
//         accountListener.processAccountMessage(messageBody, attributes);

        // Since actionType is missing, no methods should be called
    //     verify(playerService, times(0)).registerUser(any(Player.class));
    //     verify(tournamentService, times(0)).createAndSaveTournament(anyMap());
    // }

//     @Test
//     void testErrorInParsingPlayer() {
//         String messageBody = "{ \"playerId\": \"abc\", \"username\": \"player1\" }"; // Invalid playerId format
//         Map<String, MessageAttributeValue> attributes = new HashMap<>();
//         attributes.put("actionType", MessageAttributeValue.builder().stringValue("addPlayer").build());

//         // Simulate receiving a message with invalid playerId
//         accountListener.processAccountMessage(messageBody, attributes);

//         // Verify that no player registration occurred due to parsing failure
//         verify(playerService, times(0)).registerUser(any(Player.class));
//     }
// }
