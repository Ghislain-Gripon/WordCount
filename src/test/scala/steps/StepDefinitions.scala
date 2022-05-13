package steps

import io.cucumber.scala.{EN, ScalaDsl}
import WordCountCucumberScala_GhislainGripon._
import scala.reflect.io.File


class StepDefinitions extends ScalaDsl with EN {
  private var mapReducer: Option[MapReduceTask] = None
  private var textData: String = ""
  private var results: Option[List[(String, Int)]] = None
  private var config: Option[Configuration] = None
  private var configPath: String = ""

  Given("""there is a configuration file at {string}""") { (path: String) =>
    if(path.isEmpty) {
      configPath = "src/data/Config.yaml"
    }
    else {
      configPath = path
    }
    assert(File(configPath).exists, "Configuration file not found.")
  }

  And("""the configuration file is read""") { () =>
    config = Some(new Configuration(configPath))
  }

  And("""there is text""") {() =>
    assert(File("src/data/TestData.txt").exists)
  }

  When("""text is read""") { () => {
    val config = new Configuration()
    textData = FileHandler.read("src/data/TestData.txt")


  }
  }

  Then("""text is given to the program""") {() =>
    mapReducer = Some(new MapReduceTask(textData))
  }

  Then("""the program returns a list of words and their occurrence figure""") {() =>
    results = Some(mapReducer.get.execute())
    assert(results.get.nonEmpty, "No words were counted")
  }
}
