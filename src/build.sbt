name := "SparkWithScalaAndDocker"

version := "0.1"
scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.2.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-sql_2.11" % "2.2.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.hadoop" % "hadoop-common" % "2.8.2" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
   "org.apache.spark" % "spark-hive_2.11" % "2.2.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-yarn_2.11" % "2.2.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.postgresql" % "postgresql" % "42.1.4"
)