package org.applesline.mini.dubbo.context;

import com.google.common.base.Stopwatch;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liuyaping
 * 创建时间：2020年06月08日
 */
public class RpcContext {

    private static final Set<Module> MODULES = new HashSet<>();
    private static Injector INJECTOR;

    private static final ThreadLocal<Stopwatch> THREAD_LOCAL = new ThreadLocal<>();

    static {
        MODULES.add(Configuration.INSTANCE);
    }

    private RpcContext() {}

    public synchronized static <T> T getBean(Class<T> type) {
        if (INJECTOR == null) {
            throw new IllegalStateException("获取实例前请先配置guice");
        }
        return INJECTOR.getInstance(type);
    }

    public static void setConfiguration(Module... modules) {
        THREAD_LOCAL.set(Stopwatch.createStarted());
        synchronized (MODULES) {
            for(int i=0;i<modules.length;i++) {
                if (modules[i].getClass() == Configuration.class) {
                    MODULES.remove(Configuration.INSTANCE);
                }
                MODULES.add(modules[i]);
            }
            if (INJECTOR == null) {
                INJECTOR = Guice.createInjector(MODULES);
            }
        }
    }

    public static void initContext() {
        setConfiguration();
    }

    public static ThreadLocal<Stopwatch> getThreadLocal() {
        return THREAD_LOCAL;
    }
}
