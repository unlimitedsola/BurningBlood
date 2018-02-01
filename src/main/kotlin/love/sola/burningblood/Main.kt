package love.sola.burningblood

import com.megacrit.cardcrawl.desktop.DesktopLauncher
import com.sun.xml.internal.fastinfoset.util.StringArray
import love.sola.burningblood.patches.patchMainGameInstance
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.full.staticFunctions

fun main(args: Array<String>) {
    patchMainGameInstance()
    com.megacrit.cardcrawl.core.CardCrawlGame.VERSION_NUM = "[Burning Blood] (snapshot)"
    com.megacrit.cardcrawl.desktop.DesktopLauncher.main(args)
}
