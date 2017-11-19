package sparkWithScalaAndDocker

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataTypes._
import java.util.Properties
import java.sql.DriverManager
import java.util.Properties

object Postgres {
  val jdbcUsername = "postgres"
  val jdbcPassword = ""
  val jdbcHostname = "postgres"
  val jdbcPort = 5432
  val jdbcDatabase ="postgres"
  val jdbcUrl = s"jdbc:postgresql://${jdbcHostname}:${jdbcPort}/${jdbcDatabase}?user=${jdbcUsername}&password=${jdbcPassword}"
  
  val connectionProperties = new Properties()
  connectionProperties.put("user", jdbcUsername)
  connectionProperties.put("password", jdbcPassword)
  connectionProperties.put("driver", "org.postgresql.Driver")

  def getOrCreateSpark(appName: String): SparkSession = {
    DriverManager.registerDriver(new org.postgresql.Driver());
    val pgsql = Class.forName("org.postgresql.Driver");

    val spark = SparkSession
        .builder()
        .appName(appName)
        .config("spark.driver.extraClassPath", "/app/postgresql-42.1.4.jar")
        .config("spark.executor.extraClassPath", "/app/postgresql-42.1.4.jar")
        .getOrCreate()

    return spark;
  }

  def read(spark: SparkSession, tableName: String): DataFrame ={  
    val opts = Map(
      "url" -> jdbcUrl,
      "dbtable" -> tableName,
      "driver" -> "org.postgresql.Driver"
    )
  
    return spark.
        read.
        format("jdbc").
        options(opts).
        load
  }

  def write(df: DataFrame, tableName: String) {
    df
      .write
      .mode(SaveMode.Overwrite) // <--- Append to the existing table
      .jdbc(jdbcUrl, tableName, connectionProperties)
  }
}