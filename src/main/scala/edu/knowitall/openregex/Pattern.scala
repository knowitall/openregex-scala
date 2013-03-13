package edu.knowitall.openregex

import scala.collection.JavaConverters._
import com.google.common.base.{ Function => GuavaFunction }
import edu.knowitall.collection.immutable.Interval
import edu.washington.cs.knowitall.regex.Expression.BaseExpression
import edu.washington.cs.knowitall.regex.RegularExpression
import edu.washington.cs.knowitall.regex.{ Match => JavaMatch }
import edu.washington.cs.knowitall.regex.Expression

class Pattern[E](val regex: RegularExpression[E]) {
  def apply(tokens: Seq[E]): Boolean = regex(tokens.asJava)

  def matches(tokens: Seq[E]): Boolean = regex.matches(tokens.asJava)

  def find(tokens: Seq[E], start: Int = 0): Pattern.Match[E] = {
    Pattern.Match.fromJava(regex.find(tokens.asJava, start))
  }

  def findAll(tokens: Seq[E]): Seq[Pattern.Match[E]] = {
    regex.findAll(tokens.asJava).asScala map Pattern.Match.fromJava
  }

  def lookingAt(tokens: Seq[E], start: Int = 0): Pattern.Match[E] = {
    Pattern.Match.fromJava(regex.lookingAt(tokens.asJava, start))
  }
}

object Pattern {
  def compile[E](string: String, factory: String=>BaseExpression[E]): Pattern[E] = {
    val regex = RegularExpression.compile[E](string, new GuavaFunction[String, BaseExpression[E]]() {
      override def apply(string: String): BaseExpression[E] = factory(string)
    })

    new Pattern(regex)
  }

  case class Match[E](tokens: Seq[E], pairs: Seq[Group[E]], interval: Interval) {
    def text = tokens.mkString(" ")

    def groups = pairs.filter(p =>
      p.expr.isInstanceOf[Expression.MatchingGroup[_]] &&
      !(p.expr.isInstanceOf[Expression.NonMatchingGroup[_]]))

    def group(name: String) = this.groups.find {
      _.expr match {
        case group: Expression.NamedGroup[_] => group.name == name
        case _ => false
      }
    }
  }
  object Match {
    def fromJava[E](m: JavaMatch[E]) = {
      Match(m.tokens.asScala, m.pairs.asScala.map(Group.fromJava(_)), Interval.open(m.startIndex, m.endIndex + 1))
    }
  }

  case class Group[E](expr: Expression[E], tokens: Seq[E], interval: Interval) {
    def text = tokens.mkString(" ")
  }
  object Group {
    def fromJava[E](group: JavaMatch.Group[E]) = {
      Group(group.expr, group.tokens.asScala, Interval.open(group.startIndex, group.endIndex + 1))
    }
  }
}
