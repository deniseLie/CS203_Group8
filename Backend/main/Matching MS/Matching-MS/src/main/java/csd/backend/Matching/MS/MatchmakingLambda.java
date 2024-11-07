// package csd.backend.Matching.MS;

// import com.amazonaws.services.lambda.runtime.Context;
// import com.amazonaws.services.lambda.runtime.RequestHandler;
// import com.amazonaws.services.lambda.runtime.events.SQSEvent;
// import org.json.JSONObject;
// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import software.amazon.awssdk.services.sqs.SqsClient;

// import java.util.ArrayList;
// import java.util.List;

// public class MatchmakingLambda implements RequestHandler<SQSEvent, String> {

//     // Injecting AWS SDK clients into the service
//     private final MatchmakingService matchmakingService;

//     private static final int MAX_PLAYERS = 8;

//     public MatchmakingLambda() {
//         // Instantiate AWS SDK clients
//         DynamoDbClient dynamoDbClient = DynamoDbClient.builder().build();
//         SqsClient sqsClient = SqsClient.builder().build();
        
//         // Instantiate MatchmakingService with required dependencies
//         matchmakingService = new MatchmakingService(dynamoDbClient, sqsClient);
//     }

//     @Override
//     public String handleRequest(SQSEvent event, Context context) {
//         List<String> createdMatches = new ArrayList<>();

//         for (SQSEvent.SQSMessage sqsMessage : event.getRecords()) {
//             // Parse SQS message
//             JSONObject playerData = new JSONObject(sqsMessage.getBody());
//             String playerName = playerData.getString("playerName");
//             String email = playerData.getString("email");
//             String status = "queue";
//             int rankId = 1; // Example rankId, could be passed dynamically

//             // Add player to pool
//             matchmakingService.addPlayerToPool(playerName, email, status, rankId);

//             // Check if there are enough players to form a match
//             List<String> matchedPlayers = matchmakingService.checkForMatches(rankId, MAX_PLAYERS);

//             if (matchedPlayers.size() == MAX_PLAYERS) {
//                 // Create match and remove players from queue
//                 matchmakingService.createMatch(matchedPlayers);
//                 matchmakingService.removePlayersFromQueue(matchedPlayers);

//                 createdMatches.add("Match created with players: " + matchedPlayers);

//                 // Delete the message from the queue after processing
//                 matchmakingService.deleteMessageFromQueue(sqsMessage.getReceiptHandle(), "https://sqs.ap-southeast-1.amazonaws.com/YOUR_ACCOUNT_ID/Message_Bus.fifo");
//             }
//         }

//         return createdMatches.isEmpty() ? "No matches created" : String.join("\n", createdMatches);
//     }
// }
