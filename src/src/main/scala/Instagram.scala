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

object Instagram {
  def processLocations(spark: SparkSession) {  
    val df = Postgres.read(spark, "import.instagram_media_recent")

    val locations = transformLocations(spark, df);

    Postgres.write(locations, "data.UserLocation")
  }

  def transformLocations(spark: SparkSession, df: DataFrame): DataFrame = {  
    import spark.implicits._

    val jsons = df
      .map(t => t.getString(2))

    val locations = spark.read.json(jsons)
      .select(explode($"data").alias("data"))
      .select(
        monotonically_increasing_id().as("Id"),
        $"data.user.id".cast("long").as("UserId"),
        $"data.user.username".as("UserName"),
        from_unixtime($"data.created_time").cast("timestamp").as("Created"),
        $"data.location.latitude".cast("decimal(9,6)").as("Latitude"),
        $"data.location.longitude".cast("decimal(9,6)").as("Longitude")
      )
      
    locations.show()

    return locations
  }
}