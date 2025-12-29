package io.validate.sme.configs;

import com.couchbase.client.core.error.BucketNotFoundException;
import com.couchbase.client.core.error.UnambiguousTimeoutException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Getter
public class CouchbaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseConfig.class);

    @Value("${spring.couchbase.connection-string}")
    private String host;
    @Value("${spring.couchbase.username}")
    private String username;
    @Value("${spring.couchbase.password}")
    private String password;

    private static final String BUCKET_NAME = "ValidateAnalysis";

    @Bean(destroyMethod = "disconnect")
    Cluster getCouchbaseCluster() {
        Cluster cluster = null;
        try {
            LOGGER.debug("Connecting to Couchbase Cluster at: {}", host);
            cluster = Cluster.connect(host, username, password);
            cluster.waitUntilReady(Duration.ofSeconds(30L));
            return cluster;
        } catch (UnambiguousTimeoutException e) {
            LOGGER.warn("Connection to Couchbase cluster at {} timed out, but continuing with partial connectivity", host);
            return cluster;
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName());
            LOGGER.error("Error connecting to Couchbase cluster at host: {}", host);
            throw e;
        }
    }

    @Bean
    Bucket getCouchbaseBucket(Cluster cluster) {
        try {
            if(!cluster.buckets().getAllBuckets().containsKey(BUCKET_NAME)) {
                throw new BucketNotFoundException("Bucket with name " + BUCKET_NAME + " does not exist");
            }
            Bucket bucket = cluster.bucket(BUCKET_NAME);
            bucket.waitUntilReady(Duration.ofSeconds(30));
            return bucket;
        } catch (UnambiguousTimeoutException e) {
            LOGGER.error("Connection to Couchbase bucket at {} timed out.", BUCKET_NAME);
            throw e;
        } catch (BucketNotFoundException e) {
            LOGGER.error("Couchbase Bucket {} does not exist in the Cluster.", BUCKET_NAME);
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName());
            LOGGER.error("Error connecting to Couchbase bucket: {}", BUCKET_NAME);
            throw e;
        }
    }
}
