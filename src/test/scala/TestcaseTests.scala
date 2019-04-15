import scala.reflect.ClassTag
import Scanner.tokenizeJoos1w
import Scanner.LexerException
import Parser.parseJoos1w
import Parser.ParserException
import Weeding._
import org.scalatest.FunSuite

class TestcaseTests extends FunSuite {

  def assertRejectBecauseOf[T <: AnyRef](testName: String)(implicit classTag: ClassTag[T]): Unit = {
    val e = intercept[T] {
      assertPass(testName)
    }

    e match {
      case _: LexerException => println("rejected by scanner")
      case _: ParserException => println("rejected by parser")
      case _: WeederException => println("rejected by weeder")
    }

  }

  def assertPass(testName: String): Unit = {
    print("testing " + testName + ": ")
    val filename = "resources/testcases/" + testName + ".java"
    val source = io.Source.fromFile(filename)
    val str = (for (line <- source.getLines()) yield line).mkString("\n")
    val tokens = tokenizeJoos1w(str)
    val root = parseJoos1w(tokens)
    weedJoos1w(root)
    println("passed")
  }

  test("a1") {
    val passTests = List(
      "J1_01",
      "J1_1_AmbiguousName_AccessResultFromMethod",
      "J1_1_Cast_Complement",
      "J1_1_Cast_MultipleCastOfSameValue_1",
      "J1_1_Cast_MultipleCastOfSameValue_2",
      "J1_1_Cast_MultipleCastOfSameValue_3",
      "J1_1_Cast_MultipleReferenceArray",
      "J1_1_Escapes_3DigitOctalAndDigit",
      "J1_1_Instanceof_InLazyExp",
      "J1_1_Instanceof_OfAdditiveExpression",
      "J1_1_Instanceof_OfCastExpression",
      "J1_1_IntRange_NegativeInt",
      "J1_abstractclass",
      "J1_abstractmethodwithoutbody",
      "J1_arbitrarylocaldeclaration",
      "J1_arithmeticoperations",
      "J1_ArrayCreateAndIndex",
      "J1_assignmentExp",
      "J1_assignment",
      "J1_barminusfoo",
      "J1_BigInt",
      "J1_charadd",
      "J1_CharCast",
      "J1_CharCharInit1",
      "J1_CharCharInit2",
      "J1_char_escape2",
      "J1_char_escape3",
      "J1_char_escape",
      "J1_char",
      "J1_charliterals",
      "J1_classinstance",
      "J1_commentsInExp1",
      "J1_commentsInExp2",
      "J1_commentsInExp3",
      "J1_commentsInExp4",
      "J1_commentsInExp5",
      "J1_commentsInExp6",
      "J1_commentsInExp7",
      "J1_commentsInExp8",
      "J1_commentsInExp9",
      "J1_comparisonoperations",
      "J1_concat_in_binop",
      "J1_constructorbodycast",
      "J1_constructorparameter",
      "J1_constructorWithSameNameAsMethod",
      "J1_eagerbooleanoperations",
      "J1_EscapeEscape",
      "J1_evalMethodInvocationFromParExp",
      "J1_exp",
      "J1_extends",
      "J1_externalcall",
      "J1_finalclass2",
      "J1_finalclass",
      "J1_forAllwaysInit",
      "J1_forAlwaysInitAsWhile",
      "J1_forbodycast",
      "J1_forifstatements1",
      "J1_forifstatements2",
      "J1_forifstatements3",
      "J1_forinfor",
      "J1_forinitcast",
      "J1_forMethodInit",
      "J1_forMethodUpdate2",
      "J1_forMethodUpdate",
      "J1_for_no_short_if",
      "J1_forupdatecast",
      "J1_ForUpdate_ClassCreation",
      "J1_forWithoutExp",
      "J1_forWithoutInit",
      "J1_forWithoutUpdate",
      "J1_hello_comment",
      "J1_if",
      "J1_ifThenElse",
      "J1_if_then_for",
      "J1_if_then",
      "J1_implements",
      "J1_IntArrayDecl1",
      "J1_IntArrayDecl2",
      "J1_IntCast",
      "J1_IntCharInit",
      "J1_integerFun1",
      "J1_integerFun3",
      "J1_integerFun",
      "J1_IntInit",
      "J1_intliterals",
      "J1_intminusfoo",
      "J1_IntRange_MinNegativeInt",
      "J1_IsThisACast",
      "J1_lazybooleanoperations",
      "J1_maxint_comment",
      "J1_minuschar",
      "J1_minusminusminus",
      "J1_NamedTypeArray",
      "J1_NegativeByteCast",
      "J1_NegativeCharCast",
      "J1_NegativeIntCast1",
      "J1_NegativeIntCast2",
      "J1_negativeintcast3",
      "J1_NegativeOneByteByteCast",
      "J1_NegativeOneByteCharCast",
      "J1_NegativeOneByteIntCast",
      "J1_NegativeOneByteShortCast",
      "J1_NegativeShortCast",
      "J1_newobject",
      "J1_nonemptyconstructor",
      "J1_nullinstanceof1",
      "J1_nullliteral",
      "J1_octal_escape2",
      "J1_octal_escape3",
      "J1_octal_escape4",
      "J1_octal_escape5",
      "J1_octal_escape",
      "J1_primitivecasts",
      "J1_protectedfields",
      "J1_protected",
      "J1_publicclasses",
      "J1_publicconstructors",
      "J1_publicfields",
      "J1_publicmethods",
      "J1_SimpleTypeArray",
      "J1_SmallInt",
      "J1_staticmethoddeclaration",
      "J1_stringliteralinvoke",
      "J1_stringliterals",
      "J1_weird_chars",
      "J1w_Interface",
      "J1w_RestrictedNative",
      "J1w_StaticField",
    )

    val rejectTests = List(
      "Je_16_Circularity_1",
      "Je_16_Circularity_2",
      "Je_16_Circularity_3",
      "Je_16_Circularity_4_Rhoshaped",
      "Je_16_ClosestMatch_Array",
      "Je_16_ClosestMatch_Constructor_NoClosestMatch_This",
      "Je_16_IncDec_Final_ArrayLengthDec",
      "Je_16_IncDec_Final_ArrayLengthInc",
      "Je_16_IncDec_Final_PostDec",
      "Je_16_IncDec_Final_PostInc",
      "Je_16_IncDec_Final_PreDec",
      "Je_16_IncDec_Final_PreInc",
      "Je_16_IncDec_StringPostDec",
      "Je_16_IncDec_StringPostInc",
      "Je_16_IncDec_StringPreDec",
      "Je_16_IncDec_StringPreInc",
      "Je_16_MultiArrayCreation_Assign_1",
      "Je_16_MultiArrayCreation_Null",
      "Je_16_StaticThis_ArgumentToSuper",
      "Je_16_StaticThis_ArgumentToThis",
      "Je_16_SuperThis_InvalidSuperParameter",
      "Je_16_SuperThis_InvalidThisParameter",
      "Je_16_Throw_NoThrows",
      "Je_16_Throw_NotSubclass",
      "Je_16_Throw_SimpleType",
      "Je_16_Throws_This",
      "Je_16_Throw_ThrowsNotSuperclass",
      "Je_17_Unreachable_AfterThrowInConditional",
      "Je_17_Unreachable_AfterThrow",
      "Je_1_AbstractClass_AbstractConstructor",
      "Je_1_AbstractClass_Final",
      "Je_1_AbstractMethod_Body",
      "Je_1_AbstractMethodCannotBeFinal",
      "Je_1_AbstractMethod_EmptyBody",
      "Je_1_AbstractMethod_Final",
      "Je_1_AbstractMethod_Static",
      "Je_1_Access_PrivateLocal",
      "Je_1_Access_ProtectedLocal",
      "Je_1_Access_PublicLocal",
      "Je_1_Array_Data_Empty",
      "Je_1_Array_Data",
      "Je_1_Array_OnVariableNameInDecl",
      "Je_1_Cast_DoubleParenthese",
      "Je_1_Cast_Expression",
      "Je_1_Cast_LeftHandSideOfAssignment_1",
      "Je_1_Cast_LeftHandSideOfAssignment_2",
      "Je_1_Cast_NonstaticField",
      "Je_1_Cast_NoParenthesis",
      "Je_1_CastToArrayLvalue",
      "Je_1_Cast_ToMethodInvoke",
      // "Je_1_ClassDeclaration_WrongFileName_Dot.foo",
      // "Je_1_ClassDeclaration_WrongFileName",
      // "Je_1_ClassDeclaration_WrongFileName_Suffix",
      "Je_1_ClassInstantiation_InstantiateSimpleType",
      "Je_1_ClassInstantiation_InstantiateSimpleValue",
      "Je_1_Declarations_MultipleVars_Fields",
      "Je_1_Declarations_MultipleVars",
      "Je_1_Escapes_1DigitOctal_1",
      "Je_1_Escapes_1DigitOctal_2",
      "Je_1_Escapes_1DigitOctal_3",
      "Je_1_Escapes_1DigitOctal_4",
      "Je_1_Escapes_2DigitOctal_1",
      "Je_1_Escapes_2DigitOctal_2",
      "Je_1_Escapes_2DigitOctal_3",
      "Je_1_Escapes_3DigitOctal_1",
      "Je_1_Escapes_3DigitOctal_2",
      "Je_1_Escapes_3DigitOctal_3",
      "Je_1_Escapes_NonExistingEscape",
      "Je_1_Extends_NamedTypeArray",
      "Je_1_Extends_SimpleTypeArray",
      "Je_1_Extends_SimpleType",
      "Je_1_Extends_Value",
      "Je_1_FinalField_NoInitializer",
      "Je_1_For_DeclarationInUpdate",
      "Je_1_Formals_Final",
      "Je_1_Formals_Initializer_Constructor",
      "Je_1_Formals_Initializer_Method",
      "Je_1_For_MultipleDeclarationsInInit",
      "Je_1_For_MultipleUpdates",
      "Je_1_For_NotAStatementInUpdate",
      "Je_1_For_PrimaryExpInInit",
      "Je_1_For_PrimaryExpInUpdate",
      "Je_1_For_StatementInInit",
      "Je_1_Identifiers_Goto",
      "Je_1_Identifiers_Private",
      "Je_1_Implements_NamedTypeArray",
      "Je_1_Implements_SimpleTypeArray",
      "Je_1_Implements_SimpleType",
      "Je_1_Implements_Value",
      "Je_1_IncDec_IncDecNotLvalue",
      "Je_1_IncDec_Parenthesized",
      "Je_1_InstanceInitializers",
      "Je_1_InstanceOf_Null",
      "Je_1_InstanceOf_Primitive",
      "Je_1_InstanceOf_Void",
      "Je_1_Interface_ConstructorAbstract",
      "Je_1_Interface_ConstructorBody",
      "Je_1_Interface_Field",
      "Je_1_Interface_FinalMethod",
      "Je_1_Interface_MethodBody",
      "Je_1_Interface_NoBody",
      "Je_1_Interface_StaticMethod",
      // "Je_1_Interface_WrongFileName",
      "Je_1_IntRange_MinusTooBigInt",
      "Je_1_IntRange_PlusTooBigInt",
      "Je_1_IntRange_TooBigInt_InInitializer",
      "Je_1_IntRange_TooBigInt",
      "Je_1_IntRange_TooBigIntNegated",
      "Je_1_JoosTypes_Double",
      "Je_1_JoosTypes_Float",
      "Je_1_JoosTypes_Long",
      "Je_1_LabeledStatements",
      "Je_1_Literals_Class",
      "Je_1_Literals_Exponential",
      "Je_1_Literals_Float",
      "Je_1_Literals_Hex",
      "Je_1_Literals_Long",
      "Je_1_Literals_Octal",
      "Je_1_Locals_Final",
      "Je_1_Methods_MissingAccessModifier",
      "Je_1_Methods_NonAbstractNoBody",
      "Je_1_Methods_StaticFinal",
      "Je_1_MultiArrayCreation_Assign_2",
      "Je_1_MultiArrayCreation_MissingDimension_1",
      "Je_1_MultiArrayCreation_MissingDimension_2",
      "Je_1_MultiArrayCreation_MissingDimension_4",
      "Je_1_MultiArrayCreation_NoType",
      "Je_1_MultiArrayTypes_Dimensions",
      "Je_1_NegIntTooLow",
      "Je_1_NonJoosConstructs_AssignmentOperations_BitwiseAnd",
      "Je_1_NonJoosConstructs_AssignmentOperations_BitwiseOr",
      "Je_1_NonJoosConstructs_AssignmentOperations_BitwiseXOR",
      "Je_1_NonJoosConstructs_AssignmentOperations_Divide",
      "Je_1_NonJoosConstructs_AssignmentOperations_Minus",
      "Je_1_NonJoosConstructs_AssignmentOperations_Multiply",
      "Je_1_NonJoosConstructs_AssignmentOperations_Plus",
      "Je_1_NonJoosConstructs_AssignmentOperations_Remainder",
      "Je_1_NonJoosConstructs_AssignmentOperations_ShiftLeft",
      "Je_1_NonJoosConstructs_AssignmentOperations_SignShiftRight",
      "Je_1_NonJoosConstructs_AssignmentOperations_ZeroShiftRight",
      "Je_1_NonJoosConstructs_BitShift_Left",
      "Je_1_NonJoosConstructs_BitShift_SignRight",
      "Je_1_NonJoosConstructs_BitShift_ZeroRight",
      "Je_1_NonJoosConstructs_Bitwise_Negation",
      "Je_1_NonJoosConstructs_Break",
      "Je_1_NonJoosConstructs_Choice",
      "Je_1_NonJoosConstructs_Continue",
      "Je_1_NonJoosConstructs_DoWhile",
      "Je_1_NonJoosConstructs_ExpressionSequence",
      "Je_1_NonJoosConstructs_MultipleTypesPrFile",
      "Je_1_NonJoosConstructs_NestedTypes",
      "Je_1_NonJoosConstructs_PrivateFields",
      "Je_1_NonJoosConstructs_PrivateMethods",
      "Je_1_NonJoosConstructs_StaticInitializers",
      "Je_1_NonJoosConstructs_Strictftp",
      "Je_1_NonJoosConstructs_SuperMethodCall",
      "Je_1_NonJoosConstructs_Switch",
      "Je_1_NonJoosConstructs_Synchronized",
      "Je_1_NonJoosConstructs_SynchronizedStatement",
      "Je_1_NonJoosConstructs_Transient",
      "Je_1_NonJoosConstructs_UnaryPlus",
      "Je_1_NonJoosConstructs_Unicode",
      "Je_1_NonJoosConstructs_Volatile",
      "Je_1_PackagePrivate_Class",
      "Je_1_PackagePrivate_Field",
      "Je_1_PackagePrivate_Method",
      "Je_1_SuperThis_SuperAfterBlock",
      "Je_1_SuperThis_SuperAfterStatement",
      "Je_1_SuperThis_SuperInBlock",
      "Je_1_SuperThis_SuperInMethod",
      "Je_1_SuperThis_SuperThis",
      "Je_1_SuperThis_ThisAfterStatement",
      "Je_1_SuperThis_ThisInMethod",
      "Je_1_SuperThis_TwoSuperCalls",
      "Je_1_Throw_NotExpression",
      "Je_1_Throws_Array",
      "Je_1_Throws_SimpleType",
      "Je_1_Throws_Void",
      "Je_1_VoidType_ArrayCreation",
      "Je_1_VoidType_ArrayDeclaration",
      "Je_1_VoidType_Cast",
      "Je_1_VoidType_Field",
      "Je_1_VoidType_Formals",
      "Je_1_VoidType_Local",
      "Je_1_VoidType_VoidMethod",
      "Je_6_Assignable_Instanceof_SimpleTypeOfSimpleType",
      "Je_6_InstanceOf_Primitive_1",
      "Je_6_InstanceOf_Primitive_2",
      "Je_Native",
      "Je_Throws",
    )

    passTests.foreach(x => assertPass("a1/" + x))
    rejectTests.foreach(x => assertRejectBecauseOf[Throwable]("a1/" + x))
  }


}
