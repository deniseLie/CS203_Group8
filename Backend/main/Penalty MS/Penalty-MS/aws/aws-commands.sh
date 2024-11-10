#!/bin/bash

# Create DynamoDB table
aws dynamodb create-table \
    --table-name Players \
    --attribute-definitions \
        AttributeName=playerId,AttributeType=S \
    --key-schema \
        AttributeName=playerId,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region us-east-1

# Insert item into DynamoDB
aws dynamodb put-item \
    --table-name Players \
    --item \
        '{"playerId": {"S": "Player1"}, "email": {"S": "player1@example.com"}, "queueStatus": {"S": "available"}, "banUntil": {"N": "0"}, "banCount": {"N": "0"}}' \
    --region us-east-1

# Give executable permissions
# chmod +x aws-commands.sh
