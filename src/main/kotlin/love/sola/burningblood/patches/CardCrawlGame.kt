package love.sola.burningblood.patches

import love.sola.burningblood.ClassTransformer
import org.objectweb.asm.*

class MainGameInstanceTransformer : ClassTransformer {

    override fun transform(name: String, transformedName: String, classData: ByteArray): ByteArray {
        if (name == "com.megacrit.cardcrawl.core.CardCrawlGame") {
            val cr = ClassReader(classData)
            val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
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
                                    mv.visitVarInsn(
                                        Opcodes.ALOAD,
                                        0
                                    )
                                    mv.visitMethodInsn(
                                        Opcodes.INVOKESTATIC,
                                        "love/sola/burningblood/BurningBlood",
                                        "initialize",
                                        "(Lcom/megacrit/cardcrawl/core/CardCrawlGame;)V",
                                        false
                                    )
                                }
                            }
                        }
                    }
                    return mv
                }
            }, 0)
            return cw.toByteArray()
        } else {
            return classData
        }
    }

}
