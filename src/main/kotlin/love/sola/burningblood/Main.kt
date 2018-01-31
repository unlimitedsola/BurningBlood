package love.sola.burningblood

import com.sun.xml.internal.fastinfoset.util.StringArray
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.full.staticFunctions

fun main(args: Array<String>) {
    val clzloader = BurningBloodClassLoader(arrayOf(File("./libs/desktop-1.0.jar").toURI().toURL()))
    val clz = clzloader.loadClass("com.megacrit.cardcrawl.desktop.DesktopLauncher")
    val mainMethod = clz.getDeclaredMethod("main", Array<String>::class.java)
    mainMethod.invoke(null, args)
//    com.megacrit.cardcrawl.core.CardCrawlGame.VERSION_NUM = "[Burning Blood] (snapshot)"
//    com.megacrit.cardcrawl.desktop.DesktopLauncher.main(args)
}
