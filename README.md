#Spark With Scala And Docker

Project shows how to do simplest lines count in Spark

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
1. Run `init` to start docker.
2. Run `build` to build project.
3. Run `run` to run project in Spark.