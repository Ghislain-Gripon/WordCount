package steps

import io.cucumber.scala.{EN, ScalaDsl}
import WordCountCucumberScala_GhislainGripon._

class StepDefinitions extends ScalaDsl with EN {
  val mapReducer: Option[MapReduceTask] = None
  val results: Option[List[(String, Int)]] = None

  And("""there is a mapreducer""") { () =>

  }
}
