# Spark With Scala And Docker

Project shows how to create basic configuration of Spark workstation with Docker. It's not needed to setup on local environment Java, Scala, Spark, Hadoop on any other tool than Docker. As an example is show how to do simplest lines count in Spark.

Code is placed in [/src/main/scala/](https://github.com/oskardudycz/SparkWithScalaAndDocker/blob/master/src/src/main/scala/FileLinesCount.scala)

```scala
package sparkWithScalaAndDocker

import org.apache.spark.{SparkContext, SparkConf}

object FileLinesCount {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SparkWithScalaAndDocker Application")
    val sc = new SparkContext(conf)

    val fileName = args(0)
    val lines = sc.textFile(fileName).cache

    val c = lines.count
    println(s"There are $c lines in $fileName")
  }
}
```

Open cmd/shell:
1. Run `init` to start docker (see details in [init.bat](https://github.com/oskardudycz/SparkWithScalaAndDocker/blob/master/init.bat)).
2. Run `build` to build project (see details in [build.bat](https://github.com/oskardudycz/SparkWithScalaAndDocker/blob/master/build.bat)).
3. Run `run` to run project in Spark (see details in [run.bat](https://github.com/oskardudycz/SparkWithScalaAndDocker/blob/master/run.bat))