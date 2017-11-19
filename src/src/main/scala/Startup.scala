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

object Startup {
  def main(args: Array[String]) {
    val spark = Postgres.getOrCreateSpark("Instagram processing")
    
    Instagram.processLocations(spark);
  }
}