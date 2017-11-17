package sparkWithScalaAndDocker

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import java.util.Properties
import java.sql.DriverManager

object FileLinesCount {
  def main(args: Array[String]) {
    DriverManager.registerDriver(new org.postgresql.Driver());
    var pgsql = Class.forName("org.postgresql.Driver");
    // Option 1: Build the parameters into a JDBC url to pass into the DataFrame APIs
    val jdbcUsername = "postgres"
    val jdbcPassword = ""
    val jdbcHostname = "postgres"
    val jdbcPort = 5432
    val jdbcDatabase ="postgres"
    val jdbcUrl = s"jdbc:postgresql://${jdbcHostname}:${jdbcPort}/${jdbcDatabase}?user=${jdbcUsername}&password=${jdbcPassword}"

    val opts = Map(
      "url" -> jdbcUrl,
      "dbtable" -> "import.instagram_media_recent",
      "driver" -> "org.postgresql.Driver"
    )

    val conf = new SparkConf().setAppName("SparkWithScalaAndDocker Application")
    val sc = new SparkContext(conf)

    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.driver.extraClassPath", "/app/postgresql-42.1.4.jar")
      .config("spark.executor.extraClassPath", "/app/postgresql-42.1.4.jar")
      .getOrCreate()

    import spark.implicits._
    
    val df = spark.
      read.
      format("jdbc").
      options(opts).
      load

    val jsons = df
      .map(t => t.getString(2))

    // load the jsons, infer the schema
    val jsonResult = spark.read.json(jsons)
      .select(explode($"data").alias("data"))
      .select(
        $"data.user.id".as("UserId"),
        $"data.user.username".as("UserName"),
        $"data.created_time".as("Created"),
        $"data.location.latitude".as("Latitude"),
        $"data.location.longitude".as("Longitude")
      )
      
    jsonResult.show()

    val fileName = args(0)
    val lines = sc.textFile(fileName).cache

    val c = lines.count
    println(s"There are $c lines in $fileName")
  }
}