package WordCountCucumberScala_GhislainGripon

import java.util.HashMap

import org.apache.spark.SparkConf
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe


class KafkaSparkWordCount(config: Configuration) extends Task with Serializable {
  override def execute(): Any = ???
}
