package WordCountCucumberScala_GhislainGripon

import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import com.mongodb.spark._

class SparkMapReduceTask(config: Configuration) extends Task with Serializable{
  override def execute(): Unit = {
    @transient val connectionString = s"mongodb://${config.getUsername}:${config.getPassword}@${config.host}:${config.port}/${config.database}."
    val sparkConf = new SparkConf()
      .set("spark.mongodb.input.uri", connectionString+config.text_table)
      .set("spark.mongodb.output.uri", connectionString+config.result_table)
      .set("spark.jars.packages", "org.mongodb.spark:mongo-spark-connector_2.12:3.0.2")
      .setMaster(s"local[${config.spark_threads}]")
      .setAppName("MapReduce")
    val spark = SparkSession.builder()
      .config(sparkConf)
      .getOrCreate()
    import spark.implicits._
    spark.sparkContext.setLogLevel("WARN")

    val testRDD = spark.read.format(config.engine_spark_data_source).option("database", config.database).option("collection", config.text_table).load()

    val mapReduce = testRDD.select($"rawText").where($"_id" === config.main_data).flatMap(line => "(((?U)\\w)+)".r.findAllIn(line.getAs(config.text_field)).toList)
    .groupBy($"value").count().orderBy($"count".desc).repartition(1)//.write.mode(config.spark_write_mode).csv(config.data_dir + "/MapReduceResults")

    val resultList = mapReduce
      .collect

    val resultSchema = new StructType().add("_id", StringType).add(config.text_field, ArrayType(new StructType().add(StructField("value", StringType)).add(StructField("count", LongType))))

    val resultDF = spark.createDataFrame(
      spark.sparkContext.parallelize(Seq(Row(config.main_data , resultList))),
      resultSchema
      )

    MongoSpark.save(resultDF)
  }

}
