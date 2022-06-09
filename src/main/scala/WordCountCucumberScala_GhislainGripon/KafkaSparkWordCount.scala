package WordCountCucumberScala_GhislainGripon

import java.util.HashMap
import org.apache.spark.SparkConf
import org.apache.kafka.clients.consumer._
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.avro._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._



class KafkaSparkWordCount(config: Configuration) extends Task with Serializable {
  val kafkaParams: Map[String, Object] = Map[String, Object](
    "application.id" -> "MongoCount",
    "zookeeper.connect" -> "localhost:2181",
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "WordMongoCount-Consumer",
    "auto.offset.reset" -> "earliest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  val topics: Array[String] = Array("test")

  @transient val connectionString = s"mongodb://${config.getUsername}:${config.getPassword}@${config.host}:${config.port}/${config.database}."
  val sparkConf: SparkConf = new SparkConf()
    .set("spark.mongodb.input.uri", connectionString + config.text_table)
    .set("spark.mongodb.output.uri", connectionString + config.result_table)
    .set("spark.jars.packages",
      "org.mongodb.spark:mongo-spark-connector_2.12:3.0.2,org.apache.spark:spark-streaming-kafka-0-10_2.12:3.0.3" +
    ",org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.3")
    .setMaster(s"local[${config.spark_threads}]")
    .setAppName("MapReduce")

  val scc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))

  override def execute(): Any = {
    val stream = KafkaUtils.createDirectStream[String, String](
      scc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    val spark = SparkSession.builder()
      .config(sparkConf)
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")

    val df = spark.readStream.format("kafka").option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "test").option("startingOffsets", "earliest")
      .load()
    import spark.implicits._

    val map = df.select(col("value").cast("string"))
      .flatMap(line => "(((?U)\\w)+)".r.findAllIn(line.getAs("value")).toList)
      .groupBy(col("value")).count().orderBy(col("count").desc)

    map.writeStream.outputMode("complete").format("console").start().awaitTermination()
    map.show()
    val test = stream.map(record => record.value)

    test.foreachRDD(rdd =>
      println(rdd.flatMap(line => "(((?U)\\w)+)".r.findAllIn(line)).map(w => (w, 1)).reduceByKey(_ + _).collect().mkString)
    )

    scc.start()
    scc.awaitTermination()
  }
}
