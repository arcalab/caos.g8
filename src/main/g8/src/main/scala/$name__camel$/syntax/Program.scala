package $name;format="camel"$.syntax

/**
 * Internal structure to represent terms in $name$.
 */

object Program:

  /** A term in $name$ */
  enum Term:
    case End
    case Proc(p:String)
    case Prefix(act:String,t:Term)
    case Choice(t1:Term, t2:Term)
    case Par(t1:Term, t2:Term)

  case class System(defs: Map[String,Term], main:Term, toCompare:Option[Term]):
    def apply(newMain:Term) = System(defs,newMain,toCompare)


  //////////////////////////////
  // Examples and experiments //
  //////////////////////////////

  object Examples:
    import Program.Term._


    val p1: Term =
      Prefix("x",End)

