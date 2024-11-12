#!/bin/bash

# Create DynamoDB table with playerId as a Long (Number)
aws dynamodb create-table \
    --table-name Players \
    --attribute-definitions \
        AttributeName=playerId,AttributeType=N \  # 'N' for Number (Long)
    --key-schema \
        AttributeName=playerId,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region us-east-1

# aws dynamodb create-table --table-name Players --attribute-definitions AttributeName=playerId,AttributeType=N --key-schema AttributeName=playerId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --region us-east-1


# Insert initial item into DynamoDB with playerId as a Long value
aws dynamodb put-item \
    --table-name Players \
    --item \
        '{"playerId": {"N": "1"}, "queueStatus": {"S": "available"}, "banUntil": {"N": "0"}, "banCount": {"N": "0"}}' \
    --region us-east-1

# Give executable permissions
# chmod +x aws-commands.sh
