package org.applesline.mini.dubbo.context;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.applesline.mini.dubbo.invoker.AbstractInvoker;
import org.applesline.mini.dubbo.invoker.Invoker;
import org.applesline.mini.dubbo.invoker.RpcInvoker;
import org.applesline.mini.dubbo.loadbalance.AbstractLoadBalance;
import org.applesline.mini.dubbo.loadbalance.LoadBalance;
import org.applesline.mini.dubbo.loadbalance.support.RandomLoadBalance;
import org.applesline.mini.dubbo.protocol.AbstractProtocol;
import org.applesline.mini.dubbo.protocol.Protocol;
import org.applesline.mini.dubbo.protocol.SimpleProtocol;
import org.applesline.mini.dubbo.registry.AbstractRegistryService;
import org.applesline.mini.dubbo.registry.RegistryService;
import org.applesline.mini.dubbo.registry.zookeeper.ZookeeperRegistryService;
import org.applesline.mini.dubbo.serializer.Serializer;
import org.applesline.mini.dubbo.serializer.gson.GsonSerializer;
import org.applesline.mini.dubbo.transporter.Client;
import org.applesline.mini.dubbo.transporter.RemoteServer;
import org.applesline.mini.dubbo.transporter.Transporter;
import org.applesline.mini.dubbo.transporter.netty.NettyClient;
import org.applesline.mini.dubbo.transporter.netty.NettyServer;
import org.applesline.mini.dubbo.transporter.netty.NettyTransporter;
import org.applesline.mini.dubbo.transporter.netty.test.ITest;
import org.applesline.mini.dubbo.transporter.netty.test.TestImpl;

import java.util.Arrays;


/**
 * @author liuyaping
 * 创建时间：2020年06月08日
 */
public class Configuration extends AbstractModule {

    public static final Configuration INSTANCE = new Configuration();

    private Class rpcProtocolClass = SimpleProtocol.class;
    private Class transporterClass = NettyTransporter.class;
    private Class registryServiceClass = ZookeeperRegistryService.class;
    private Class loadBalanceClass = RandomLoadBalance.class;
    private Class invokerClass = RpcInvoker.class;
    private Class serializerClass = GsonSerializer.class;
    private Class remoteServerClass = NettyServer.class;
    private Class clientClass = NettyClient.class;

    private Protocol rpcProtocol;
    private Transporter transporter;
    private RegistryService registryService;
    private LoadBalance loadBalance;
    private Invoker invoker;
    private Serializer serializer;
    private RemoteServer remoteServer;
    private Client client;

    private Configuration(){}

    public Configuration(Class... implClasses) {
        for (int i=0;i<implClasses.length;i++) {
            if (implClasses[i].getSuperclass() == AbstractProtocol.class) {
                rpcProtocolClass = implClasses[i];
            }
            if (Arrays.asList(implClasses[i].getInterfaces()).contains(Transporter.class)) {
                transporterClass = implClasses[i];
            }
            if (implClasses[i].getSuperclass() == AbstractRegistryService.class) {
                registryServiceClass = implClasses[i];
            }
            if (implClasses[i].getSuperclass() == AbstractLoadBalance.class) {
                loadBalanceClass = implClasses[i];
            }
            if (implClasses[i].getSuperclass() == AbstractInvoker.class) {
                invokerClass = implClasses[i];
            }
            if (Arrays.asList(implClasses[i].getInterfaces()).contains(Serializer.class)) {
                serializerClass = implClasses[i];
            }
            if (Arrays.asList(implClasses[i].getInterfaces()).contains(RemoteServer.class)) {
                remoteServerClass = implClasses[i];
            }
            if (Arrays.asList(implClasses[i].getInterfaces()).contains(Client.class)) {
                clientClass = implClasses[i];
            }
        }
    }

    public Configuration(Object... objs) {
        for (int i=0;i<objs.length;i++) {
            if (objs[i].getClass().getSuperclass() == AbstractProtocol.class) {
                this.rpcProtocol = (AbstractProtocol)objs[i];
            }
            if (Arrays.asList(objs[i].getClass().getInterfaces()).contains(Transporter.class)) {
                this.transporter = (Transporter)objs[i];
            }
            if (objs[i].getClass().getSuperclass() == AbstractRegistryService.class) {
                this.registryService = (AbstractRegistryService)objs[i];
            }
            if (objs[i].getClass().getSuperclass() == AbstractLoadBalance.class) {
                this.loadBalance = (AbstractLoadBalance)objs[i];
            }
            if (objs[i].getClass().getSuperclass() == AbstractInvoker.class) {
                this.invoker = (AbstractInvoker)objs[i];
            }
            if (Arrays.asList(objs[i].getClass().getInterfaces()).contains(Serializer.class)) {
                this.serializer = (Serializer) objs[i];
            }
            if (Arrays.asList(objs[i].getClass().getInterfaces()).contains(RemoteServer.class)) {
                this.remoteServer = (RemoteServer) objs[i];
            }
            if (Arrays.asList(objs[i].getClass().getInterfaces()).contains(Client.class)) {
                this.client = (Client) objs[i];
            }
        }

    }

    @Override
    protected void configure() {
        bind(ITest.class).to(TestImpl.class).in(Singleton.class);
        if (rpcProtocol != null) {
            bind(Protocol.class).toInstance(rpcProtocol);
        } else {
            bind(Protocol.class).to(rpcProtocolClass).in(Singleton.class);
        }
        if (transporter != null) {
            bind(Transporter.class).toInstance(transporter);
        } else {
            bind(Transporter.class).to(transporterClass).in(Singleton.class);
        }
        if (registryService != null) {
            bind(RegistryService.class).toInstance(registryService);
        } else {
            bind(RegistryService.class).to(registryServiceClass).in(Singleton.class);
        }
        if (loadBalance != null) {
            bind(LoadBalance.class).toInstance(loadBalance);
        } else {
            bind(LoadBalance.class).to(loadBalanceClass).in(Singleton.class);
        }
        if (invoker != null) {
            bind(Invoker.class).toInstance(invoker);
        } else {
            bind(Invoker.class).to(invokerClass).in(Singleton.class);
        }
        if (serializer != null) {
            bind(Serializer.class).toInstance(serializer);
        } else {
            bind(Serializer.class).to(serializerClass).in(Singleton.class);
        }
        if (remoteServer != null) {
            bind(RemoteServer.class).toInstance(remoteServer);
        } else {
            bind(RemoteServer.class).to(remoteServerClass).in(Singleton.class);
        }
        if (client != null) {
            bind(Client.class).toInstance(client);
        } else {
            bind(Client.class).to(clientClass).in(Singleton.class);
        }

    }

}
