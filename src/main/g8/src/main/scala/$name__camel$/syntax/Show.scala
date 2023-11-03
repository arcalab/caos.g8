package $name;format="camel"$.syntax

import $name;format="camel"$.syntax.Program.*
import $name;format="camel"$.syntax.Program.Term.*

/**
 * List of functions to produce textual representations of commands
 */
object Show:

  def justTerm(s: System): String = apply(s.main)

  def apply(s: System): String =
    apply(s.defs)+
    apply(s.main)+(s.toCompare match
      case None => ""
      case Some(t) => " ~~ "+apply(t)
    )
  def apply(defs: Map[String,Term]): String =
    if defs.isEmpty then ""
    else "let\n"+
      (for (p,t)<-defs yield s"  \$p = \${apply(t)}").mkString("\n") +
      "\nin\n  "

  /** Pretty expression */
  def apply(e: Term): String = e match
    case Term.End => "0"
    case Term.Proc(p) => p
    case Term.Prefix(act, Term.End) => act
    case Term.Prefix(act, t) => s"\$act.\${applyP(t)}"
    case Term.Choice(t1, t2) => s"\${applyP(t1)}+\${applyP(t2)}"
    case Term.Par(t1, t2) => s"\${applyP(t1)} | \${applyP(t2)}"

  private def applyP(e:Term): String = e match
    case _:(End.type|Proc|Prefix) => apply(e)
    case _ => s"(\${apply(e)})"


  /** Converts the main term into a mermaid diagram reflecting its structure. */
  def mermaid(s:System): String = "graph TD\n" +
  s"  style \${s.main.hashCode()} fill:#ffe177,stroke:#6a6722,stroke-width:4px;\n" +
    (term2merm(s.defs) ++
     term2merm(s.main)).mkString("\n")

      /** Builds nodes and arcs, using a set structure to avoid repetition. */
  private def term2merm(e: Term): Set[String] = e match
    case Term.End => Set(s"  \${e.hashCode()}([\"0\"])")
    case Term.Proc(p) => Set(s"  \${e.hashCode()}([\"\$p\"])")
    case Term.Prefix(act, t) => term2merm(t) ++
      Set(s"  \${e.hashCode()} -->|action| \${act.hashCode()}",
          s"  \${e.hashCode()} -->|rest| \${t.hashCode()}",
          s"  \${e.hashCode()}([\"\${apply(e)}\"])",
          s"  \${act.hashCode()}([\"\$act\"])"
        )
    case Term.Choice(t1, t2) =>
      term2merm(t1) ++ term2merm(t2) ++
      Set(s"  \${e.hashCode()} -->|option 1| \${t1.hashCode()}",
          s"  \${e.hashCode()} -->|option 2| \${t2.hashCode()}",
          s"  \${e.hashCode()}([\"\${apply(e)}\"])")
    case Term.Par(t1, t2) =>
      term2merm(t1) ++ term2merm(t2) ++
      Set(s"  \${e.hashCode()} -->|par 1| \${t1.hashCode()}",
          s"  \${e.hashCode()} -->|par 2| \${t2.hashCode()}",
          s"  \${e.hashCode()}([\"\${apply(e)}\"])")

  /** Builds the nodes and arcs of the diagram of the definitions */
  private def term2merm(defs: Map[String,Term]): Set[String] = 
    defs.flatMap( (p,t) =>
      term2merm(t) +
      s"  \${Proc(p).hashCode()}([\"\$p\"])" +
      s"  \${Proc(p).hashCode()} -->|definition| \${t.hashCode}"
    ).toSet
