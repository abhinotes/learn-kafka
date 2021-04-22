package com.abhinotes.learn.kafka.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class TopicVerification {

    private static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    private final String brokerHosts;

    public TopicVerification(@Value("${bootstrap-servers}") String brokerHosts) {
        this.brokerHosts = brokerHosts;
    }

    /**
     * Method to find is a provided topic exists
     * @param topic
     * @return tru if topic exists, false otherwise
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public boolean topicExists(String topic) throws ExecutionException, InterruptedException {

        Properties prop = new Properties();
        prop.setProperty(BOOTSTRAP_SERVERS, brokerHosts);
        AdminClient admin = AdminClient.create(prop);
        return
                admin.listTopics().names().get().stream()
                        .anyMatch(topicName -> topicName.equalsIgnoreCase(topic));

    }


}
