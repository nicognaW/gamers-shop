package com.example.springify

import com.example.common.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Table
import org.kodein.di.ktor.controller.DIController
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import org.kodein.type.erasedOf
import javax.naming.ConfigurationException
import kotlin.reflect.full.createInstance

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.componentScanner() {
    val scannerPackageName = (environment.config.propertyOrNull("ktor.scanner.package") ?: throw ConfigurationException(
        "没配置ktor.scanner.package"
    )).getString()
    val tableScanEnabled = (environment.config.propertyOrNull("ktor.scanner.scan_tables")
        ?: if (this.environment.developmentMode) "true" else "false") == "true"
    val componentScanEnabled = (environment.config.propertyOrNull("ktor.scanner.enable") ?: "true") == "true"
    val clazzSet = getClasses(scannerPackageName)
    if (componentScanEnabled) scanComponent(clazzSet)
    if (tableScanEnabled) scanTable(clazzSet)
}

private fun Application.scanTable(clazzSet: Set<Class<*>>) {
    val tableSet: MutableSet<Table> = mutableSetOf()
    for (clazz in clazzSet) {
        if (Table::class.java.isAssignableFrom(clazz)) {
            tableSet.add(clazz.kotlin.objectInstance as Table)
        }
    }
    DatabaseFactory.initTables(tableSet)
}

/**
 * 扫描目标集合内所有类，并将Component类以及Controller类进行DI注册绑定
 *
 * @author Nicog
 * @param clazzSet 目标集合
 */
private fun Application.scanComponent(clazzSet: Set<Class<*>>) {
    di {
        fun register(clazz: Class<*>): Any {
            val instance = if (ApplicationAware::class.java.isAssignableFrom(clazz))
                clazz.getConstructor(Application::class.java).newInstance(this@scanComponent)
            else clazz.kotlin.createInstance()
            Bind(erasedOf(instance)) with singleton { instance }
            return instance
        }
        /**
         * 在包集合遍历中，剔除注解类本身，在未来版本中将springify单独打包编译时，可去掉这个判断。
         * <p>
         * 在when条件中，判断类含有哪项注解，根据不同的注解类型，选择不同的行为。例如对于@Controller注解的类，不但要将其注册至DI容器，还要为其注册Routing
         * </p>
         */
        for (clazz in clazzSet) if (!springifyAnnotations.contains(clazz.kotlin)) when {
            clazz.annotations.contains(Component()) -> {
                register(clazz)
            }
            clazz.annotations.contains(Controller()) -> {
                val instance = register(clazz)
                if (DIController::class.java.isAssignableFrom(clazz)) {
                    routing {
                        (instance as DIController).apply { getRoutes() }
                    }
                } else {
                    log.warn("$clazz is annotated by @Controller but does not extend DIController")
                }
            }
            clazz.annotations.contains(Repository()) -> {
                register(clazz)
            }
            clazz.annotations.contains(Service()) -> {
                register(clazz)
            }
        }
    }
}