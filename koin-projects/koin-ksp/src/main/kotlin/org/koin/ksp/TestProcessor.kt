package org.koin.ksp

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*

import java.io.OutputStream
import kotlin.reflect.KType

/**
 * @author Fabio de Matos
 * @since 16/02/2021.
 */
open class TestProcessor : SymbolProcessor {

    lateinit var codeGenerator: CodeGenerator
    lateinit var file: OutputStream
    var invoked = false

    override fun finish() {

    }

    override fun init(
        options: Map<String, String>,
        kotlinVersion: KotlinVersion,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        this.codeGenerator = codeGenerator

        file = codeGenerator.createNewFile(Dependencies(false), "", "TestProcessor", "log")
//        emit("TestProcessor: init($options)", "")

    }

    override fun process(resolver: Resolver): List<KSAnnotated> {

        if (invoked) {
            return emptyList()
        }
        invoked = true

        val moduleKsType =
            org.koin.core.module.Module::class.java
                .let {
                    object : KSName {
                        override fun asString(): String = it.canonicalName
                        override fun getQualifier(): String = "Not important "
                        override fun getShortName(): String = it.simpleName
                    }
                }
                .let {
                    resolver.getClassDeclarationByName(it)
                        ?.asStarProjectedType()
                }
                .also {
                    emit("Did it work? $it", "    ")
                }



        resolver.getAllFiles()
            .let {
                writeSampleFile(it, moduleKsType)
            }


        return listOf()
    }

    private fun writeSampleFile(
        list: List<KSFile>,
        moduleKsType: KSType?
    ) {

        val fileKt = codeGenerator.createNewFile(Dependencies(false), "", "HELLO", "java")
        fileKt.appendText("public class HELLO{\n")
        fileKt.appendText("public int foo() { return 123455; }\n")
        fileKt.appendText("public String bar() { return \"whaaaaaat\"; }\n")


        val first = list
            .map {
                it.declarations
            }
            .flatten()
            .mapNotNull {
                it as? KSPropertyDeclaration
            }

        val second = list
            .map { it.declarations }
            .flatten()
            .mapNotNull { it as? KSClassDeclaration }
            .map { it.declarations }
            .flatten()
            .mapNotNull {
                it as? KSPropertyDeclaration
            }

        val combined = mutableListOf<KSPropertyDeclaration>()
            .also {
                it.addAll(first)
                it.addAll(second)
            }
            .filter {
                it.type.resolve().declaration == moduleKsType?.declaration
            }
            .map {
                emit(
                    "Found $it type ${it.type} -- ${it.type.resolve().declaration} from file ${it.containingFile} a // ${moduleKsType?.declaration}",
                    "   +-+-+-  "
                )
                it
            }
            .map {
                it.findActuals()
            }
            .flatten()
            .map{
                emit("What is this $it","          ---")
            }

        fileKt.appendText("}")
        return

        list
            .map {
                it.declarations
            }
            .flatten()
//            .mapNotNull { it as? KSClassDeclaration }
//            .mapNotNull { it.declarations }
//            .flatten()
            .mapNotNull { it as? KSPropertyDeclaration }
//            .filter { it.type.javaClass.canonicalName == Module::class.java.canonicalName }
            .map {


//                val a = it
//                    .let {
//                        "${it.extensionReceiver} ${it.isDelegated()}  sss ${it.findOverridee()} ${it.findExpects()} ${it.findExpects()} ${it.containingFile}" +
//                                "this --> ${it.closestClassDeclaration()}  that-> ${it.type}  ${it.type.element} -- ${it.typeParameters}" +
//                                "parent decl ${it.parentDeclaration}" +
//                                "\n +++++ ${it.type.resolve()
//                                    .isAssignableFrom(moduleType)} //////////${it.type} - ${it.type.element?.typeArguments} - ${it.type.resolve()}-  ${it.type.javaClass.canonicalName} == ${Module::class.java.canonicalName}"
//                    }

                emit(
                    "Processing ${it} - ${it.javaClass.canonicalName} - ${it.type.resolve()
                        .starProjection()}",
                    "   "
                )
                it
            }
            .joinToString(",")
            .filter {
                true
            }
            .let {
                "public String list = \"$it\";  "
            }
            .let {
                fileKt.appendText(it)
            }
        fileKt.appendText("}")

    }


    fun emit(s: String, indent: String) {
        file.appendText("$indent$s\n")
    }


}


fun OutputStream.appendText(str: String, identation: String = "") {

    this.write((identation + str).toByteArray())
}