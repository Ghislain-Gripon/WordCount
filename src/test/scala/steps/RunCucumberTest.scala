package steps

import io.cucumber.junit.{Cucumber, CucumberOptions}
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(tags = "@mapreduce",
  features = Array("classpath:features"),
  glue = Array("classpath:steps"),
  plugin = Array("pretty",
    "html:target/cucumber/test-report.html",
    "json:target/cucumber/test-report.json",
    "junit:target/cucumber/test-report.xml"))
class RunCucumberTest {

}
