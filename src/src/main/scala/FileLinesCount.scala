package sparkWithScalaAndDocker

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataTypes._
import java.util.Properties
import java.sql.DriverManager
import java.util.Properties

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

    val connectionProperties = new Properties()
    connectionProperties.put("user", jdbcUsername)
    connectionProperties.put("password", jdbcPassword)
    connectionProperties.put("driver", "org.postgresql.Driver")

    val dataOpts = Map(
      "url" -> jdbcUrl,
      "dbtable" -> "data.UserLocation",
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
        monotonically_increasing_id().as("Id"),
        $"data.user.id".cast(LongType).as("UserId"),
        $"data.user.username".as("UserName"),
        from_unixtime($"data.created_time").as("Created"),
        $"data.location.latitude".cast("decimal(9,6)").as("Latitude"),
        $"data.location.longitude".cast("decimal(9,6)").as("Longitude")
      )
      
    jsonResult.show()

    jsonResult
      .write
      .mode(SaveMode.Overwrite) // <--- Append to the existing table
      .jdbc(jdbcUrl, "data.UserLocation", connectionProperties)

    val fileName = args(0)
    val lines = sc.textFile(fileName).cache

    val c = lines.count
    println(s"There are $c lines in $fileName")
  }
}