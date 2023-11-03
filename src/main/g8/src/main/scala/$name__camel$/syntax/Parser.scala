package $name;format="camel"$.syntax

import cats.parse.Parser.*
import cats.parse.{LocationMap, Parser as P, Parser0 as P0}
import $name;format="camel"$.syntax.Program.*
import $name;format="camel"$.syntax.Program.Term.*

import scala.sys.error

object Parser :

  /** Parse a command  */
  def parseProgram(str:String):System =
    pp(program,str) match {
      case Left(e) => error(e)
      case Right(c) => c
    }

  /** Applies a parser to a string, and prettifies the error message */
  private def pp[A](parser:P[A], str:String): Either[String,A] =
    parser.parseAll(str) match
      case Left(e) => Left(prettyError(str,e))
      case Right(x) => Right(x)

  /** Prettifies an error message */
  private def prettyError(str:String, err:Error): String =
    val loc = LocationMap(str)
    val pos = loc.toLineCol(err.failedAtOffset) match
      case Some((x,y)) =>
        s"""at (\$x,\$y):
           |"\${loc.getLine(x).getOrElse("-")}"
           |\${("-" * (y+1))+"^\n"}""".stripMargin
      case _ => ""
    s"\${pos}expected: \${err.expected.toList.mkString(", ")}\noffsets: \${
      err.failedAtOffset};\${err.offsets.toList.mkString(",")}"

  // Simple parsers for spaces and comments
  /** Parser for a sequence of spaces or comments */
  private val whitespace: P[Unit] = P.charIn(" \t\r\n").void
  private val comment: P[Unit] = string("//") *> P.charWhere(_!='\n').rep0.void
  private val sps: P0[Unit] = (whitespace | comment).rep0.void

  // Parsing smaller tokens
  private def alphaDigit: P[Char] =
    P.charIn('A' to 'Z') | P.charIn('a' to 'z') | P.charIn('0' to '9') | P.charIn('_')
  private def varName: P[String] =
    (charIn('a' to 'z') ~ alphaDigit.rep0).string
  private def procName: P[String] =
    (charIn('A' to 'Z') ~ alphaDigit.rep0).string
  private def symbols: P[String] =
    // symbols starting with "--" are meant for syntactic sugar of arrows, and ignored as symbols of terms
    P.not(string("--")).with1 *>
    oneOf("+-><!%/*=|&".toList.map(char)).rep.string

  import scala.language.postfixOps

  /** A program is a command with possible spaces or comments around. */
  private def program: P[System] =
    ((oneProgram<*sps)~((char('~')*>sps*>(term<*sps))?))
      .map((x,y) => System(x.defs,x.main,y))
  private def oneProgram: P[System] =
    system|term.map(x=>System(Map(),x,None))
  private def system: P[System] =
    string("let") *> sps *>
    ((defn.repSep0(sps)<*sps<*string("in")<*sps).with1 ~ term)
      .map((x,y)=>System(x.toMap,y,None))
  private def defn:P[(String,Term)] =
    (procName <* char('=').surroundedBy(sps)) ~
      (term <* sps <* char(';'))

  private def term: P[Term] = P.recursive(more =>
    termSum(more).repSep(sps *> char('|') <* sps)
      .map(l => l.toList.tail.foldLeft(l.head)((t1, t2) => Par(t1, t2)))
  )
  private def termSum(more:P[Term]): P[Term] =
    (termSeq(more)<*sps).repSep(char('+') <* sps)
      .map(l=>l.toList.tail.foldLeft(l.head)((t1,t2)=>Choice(t1,t2)))

  private def termSeq(more:P[Term]): P[Term] = P.recursive(t2 =>
    end | proc | pref(t2) | char('(')*>more.surroundedBy(sps)<*char(')')
  )

  private def end: P[Term] =
    char('0').as(End)

  private def proc: P[Term] =
    procName.map(Proc.apply)

  private def pref(t2:P[Term]): P[Term] =
    ((varName <* sps) ~ ((char('.') *> t2)?))
      .map(x => Prefix(x._1,x._2.getOrElse(End)))


  //////////////////////////////
  // Examples and experiments //
  //////////////////////////////
  object Examples:
    val ex1 =
      """x:=28; while(x>1) do x:=x-1"""
