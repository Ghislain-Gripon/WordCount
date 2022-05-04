import org.scalatest.wordspec.AnyWordSpec
import WordCountCucumberScala_GhislainGripon.Configuration
import WordCountCucumberScala_GhislainGripon.FieldEmptyError

class ConfigurationTest extends AnyWordSpec {
  val config = new Configuration()
  val configFileSelect = new Configuration("src/data/Config.yaml")

  "A Configuration class instance" should {
    "throw an error if there are no correct config file at specified location" in {
      assertThrows[Exception](new Configuration("wrong/path"))
    }

    "be able to open a correct config file at the specified path" in {
      assert(new Configuration("src/data/Config.yaml").isInstanceOf[Configuration])
    }

    "return a given field with the correct type" in {
      assert(config.max_thread_num.isInstanceOf[Int])
      assert(config.test_data.isInstanceOf[String])
      assert(config.main_data.isInstanceOf[String])
      assert(config.gherkin.isInstanceOf[String])
      assert(config.version.isInstanceOf[String])
      assert(config.step_definition.isInstanceOf[String])
      assert(config.thread_use_or_not.isInstanceOf[Boolean])
      assert(config.tests_to_run.isInstanceOf[List[String]])
    }

  }

  "FieldEmptyError" should {
    "send its error message" in {
      assertThrows(throw FieldEmptyError("FieldEmptyError throws this message."))
    }
  }
}
