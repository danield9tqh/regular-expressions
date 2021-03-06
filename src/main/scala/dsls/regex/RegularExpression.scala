package dsls.regex

/**
 * Modify this file to implement an internal DSL for regular expressions. 
 * 
 * You're allowed to add anything you want to this file, but you're not allowed
 * to *remove* anything that currently appears in the file.
 */

/** The top of a class hierarchy that encodes regular expressions. */
abstract class RegularExpression {
  /** returns true if the given string matches this regular expression */
  def matches(string: String) = RegexMatcher.matches(string, this)
  def ||(other: RegularExpression) = new Union(this, other)
  def ~(other: RegularExpression) = new Concat(this, other)
  def <*> = new Star(this)
  def <+> = new Concat(this, this <*>)
  def apply(n: Int): RegularExpression = {
    require(n >= 0)
    if (n == 0)
        EPSILON
    else
        this ~ this{n - 1}
  }
  
}

object RegularExpression {
    implicit def apply(literal: Char) = new Literal(literal)
    implicit def apply(literal: String) = {
        val literals: List[RegularExpression] = literal.toList.map(Literal)
        val first: RegularExpression = EPSILON
        literals.foldLeft(first)(Concat)
    }
}

/** a regular expression that matches nothing */
object EMPTY extends RegularExpression

/** a regular expression that matches the empty string */
object EPSILON extends RegularExpression

/** a regular expression that matches a literal character */
case class Literal(val literal: Char) extends RegularExpression

/** a regular expression that matches either one expression or another */
case class Union(val left: RegularExpression, val right: RegularExpression) 
  extends RegularExpression

/** a regular expression that matches one expression followed by another */
case class Concat(val left: RegularExpression, val right: RegularExpression) 
  extends RegularExpression
  
/** a regular expression that matches zero or more repetitions of another 
 *  expression
 */
case class Star(val expression: RegularExpression) extends RegularExpression
