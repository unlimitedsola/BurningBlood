package love.sola.burningblood

import love.sola.burningblood.patches.MainGameInstanceTransformer
import java.net.URLClassLoader

class Launcher {

    private val classLoader = BurningBloodClassLoader((this::class.java.classLoader as URLClassLoader).urLs)

    init {
        classLoader.addTransformer(MainGameInstanceTransformer())
        Thread.currentThread().contextClassLoader = classLoader
    }

    fun launch(args: Array<String>) {
        val clazz = classLoader.loadClass("com.megacrit.cardcrawl.desktop.DesktopLauncher")
        val launchMethod = clazz.getMethod("main", Array<String>::class.java)
        launchMethod.invoke(null, args)
//        com.megacrit.cardcrawl.core.CardCrawlGame.VERSION_NUM = "[Burning Blood] (snapshot)"
//        com.megacrit.cardcrawl.desktop.DesktopLauncher.main(args)
    }

}
