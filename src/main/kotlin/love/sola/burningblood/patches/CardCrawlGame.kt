package love.sola.burningblood.patches

import love.sola.burningblood.patch
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

fun patchMainGameInstance() {
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
        }
    }
}
