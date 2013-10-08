package edu.knowitall.openregex

import scala.collection.JavaConverters._
import com.google.common.base.{ Function => GuavaFunction }
import edu.knowitall.collection.immutable.Interval
import edu.washington.cs.knowitall.regex.Expression.BaseExpression
import edu.washington.cs.knowitall.regex.RegularExpression
import edu.washington.cs.knowitall.regex.RegularExpressionParser
import edu.washington.cs.knowitall.regex.{ Match => JavaMatch }
import edu.washington.cs.knowitall.regex.Expression

case class Pattern[E](val regex: RegularExpression[E]) {
  def apply(tokens: Seq[E]): Boolean = regex(tokens.asJava)

  def matches(tokens: Seq[E]): Boolean = regex.matches(tokens.asJava)

  def find(tokens: Seq[E], start: Int = 0): Option[Pattern.Match[E]] = {
    Option(regex.find(tokens.asJava, start)) map Pattern.Match.fromJava
  }

  def findAll(tokens: Seq[E]): Seq[Pattern.Match[E]] = {
    regex.findAll(tokens.asJava).asScala map Pattern.Match.fromJava
  }

  def lookingAt(tokens: Seq[E], start: Int = 0): Pattern.Match[E] = {
    Pattern.Match.fromJava(regex.lookingAt(tokens.asJava, start))
  }
}

case class PatternParser[E](val parser: RegularExpressionParser[E]) {
  def apply(string: String) = Pattern(parser(string))
}

object Pattern {
  def compile[E](string: String, factoryF: String=>BaseExpression[E]): Pattern[E] = {
    parser(factoryF).apply(string)
  }

  def parser[E](factoryF: String=>BaseExpression[E]): PatternParser[E] = {
    val parser = new RegularExpressionParser[E]() {
      override def factory(string: String): BaseExpression[E] = factoryF(string)
    }
    new PatternParser(parser)
  }

  def parser[E](factoryF: String=>BaseExpression[E], readToken: String=>String): PatternParser[E] = {
    val parser = new RegularExpressionParser[E]() {
      override def factory(string: String): BaseExpression[E] = factoryF(string)
      override def readToken(string: String): String = readToken(string)
    }
    new PatternParser(parser)
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
