package csd.backend.Matching.MS;

import java.util.HashMap;

/**
 * This class demonstrates the creation of DynamoDB tables for:
 * 1. Players table
 * 2. Matches table
 *
 * Players Table Schema:
 * - playerName (String) 
 * - email (String, Primary Key)
 * - status (String) [queue / not queue]
 * - rank (rankId)
 *
 * Matches Table Schema:
 * - matchId (Number, Primary Key)
 * - playerIds (List of Strings) representing the IDs of 8 players
 *
 * These tables are created locally in a DynamoDB instance running on Docker.
 */

public class CreateDynamoTables {

    // ------------------------ Players Table -----------------------------------

    // CLI command : create the Players table in local DynamoDB.
    // aws dynamodb create-table --table-name Players --attribute-definitions AttributeName=playerName,AttributeType=S --key-schema AttributeName=playerName,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --endpoint-url http://localhost:8000

    // aws dynamodb create-table 
    //     --table-name Players
    //     --attribute-definitions AttributeName=playerName,AttributeType=S
    //     --key-schema AttributeName=playerName, KeyType=HASH
    //     --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
    //     --endpoint-url http://localhost:8000

    // Delete Table
    // aws dynamodb delete-table --table-name Players --endpoint-url http://localhost:8000
    
    // aws dynamodb delete-table
    //     --table-name Players
    //     --endpoint-url http://localhost:8000
    
    // Populate
    // aws dynamodb put-item --table-name Players --item "{\"playerName\": {\"S\": \"Player1\"}, \"status\": {\"S\": \"queue\"}, \"rankId\": {\"N\": \"1\"}, \"email\": {\"S\": \"player1@example.com\"}}" --endpoint-url http://localhost:8000
    // aws dynamodb put-item --table-name Players --item "{\"playerName\": {\"S\": \"Player2\"}, \"status\": {\"S\": \"queue\"}, \"rankId\": {\"N\": \"2\"}, \"email\": {\"S\": \"player2@example.com\"}}" --endpoint-url http://localhost:8000
    // aws dynamodb put-item --table-name Players --item "{\"playerName\": {\"S\": \"Player3\"}, \"status\": {\"S\": \"not queue\"}, \"rankId\": {\"N\": \"3\"}, \"email\": {\"S\": \"player3@example.com\"}}" --endpoint-url http://localhost:8000

    
    // ------------------------ Matches Table ----------------------------------

    //  CLI command : create the Matches table in local DynamoDB.
    
    // aws dynamodb create-table
    // aws dynamodb create-table --table-name Matches --attribute-definitions AttributeName=matchId,AttributeType=N --key-schema AttributeName=matchId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --endpoint-url http://localhost:8000
    
    //     --table-name Matches
    //     --attribute-definitions AttributeName=matchId,AttributeType=N
    //     --key-schema AttributeName=matchId,KeyType=HASH
    //     --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
    //     --endpoint-url http://localhost:8000
    
    // Delete Matches Table
    // aws dynamodb delete-table --table-name Matches --endpoint-url http://localhost:8000
   
    // Populate Matches Table
    // aws dynamodb put-item --table-name Matches --item "{\"matchId\": {\"N\": \"1\"}, \"playerIds\": {\"L\": [{\"S\": \"Player1\"}, {\"S\": \"Player2\"}, {\"S\": \"Player3\"}, {\"S\": \"Player4\"}, {\"S\": \"Player5\"}, {\"S\": \"Player6\"}, {\"S\": \"Player7\"}, {\"S\": \"Player8\"}]}}" --endpoint-url http://localhost:8000

    // aws dynamodb put-item --table-name Matches --item "{\"matchId\": {\"N\": \"2\"}, \"playerIds\": {\"L\": [{\"S\": \"Player1\"}, {\"S\": \"Player2\"}, {\"S\": \"Player3\"}, {\"S\": \"Player4\"}, {\"S\": \"Player5\"}, {\"S\": \"Player6\"}, {\"S\": \"Player7\"}, {\"S\": \"Player8\"}]}}" --endpoint-url http://localhost:8000

    
    // --------------------- List and Verify Tables -------------------------------
    /**
     * List all tables in your local DynamoDB : Verifiy tables
     *
     * aws dynamodb list-tables --endpoint-url http://localhost:8000 --output json
     */
}
