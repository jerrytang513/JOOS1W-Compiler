import java.io.{File, FilenameFilter}

import Scanner.tokenizeJoos1w
import Scanner.LexerException
import Parser.parseJoos1w
import Parser.ParserException
import Weeding._
import Asts.{CompilationUnit, treeToAst}
import Environments._
import Disambiguation.{DisambiguateException, TypeCheckingException}
import StaticAnalysis.{DefiniteAssignmentException, ReachabilityException}
import CodeGeneration.{CodeGenerationException, joos1wA5}
import org.scalatest.FunSuite
import Utils._

import sys.process._
import scala.reflect.ClassTag

class A5Tests extends FunSuite {

  case class NASMException(message: String = "") extends Throwable {
    override def getMessage: String = message
  }

  case class LinkerException(message: String = "") extends Throwable {
    override def getMessage: String = message
  }

  case class ExecutableException(message: String = "") extends Throwable {
    override def getMessage: String = message
  }

  def compile(f: File): CompilationUnit = {
    val source = io.Source.fromFile(f)
    val str = (for (line <- source.getLines()) yield line).mkString("\n")
    val tokens = tokenizeJoos1w(str)
    val root = parseJoos1w(tokens)
    weedJoos1w(root)
    treeToAst(root).head.asInstanceOf[CompilationUnit]
  }

  val stdlib: Seq[File] = allFilesInDir("resources/testcases/stdlib/5.0")

  val stdlibCompiled: Seq[CompilationUnit] = stdlib.map(compile)

  val asmDirectory = "output/"

  val runtime_s = "resources/testcases/stdlib/5.0/runtime.s"

  def getFilesWithExtension(dirName: String, extension: String): Seq[File] = {
    new File(dirName).listFiles(new FilenameFilter {
      override def accept(file: File, s: String): Boolean = {
        s.endsWith(extension)
      }
    })
  }

  // instanceof is not implemented yet, so skip those test cases for now
  val blacklist: Seq[String] = Seq("J1_1_Instanceof_InLazyExp.java", "J1_1_Instanceof_OfAdditiveExpression.java", "J1_1_Instanceof_OfCastExpression.java")
  def assertPass(testName: String, files: Seq[File]): Unit = {
    print("testing " + testName + ": ")
    if (blacklist.contains(testName)) {
      println("skipped")
      return
    }
    // remove the old `output` directory and create an empty `output`
    var retCode = 0
    if (new File("main").isFile) {
      // clear the executable from previous run
      s"rm main".!
    }
    if (new File(asmDirectory).isDirectory) {
      // clear the *.o and *.s from previous run
      s"rm -rf $asmDirectory".!
    }
    new File(asmDirectory).mkdirs()

    if (new File(asmDirectory).isDirectory) {
      // copy runtime.s into `output` directory
      s"cp $runtime_s $asmDirectory".!
    }

    val asts: Seq[CompilationUnit] = files.map(compile) ++ stdlibCompiled
    // single file reject can all be resolved when linkType is enabled. For now it is still incomplete linkType(asts)
    joos1wA5(asts)

    // assemble
    for (file <- new File(asmDirectory).listFiles) {
      val retCode = s"nasm -O1 -f elf -g -F dwarf $asmDirectory/${file.getName}".!
      if (retCode != 0) {
        throw NASMException("Error found in generated code")
      }
    }
    val objectFiles = getFilesWithExtension(asmDirectory, ".o")
    // link
    retCode = s"ld -melf_i386 -o main ${objectFiles.mkString(" ")}".!
    if (retCode != 0) {
      throw LinkerException("Error found in linker")
    }
    // run executable
    retCode = s"./main".!
    if (retCode != 123) {
      throw ExecutableException("Error found in Executable")
    }
    println("passed")
  }

  def assertRejectBecauseOf[T <: AnyRef](testName: String, files: Seq[File])(implicit classTag: ClassTag[T]): Unit = {
    val e = intercept[T] {
      assertPass(testName, files)
    }

    e match {
      case _: LexerException => print("rejected by scanner")
      case _: ParserException => print("rejected by parser")
      case _: WeederException => print("rejected by weeder")
      case _: EnvironmentBuildingException => print("rejected by environment builder")
      case _: TypeLinkingException => print("rejected by type linking")
      case _: HierarchyCheckingException => print("rejected by hierarchy check")
      case _: DisambiguateException => print("rejected by disambuation")
      case _: TypeCheckingException => print("rejected by type checking")
      case _: ReachabilityException => print("rejected by reachability analysis")
      case _: DefiniteAssignmentException => print("rejected by definite assignment analysis")
      case _: CodeGenerationException => print("rejected by code generation")
    }
    println(" " + e.toString)
  }

  test("stdlib test") {
    println(stdlib.map(_.getAbsolutePath))
  }

  val root = new File("resources/testcases/a5")

  test("single files pass") {
    val files = root.listFiles.filter(_.isFile).filter(_.getName.matches("J[0-9].*"))

    for ((file, i) <- files.zipWithIndex) {
      print((i + 1) + "/" + files.length + " ")
      assertPass(file.getName, Seq(file))
    }
  }

  test("single files reject") {
    val files = root.listFiles.filter(_.isFile).filter(_.getName.matches("Je.*"))

    for ((file, i) <- files.zipWithIndex) {
      print((i + 1) + "/" + files.length + " ")
      assertRejectBecauseOf[Throwable](file.getName, Seq(file))
    }
  }

  test("multiple files pass") {
    val directories = root.listFiles.filter(_.isDirectory).filter(_.getName.matches("J[0-9].*"))

    for ((directory, i) <- directories.zipWithIndex) {
      print((i + 1) + "/" + directories.length + " ")
      assertPass(directory.getName, allFilesInDir(directory.getAbsolutePath))
    }
  }

  test("multiple files reject") {
    val directories = root.listFiles.filter(_.isDirectory).filter(_.getName.matches("Je.*"))

    for ((directory, i) <- directories.zipWithIndex) {
      print((i + 1) + "/" + directories.length + " ")
      assertRejectBecauseOf[Throwable](directory.getName, allFilesInDir(directory.getAbsolutePath))
    }
  }

  test("single test") {
    val directory = new File(root.getAbsolutePath + "/J1_reachability_return")
    assertPass(directory.getName, allFilesInDir(directory.getAbsolutePath))
  }

  test("single file test") {
    val file = new File(root.getAbsolutePath + "/J1_A_ArrayStoreLoad.java")
    assertPass(file.getName, Seq(file))
  }

}
