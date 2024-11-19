package csd.backend.Penalty.MS.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.*;

import csd.backend.Penalty.MS.utils.*;

@Service
public class PenaltyService {

    private final SqsService sqsService;
    private final PlayerService playerService;

    @Autowired
    public PenaltyService(SqsService sqsService, PlayerService playerService) {
        this.sqsService = sqsService;
        this.playerService = playerService;
    }

    private static final Logger logger = LoggerFactory.getLogger(PenaltyService.class);
    private static final int BASE_BAN_DURATION_IN_SECONDS = 300; // 5 minutes

    // Ban player for a specified duration based on their ban count
    public ResponseEntity<Map<String, Object>> banPlayer(Long playerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Calculate
            int banCount = playerService.getBanCount(playerId);
            int dynamicDuration = calculateBanDuration(banCount);
            Long banEndTime = System.currentTimeMillis() + (dynamicDuration * 1000);

            // Excecute
            updatePlayerBanStatus(playerId, banCount, banEndTime);
            logger.info("Player {} banned for {} seconds (dynamic duration)", playerId, dynamicDuration);

            // Blast to other microservices
            sendBanMessageToQueue(playerId, banEndTime);

            // Response
            response.put("message", "Player banned successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error occurred while banning player: {}", playerId, e);
            response.put("message", "Error banning player.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check player status and return remaining ban time if banned
    public ResponseEntity<Map<String, Object>> checkPlayerBanStatus(Long playerId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, AttributeValue> player = playerService.getPlayerDetails(playerId);

            // Check if the player exists
            if (player == null || !player.containsKey("playerId") || !player.containsKey("queueStatus") || !player.containsKey("banUntil")) {
                logger.warn("Player {} not found or missing required fields", playerId);
                return ResponseUtil.createNotFoundResponse("Player not found");
            }

            // Calculate the remaining ban time
            long remainingTime = calculateRemainingBanTime(player);
            
            // Prepare the response
            prepareResponse(response, player, remainingTime);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error occurred while checking player status: {}", playerId, e);
            return ResponseUtil.createInternalServerErrResponse("Error checking player status");
        }
    }

    // Calculate the ban duration based on the player's previous ban count
    private int calculateBanDuration(int banCount) {
        return BASE_BAN_DURATION_IN_SECONDS + (banCount * 60); 
    }

    // Update the player's ban status, including queue status, ban time, and incrementing the ban count
    private void updatePlayerBanStatus(Long playerId, int banCount, Long banEndTime) {
        playerService.updatePlayerStatus(playerId, "banned");
        playerService.updateBanUntil(playerId, banEndTime);
        playerService.updateBanCount(playerId, (banCount + 1));
    }
    
    // Helper method to calculate remaining ban time
    private long calculateRemainingBanTime(Map<String, AttributeValue> player) {
        long banUntil = Long.parseLong(player.get("banUntil").n());
        return (banUntil > System.currentTimeMillis()) ? banUntil - System.currentTimeMillis() : 0;
    }

    // Helper method to prepare the response with player details
    private void prepareResponse(Map<String, Object> response, Map<String, AttributeValue> player, long remainingTime) {
        response.put("playerId", player.get("playerId").n());
        response.put("queueStatus", player.get("queueStatus").n());
        response.put("remainingTime", remainingTime);
    }

    // Send a message to the matchmaking queue regarding the ban
    private void sendBanMessageToQueue(Long playerId, Long banEndTime) {
        String messageBody = playerId + ", until: " + new Date(banEndTime);
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("actionType", MessageAttributeValue.builder()
                .stringValue("ban")
                .dataType("String")
                .build());

        sqsService.sendMessageToQueue("matchmaking", messageBody, messageAttributes);
        logger.info("Message sent to matchmaking queue: {}", messageBody);
    }

    // Scheduled task to unban players whose ban duration has expired
    @Scheduled(fixedRate = 60000) // Check every minute
    public void unbanExpiredPlayers() {

        // Get players with "banned" status
        List<Map<String, AttributeValue>> bannedPlayers = playerService.getBannedPlayers();

        // Loop through players
        for (Map<String, AttributeValue> player : bannedPlayers) {
            Long playerId = Long.parseLong(player.get("playerId").n());
            Long banUntil = Long.parseLong(player.get("banUntil").n());

            // Check if the ban has expired
            if (banUntil <= System.currentTimeMillis()) {
                playerService.updatePlayerStatus(playerId, "available"); 
                logger.info("Player {} has been unbanned and is now available", playerId);
            }
        }
    }

    // Add a player 
    public void addPlayerToPool(Long playerId, String queueStatus) {
        playerService.addPlayerToPool(playerId, queueStatus);
    }

    // Delete player 
    public void deletePlayer(Long playerId) {
        playerService.deletePlayer(playerId);
    }

    // Send a message to the Penalty Queue
    public void sendMessageToPenaltyQueue(String messageBody, Map<String, MessageAttributeValue> messageAttributes) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(sqsService.getQueueUrl("penalty"))
                .messageBody(messageBody)
                .messageAttributes(messageAttributes)
                .messageGroupId("PenaltyServiceGroup") // Required for FIFO queues
                .messageDeduplicationId(String.valueOf(messageBody.hashCode())) // Ensures unique messages in FIFO
                .build();

        SendMessageResponse response = sqsService.getSqsClient().sendMessage(sendMsgRequest);
        System.out.println("Message sent to Penalty Queue with MessageId: " + response.messageId());
    }
}
