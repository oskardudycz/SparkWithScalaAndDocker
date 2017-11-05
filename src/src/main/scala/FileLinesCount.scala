package sparkWithScalaAndDocker

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SparkSession
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
      "dbtable" -> "projects",
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
    
    val df = spark.
      read.
      format("jdbc").
      options(opts).
      load

    df.show(truncate = false)

    val fileName = args(0)
    val lines = sc.textFile(fileName).cache

    val c = lines.count
    println(s"There are $c lines in $fileName")
  }
}