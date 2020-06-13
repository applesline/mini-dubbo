package org.applesline.mini.dubbo.invoker;

import com.google.inject.Inject;
import io.netty.channel.Channel;
import org.applesline.mini.dubbo.common.Util;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.serializer.Serializer;
import org.applesline.mini.dubbo.transporter.Transporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuyaping
 * 创建时间：2020年06月04日
 */
public abstract class AbstractInvoker implements Invoker {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final int TIMEOUT = Environment.getEnv("mini-dubbo.timeout",Integer.class);

    protected static final ConcurrentHashMap<Integer,Result> RPC_RESULTS = new ConcurrentHashMap(256);

    public static final AtomicInteger RequestId = new AtomicInteger(0);

    @Inject
    protected Transporter transporter;
    @Inject
    protected Serializer serializer;

    protected static Channel channel;

    @Override
    public Object invoke(Invocation invocation) {
        openChannel();

        Result result = new Result();
        RequestId.compareAndSet(Integer.MAX_VALUE,0);
        int requestId = RequestId.incrementAndGet();
        RPC_RESULTS.put(requestId,result);
        doInvoke(invocation,requestId);
        result.waitForResult(TIMEOUT);
        RPC_RESULTS.remove(requestId);

        try {
            if ("void".equals(result.getReturnType()) || result.getData() == null) {
                return null;
            }
            Class returnClass = Util.toClass(result.getReturnType());
            if (returnClass == null) {
                returnClass = Class.forName(result.getReturnType());
            }
            return serializer.deserialize(returnClass,serializer.serialize(result.getData()));
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    protected abstract void doInvoke(Invocation invocation,int requestId);

    private void openChannel() {
        if (channel == null) {
            synchronized (this) {
                if (channel == null) {
                    try {
                        channel = transporter.openClient().connect();
                    } catch (Exception e) {
                        channel = transporter.openClient().reconnect(0);
                    }
                }
            }
        }
    }

    public static void setChannel(Channel channel) {
        AbstractInvoker.channel = channel;
    }

    public static Result getResult(int requestId) {
        return RPC_RESULTS.get(requestId);
    }
}


