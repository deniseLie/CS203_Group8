#!/bin/bash

# Create DynamoDB table for Players
aws dynamodb create-table \
    --table-name Players \
    --attribute-definitions \
        AttributeName=playerId,AttributeType=S \
    --key-schema \
        AttributeName=playerId,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region us-east-1

# Insert item into DynamoDB with matchmaking columns
aws dynamodb put-item \
    --table-name Players \
    --item \
        '{
            "playerId": {"S": "Player1"},
            "email": {"S": "player1@example.com"},
            "queueStatus": {"S": "queue"},
            "rankId": {"N": "1"},
            "banUntil": {"N": "0"},
            "banCount": {"N": "0"}
        }' \
    --region us-east-1

# Insert another item (example: another player)
aws dynamodb put-item \
    --table-name Players \
    --item \
        '{
            "playerId": {"S": "Player2"},
            "email": {"S": "player2@example.com"},
            "queueStatus": {"S": "available"},
            "rankId": {"N": "2"},
            "banUntil": {"N": "0"},
            "banCount": {"N": "1"}
        }' \
    --region us-east-1

# Insert more items as needed for different players
# (Repeat the aws dynamodb put-item for more players if needed)

# Output to confirm items were added
echo "DynamoDB table and items added successfully!"

# Give executable permissions to the script (for local use) - in git bash
# chmod +x aws-commands.sh

# Run the script 
# bash ./aws-commands.sh