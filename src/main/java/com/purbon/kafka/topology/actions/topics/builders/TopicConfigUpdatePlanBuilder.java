package com.purbon.kafka.topology.actions.topics.builders;

import com.purbon.kafka.topology.actions.topics.TopicConfigUpdatePlan;
import com.purbon.kafka.topology.api.adminclient.TopologyBuilderAdminClient;
import com.purbon.kafka.topology.model.Topic;
import java.io.IOException;
import java.util.Set;
import org.apache.kafka.clients.admin.Config;

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
              if (currentConfigs.get(configKey) == null) {
                topicConfigUpdatePlan.addNewConfig(configKey, configValue);
              } else {
                topicConfigUpdatePlan.addConfigToUpdate(configKey, configValue);
              }

              Set<String> configKeys = topic.getRawConfig().keySet();

              currentConfigs
                  .entries()
                  .forEach(
                      entry -> {
                        if (!entry.isDefault() && !configKeys.contains(entry.name())) {
                          topicConfigUpdatePlan.addConfigToDelete(entry.name(), entry.value());
                        }
                      });
            });

    return topicConfigUpdatePlan;
  }
}
