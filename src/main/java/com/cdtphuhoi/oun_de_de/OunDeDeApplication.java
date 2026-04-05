package com.cdtphuhoi.oun_de_de;

import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION;
import static org.apache.kafka.clients.producer.ProducerConfig.PARTITIONER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import com.cdtphuhoi.oun_de_de.configs.ApplicationConfig;
import com.kakfainaction.Alert;
import com.kakfainaction.AlertCallback;
import com.kakfainaction.alert_status;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@Import(ApplicationConfig.class)
public class OunDeDeApplication {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        SpringApplication.run(OunDeDeApplication.class, args);
        var props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put(ACKS_CONFIG, "all");
        props.put(RETRIES_CONFIG, "3");
        props.put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        var producer = new KafkaProducer<Long, Alert>(props);
        var alert = new Alert();
        alert.setSensorId(12345L);
        alert.setTime(Calendar.getInstance().getTimeInMillis());
        alert.setStatus(alert_status.Critical);
        System.out.println(alert.toString());

        var producerRecord = new ProducerRecord<Long, Alert>("avrotest", alert.getSensorId(), alert);
        RecordMetadata result = producer.send(producerRecord).get();
        System.out.printf("offset = %d, topic = %s, timestamp = %Tc %n", result.offset(), result.topic(),
            result.timestamp());

        sendHealthTreading();
        sendCriticalAlert();
    }

    private static void sendHealthTreading() {
        var props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("key.serializer", "com.kakfainaction.AlertKeySerde");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<Alert, String> producer = new KafkaProducer<Alert, String>(props);
        Alert alert = new Alert();
        alert.setSensorId(0L);
        alert.setStatus(alert_status.Critical);
        alert.setStageId("Stage_0");
        alert.setStageReason("Stage 0 stopped");
        ProducerRecord<Alert, String> producerRecord = new ProducerRecord<Alert, String>("healthtrend", alert,  alert.getStageReason().toString());
        producer.send(producerRecord);
    }

    private static void sendCriticalAlert() {
        var props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("key.serializer", "com.kakfainaction.AlertKeySerde");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put(PARTITIONER_CLASS_CONFIG, "com.kakfainaction.AlertLevelPartitioner");
        Producer<Alert, String> producer = new KafkaProducer<Alert, String>(props);
        Alert alert = new Alert();
        alert.setSensorId(1L);
        alert.setStatus(alert_status.Critical);
        alert.setStageId("Stage_1");
        alert.setStageReason("Stage 1 stopped");
        ProducerRecord<Alert, String> producerRecord = new ProducerRecord<Alert, String>("alert", alert, alert.getStageReason().toString());
        producer.send(producerRecord, new AlertCallback());
    }
}
