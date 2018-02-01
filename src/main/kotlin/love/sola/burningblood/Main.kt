package love.sola.burningblood

import com.megacrit.cardcrawl.desktop.DesktopLauncher
import com.sun.xml.internal.fastinfoset.util.StringArray
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.full.staticFunctions

fun main(args: Array<String>) {
    patch("com.megacrit.cardcrawl.core.CardCrawlGame") { cw ->
        object : ClassVisitor(Opcodes.ASM6, cw) {
            override fun visitMethod(
                access: Int,
                name: String?,
                desc: String?,
                signature: String?,
                exceptions: Array<out String>?
            ): MethodVisitor? {
                val mv = cv.visitMethod(access, name, desc, signature, exceptions)
                if (name == "<init>") {
                    return object : MethodVisitor(Opcodes.ASM6, mv) {
                        override fun visitLineNumber(line: Int, start: Label?) {
                            mv.visitLineNumber(line, start)
                            if (line == 187) {
                                mv.visitFieldInsn(
                                    Opcodes.GETSTATIC,
                                    "java/lang/System",
                                    "out",
                                    "Ljava/io/PrintStream;"
                                )
                                mv.visitLdcInsn("HELLO ASM!")
                                mv.visitMethodInsn(
                                    Opcodes.INVOKEVIRTUAL,
                                    "java/io/PrintStream",
                                    "println",
                                    "(Ljava/lang/Object;)V",
                                    false
                                )
                            }
                        }
                    }
                }
                return mv
            }
        }
    }
    com.megacrit.cardcrawl.core.CardCrawlGame.VERSION_NUM = "[Burning Blood] (snapshot)"
    com.megacrit.cardcrawl.desktop.DesktopLauncher.main(args)
}
