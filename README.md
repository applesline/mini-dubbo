# mini-dubbo

##### 迷你版dubbo实现，零配置、易于使用，支持redis、zookeeper注册中心，轻量级ioc容器guice提供对象容器化支持

---

> 快速入门

#### 1.添加依赖

##### 1.1 maven中央仓库下载依赖包（暂不可用）
```xml
<dependencies>
    <dependency>
        <groupId>org.applesline</groupId>
        <artifactId>mini-dubbo</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

##### 1.2 自行编译jar包（推荐）
###### 1.2.1 下载源码
```bash
git clone https://github.com/applesline/mini-dubbo.git
```

###### 1.2.2 使用maven命令编译、打包
```bash
mvn clean package
```

###### 1.2.3 将编译好的 mini-dubbo-1.0.0.jar导入到项目类路径下
---

#### 2.业务接口定义
```java
package org.applesline.api;

public interface GreetingService {
    String sayHello(String name);
}
```
---

#### 3.业务接口实现
```java
package org.applesline;

import org.applesline.api.GreetingService;

public class GreetingServiceImpl implements GreetingService {
    public String sayHello(String name) {
        return "hi " + name;
    }
}
```
---

#### 4.接口与实现类进行绑定
```java
package org.applesline;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.applesline.api.GreetingService;

public class ServerConfig extends AbstractModule {
    public void configure() {
        bind(GreetingService.class).to(GreetingServiceImpl.class).in(Singleton.class);
    }
}
```
---

##### 5.编写服务端程序并注册服务（guice中注册服务，相当于spring的ioc容器）
```java
package org.applesline;

import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.protocol.Protocol;

public class ServiceProvider {

    public static void main(String[] args) {
        // 将服务注册到guice中，默认使用本地zookeeper作为注册中心（127.0.0.1:2181），需要确保本地安装并启动了zk
        RpcContext.setConfiguration(new ServerConfig());
        Protocol protocol = RpcContext.getBean(Protocol.class);
        protocol.export();
    }
}

```
---

#### 6.编写客户端服务
```java
package org.applesline;

import org.applesline.api.GreetingService;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.proxy.ProxyFactory;

public class ServiceConsumer {

    public static void main(String[] args) {
        // 默认使用本地zookeeper作为注册中心（127.0.0.1:2181），需要确保本地安装并启动了zk
        RpcContext.initContext();
        GreetingService greetingService = ProxyFactory.getProxyObject(GreetingService.class);
        System.out.println(greetingService.sayHello("mini-dubbo"));
    }
}
```
### 自此便可以愉快的体验mini-dubbo带来的乐趣了

---
> 联系作者

#### mini-dubbo相关问题请联系作者： <applesline@163.com>
