package csd.backend.Matching.MS.config;

// import java.util.HashMap;

/**
 * This class demonstrates the creation of DynamoDB tables for:
 * 1. Players table
 * 2. Matches table
 *
 * Players Table Schema:
 * - playerName (String)
 * - email (String, Primary Key)
 * - queueStatus (String) [queue / not queue]
 * - rankId (int)
 *
 * Matches Table Schema:
 * - matchId (Number, Primary Key)
 * - playerIds (List of Strings) representing the IDs of 8 players
 *
 * These tables are created locally in a DynamoDB instance running on Docker.
 */
public class CreateDynamoTables {

    // ------------------------ Players Table -----------------------------------

    /**
     * CLI command to create the Players table in local DynamoDB:
     */
    // aws dynamodb create-table --table-name Players \
    //     --attribute-definitions AttributeName=playerName,AttributeType=S \
    //     --key-schema AttributeName=playerName,KeyType=HASH \
    //     --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    //     --endpoint-url http://localhost:8000

        // One-liner version for windows
        // aws dynamodb create-table --table-name Players --attribute-definitions AttributeName=playerName,AttributeType=S --key-schema AttributeName=playerName,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --endpoint-url http://localhost:8000;

    /**
     * CLI command to delete the Players table:
     */
    // aws dynamodb delete-table --table-name Players --endpoint-url http://localhost:8000

    /**
     * CLI command to populate the Players table:
     */
    // aws dynamodb put-item --table-name Players \
    //     --item "{\"email\": {\"S\": \"player1@example.com\"}, \"playerName\": {\"S\": \"Player1\"}, \"queueStatus\": {\"S\": \"queue\"}, \"rankId\": {\"N\": \"1\"}}" \
    //     --endpoint-url http://localhost:8000
        
        // One-liner version for windows
        // aws dynamodb put-item --table-name Players --item '{"email": {"S": "player1@example.com"}, "playerName": {"S": "Player1"}, "queueStatus": {"S": "queue"}, "rankId": {"N": "1"}}' --endpoint-url http://localhost:8000;

    // ------------------------ Matches Table ----------------------------------

    /**
     * CLI command to create the Matches table in local DynamoDB:
     */
    // aws dynamodb create-table --table-name Matches \
    //     --attribute-definitions AttributeName=matchId,AttributeType=N \
    //     --key-schema AttributeName=matchId,KeyType=HASH \
    //     --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    //     --endpoint-url http://localhost:8000

        // One-liner version for windows
        // aws dynamodb create-table --table-name Matches --attribute-definitions AttributeName=matchId,AttributeType=N --key-schema AttributeName=matchId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --endpoint-url http://localhost:8000;
    
        /**
     * CLI command to delete the Matches table:
     */
    // aws dynamodb delete-table --table-name Matches --endpoint-url http://localhost:8000

    /**
     * CLI command to populate the Matches table:
     */
    // aws dynamodb put-item --table-name Matches \
    //     --item "{\"matchId\": {\"N\": \"1\"}, \"playerIds\": {\"L\": [{\"S\": \"Player1\"}, {\"S\": \"Player2\"}, {\"S\": \"Player3\"}, {\"S\": \"Player4\"}, {\"S\": \"Player5\"}, {\"S\": \"Player6\"}, {\"S\": \"Player7\"}, {\"S\": \"Player8\"}]}}" \
    //     --endpoint-url http://localhost:8000

        // One-liner version for windows
        // aws dynamodb put-item --table-name Matches --item '{"matchId": {"N": "1"}, "playerIds": {"L": [{"S": "Player1"}, {"S": "Player2"}, {"S": "Player3"}, {"S": "Player4"}, {"S": "Player5"}, {"S": "Player6"}, {"S": "Player7"}, {"S": "Player8"}]}}' --endpoint-url http://localhost:8000;

    // Additional Populate Command Example:
    // aws dynamodb put-item --table-name Matches \
    //     --item "{\"matchId\": {\"N\": \"2\"}, \"playerIds\": {\"L\": [{\"S\": \"Player1\"}, {\"S\": \"Player2\"}, {\"S\": \"Player3\"}, {\"S\": \"Player4\"}, {\"S\": \"Player5\"}, {\"S\": \"Player6\"}, {\"S\": \"Player7\"}, {\"S\": \"Player8\"}]}}" \
    //     --endpoint-url http://localhost:8000

        // One-liner version for windows

    // --------------------- List and Verify Tables -------------------------------

    /**
     * CLI command to list all tables in your local DynamoDB:
     * Verify the tables with:
     * aws dynamodb list-tables --endpoint-url http://localhost:8000 --output json
     */

    // --------------------- Build Docker Image -----------------------------------

    /**
     * CLI command to build Docker image:
     * docker build -t match-making-ms .
     */
}
