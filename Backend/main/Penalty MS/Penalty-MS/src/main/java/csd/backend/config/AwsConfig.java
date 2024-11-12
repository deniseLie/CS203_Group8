package csd.backend.config;

// import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsConfig {

    @Value("${aws.access-key-id}")
    private String accessKey;
    
    @Value("${aws.secret-access-key}")
    private String secretKey;
    
    @Value("${aws.region}")
    private String region;

    // private final Dotenv dotenv = Dotenv.load();

    // private final String accessKey = dotenv.get("AWS_ACCESS_KEY_ID");
    // private final String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
    // private final String region = dotenv.get("AWS_REGION");

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build();
    }

    // @Bean
    // public SqsClient sqsClient() {
    //     return SqsClient.builder()
    //         .region(Region.of(region))
    //         .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
    //         .build();
    // }
}
