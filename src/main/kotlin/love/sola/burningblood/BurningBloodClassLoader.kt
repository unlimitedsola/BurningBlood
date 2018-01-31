package love.sola.burningblood

import org.objectweb.asm.*
import java.io.File
import java.net.URL
import java.net.URLClassLoader

class BurningBloodClassLoader(urls: Array<URL>) : URLClassLoader(urls) {

    override fun findClass(name: String?): Class<*> {
        if (name == "com.megacrit.cardcrawl.core.CardCrawlGame") {
            return patch(name)
        }
        return super.findClass(name)
    }

    private fun patch(name: String): Class<*> {
        getResourceAsStream(name.replace('.', '/') + ".class").use {
            val cr = ClassReader(it)
            val cw = ClassWriter(cr, 0)
            cr.accept(object : ClassVisitor(Opcodes.ASM6, cw) {
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
            }, 0)
            val data = cw.toByteArray()
            return defineClass(name, data, 0, data.size)
        }
    }

}
