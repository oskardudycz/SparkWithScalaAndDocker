docker exec -t -i docker_spark-master_1 bash -c "spark-submit --class sparkWithScalaAndDocker.Startup /app/target/scala-2.11/sparkwithscalaanddocker_2.11-0.1.jar /app/build.sbt"