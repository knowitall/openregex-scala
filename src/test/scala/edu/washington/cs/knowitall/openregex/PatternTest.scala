package edu.washington.cs.knowitall.openregex

import org.junit.runner.RunWith
import edu.washington.cs.knowitall.regex.Expression.BaseExpression

import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import edu.washington.cs.knowitall.logic.Expression.Arg

@RunWith(classOf[JUnitRunner]) /** Real functionality tests are in openregex itself.
  *
  * @author schmmd
  */
class PatternTest extends Specification {
  def deserialize(matcher: String) =
    new BaseExpression[String](matcher) {
      override def apply(string: String) = string == matcher
    }
  "Basic regular expression tests" should {
    val regex = Pattern.compile[String]("<hello>? <goodbye>* <.>", deserialize)

    regex.matches(List("hello", "goodbye", ".")) must beTrue
    regex.matches(List("hello", "goodbye", "goodbye", ".")) must beTrue
    regex.matches(List("hello", "goodbye", "goodbye")) must beFalse
  }

  "Matching group regular expression tests" should {
    val regex = Pattern.compile[String]("(<hello>?) (<foo>:<goodbye>*) <.>", deserialize)

    val m = regex.find(List("hello", "goodbye", "goodbye", "."))
    m.groups(0).text == "hello"
    m.groups(1).text == "goodbye goodbye"
    m.group("foo").get.text == "goodbye goodbye"
  }
}