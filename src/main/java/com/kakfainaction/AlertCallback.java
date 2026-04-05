package com.kakfainaction;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class AlertCallback implements Callback {
    public AlertCallback() {
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            System.out.printf("Error sending message: " + "offset = %d, topic = %s, timestamp = % Tc % n", metadata.offset(), metadata.topic(), metadata.timestamp());
        }
    }
}
