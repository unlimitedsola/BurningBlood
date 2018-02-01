package love.sola.burningblood

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.lang.reflect.Method

val defineMethod: Method = ClassLoader::class.java.getDeclaredMethod(
    "defineClass",
    String::class.java,
    ByteArray::class.java,
    Int::class.java,
    Int::class.java
).apply { isAccessible = true }

internal fun patch(
    name: String,
    classLoader: ClassLoader = Thread.currentThread().contextClassLoader,
    patcher: (ClassWriter) -> ClassVisitor
) {
    classLoader.getResourceAsStream(name.replace('.', '/') + ".class").use {
        val cr = ClassReader(it)
        val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
        cr.accept(patcher(cw), 0)
        val data = cw.toByteArray()
        defineMethod.invoke(ClassLoader.getSystemClassLoader(), name, data, 0, data.size)
    }
}
