package edu.washington.cs.knowitall.openregex

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import edu.washington.cs.knowitall.logic.Expression.Arg

@RunWith(classOf[JUnitRunner])
/** Real functionality tests are in openregex itself.
  *
  * @author schmmd
  */
class LogicTest extends Specification {
  "Some logic expression" should {
    val logic = Logic.compile[Unit]("true & (true | !false)", s => s.toLowerCase match {
      case "true" => new Arg.Value(true)
      case "false" => new Arg.Value(false)
    })

    "be true" in {
      logic.apply() must beTrue
    }
  }
}