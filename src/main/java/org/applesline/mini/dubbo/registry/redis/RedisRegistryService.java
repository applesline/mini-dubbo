package org.applesline.mini.dubbo.registry.redis;

import org.applesline.mini.dubbo.ServerNode;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.registry.AbstractRegistryService;
import org.redisson.Redisson;
import org.redisson.api.RSet;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyaping
 * 创建时间：2020年06月08日
 */
public class RedisRegistryService extends AbstractRegistryService {

    private RedissonClient redissonClient;
    private RTopic topic;

    private final String rootPath = "mini-dubbo.servers";

    public RedisRegistryService() {
        this(Environment.getEnv("mini-dubbo.redis.servers",String.class).split(";"));
    }

    public RedisRegistryService(String... address) {
        Config config = new Config();
        if (address.length == 1) {
            config.useSingleServer().setAddress(address[0]);
        } else {
            config.useClusterServers().addNodeAddress(address);
        }
        redissonClient = Redisson.create(config);
        subscribeTopic();
    }

    @Override
    public void doRegister() {
        redissonClient.getSet(rootPath).add(serverNode.toString());
        topic.publish(serverNode.toString());
    }

    @Override
    public void unregister() {
        redissonClient.getSet(rootPath).remove(serverNode.toString());
    }

    @Override
    public List<ServerNode> getServerNodes() {
        RSet<String> nodeSet = redissonClient.getSet(rootPath);
        List<ServerNode> nodes = new ArrayList<>();
        nodeSet.forEach(node->{
            String[] pairs = node.split(":");
            nodes.add(new ServerNode(pairs[0],Integer.parseInt(pairs[1])));
        });
        return nodes;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    private void subscribeTopic() {
        topic = redissonClient.getTopic(rootPath);
        topic.addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence channel, String msg) {
                log.info("topic [{}] received a message [{}]",channel,msg);
                redissonClient.getSet(rootPath).add(msg);
            }
        });
    }

}
