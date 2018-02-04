package love.sola.burningblood

import java.net.URL
import java.net.URLClassLoader

interface ClassTransformer {
    fun transform(name: String, transformedName: String, classData: ByteArray): ByteArray
}

class BurningBloodClassLoader(urls: Array<URL>) : URLClassLoader(urls, null) {

    private val realParent = javaClass.classLoader

    private val transformers = arrayListOf<ClassTransformer>()
    private val classLoaderExclusions = hashSetOf<String>()
    private val transformerExclusions = hashSetOf<String>()

    init {
        // classloader exclusions
        addClassLoaderExclusion("java.")
        addClassLoaderExclusion("sun.")
        addClassLoaderExclusion("org.apache.logging.")
        addClassLoaderExclusion("love.sola.burningblood.")

        // transformer exclusions
        addTransformerExclusion("javax.")
        addTransformerExclusion("org.apache.")
        addTransformerExclusion("org.lwjgl.")
        addTransformerExclusion("org.slf4j.")
        addTransformerExclusion("org.objectweb.asm.")
        addTransformerExclusion("net.java.games.")
        addTransformerExclusion("javazoom.jl.decoder.")
        addTransformerExclusion("io.sentry.")
        addTransformerExclusion("com.badlogic.gdx.")
        addTransformerExclusion("com.codedisaster.steamworks.")
        addTransformerExclusion("com.esotericsoftware.spine.")
        addTransformerExclusion("com.fastxml.jackson.core.")
        addTransformerExclusion("com.google.gson.")
        addTransformerExclusion("com.jcraft.")
    }

    fun addTransformer(transformer: ClassTransformer) {
        transformers.add(transformer)
    }

    fun addClassLoaderExclusion(prefix: String) {
        classLoaderExclusions.add(prefix)
    }

    fun addTransformerExclusion(prefix: String) {
        transformerExclusions.add(prefix)
    }

    override fun findClass(name: String): Class<*> {
        if (classLoaderExclusions.any { name.startsWith(it) }) {
            return realParent.loadClass(name)
        }
        if (transformerExclusions.any { name.startsWith(it) }) {
            return super.findClass(name)
        }
        val finalClass = transformers.fold(getClassBytes(name)) { classData, transformer ->
            transformer.transform(
                name,
                name, //TODO name mapping feature.
                classData
            )
        }
        return defineClass(name, finalClass, 0, finalClass.size)
    }

    private fun getClassBytes(name: String): ByteArray {
        getResourceAsStream(name.replace('.', '/') + ".class").use {
            return it.readBytes()
        }
    }
}
