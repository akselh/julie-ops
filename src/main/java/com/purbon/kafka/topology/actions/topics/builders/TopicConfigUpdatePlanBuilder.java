package com.purbon.kafka.topology.actions.topics.builders;

import com.purbon.kafka.topology.actions.topics.TopicConfigUpdatePlan;
import com.purbon.kafka.topology.api.adminclient.TopologyBuilderAdminClient;
import com.purbon.kafka.topology.model.Topic;
import java.io.IOException;
import java.util.Set;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;

public class TopicConfigUpdatePlanBuilder {

  private TopologyBuilderAdminClient adminClient;

  public TopicConfigUpdatePlanBuilder(TopologyBuilderAdminClient adminClient) {
    this.adminClient = adminClient;
  }

  public TopicConfigUpdatePlan createTopicConfigUpdatePlan(Topic topic, String fullTopicName) {

    Config currentConfigs = adminClient.getActualTopicConfig(fullTopicName);

    TopicConfigUpdatePlan topicConfigUpdatePlan = new TopicConfigUpdatePlan(topic);

    try {
      if (topic.partitionsCount() > adminClient.getPartitionCount(fullTopicName)) {
        topicConfigUpdatePlan.setUpdatePartitionCount(true);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to get partition count for topic " + fullTopicName, e);
    }

    topic
        .getRawConfig()
        .forEach(
            (configKey, configValue) -> {
              ConfigEntry currentConfigEntry = currentConfigs.get(configKey);
              if (!currentConfigEntry.value().equals(configValue)) {
                if (isDynamicTopicConfig(currentConfigEntry)) {
                  topicConfigUpdatePlan.addConfigToUpdate(configKey, configValue);
                } else {
                  topicConfigUpdatePlan.addNewConfig(configKey, configValue);
                }
              }
            });

    Set<String> configKeys = topic.getRawConfig().keySet();
    currentConfigs
        .entries()
        .forEach(
            entry -> {
              if (isDynamicTopicConfig(entry) && !configKeys.contains(entry.name())) {
                topicConfigUpdatePlan.addConfigToDelete(entry.name(), entry.value());
              }
            });
    return topicConfigUpdatePlan;
  }

  private boolean isDynamicTopicConfig(ConfigEntry currentConfigEntry) {
    return currentConfigEntry.source().equals(ConfigEntry.ConfigSource.DYNAMIC_TOPIC_CONFIG);
  }
}
