package org.applesline.mini.dubbo.registry.zookeeper;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.applesline.mini.dubbo.ServerNode;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.registry.AbstractRegistryService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */

public class ZookeeperRegistryService extends AbstractRegistryService {


    private ZkClient zkClient;

    private final String rootPath = "/mini-dubbo/servers";

    public ZookeeperRegistryService() {
        this(Environment.getEnv("mini-dubbo.zookeeper.servers",String.class));
    }

    public ZookeeperRegistryService(String connectString) {
        zkClient = new ZkClient(connectString);
        initRootPath();
        subscribeChildChanges();
    }

    public void doRegister() {
            String path = rootPath + "/" + serverNode.toString();
            if (!zkClient.exists(path)) {
                zkClient.createPersistent(path);

            }
    }

    public List<ServerNode> getServerNodes() {
        List<ServerNode> serverNodes = CACHE.getIfPresent(rootPath);
        if (serverNodes != null) {
            return serverNodes;
        }
        serverNodes = new ArrayList<ServerNode>(16);
        List<String> servers = zkClient.getChildren(rootPath);
        for (String server : servers) {
            String[] pair = server.split(":");
            serverNodes.add(new ServerNode(pair[0],Integer.parseInt(pair[1])));
        }
        return serverNodes;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    public void unregister() {
        zkClient.delete(rootPath + "/" + serverNode);
    }

    public void initRootPath() {
        if (!zkClient.exists(rootPath)) {
            String[] paths = rootPath.split("/");
            if (!zkClient.exists("/"+paths[1])) {
                zkClient.createPersistent("/"+paths[1]);
            }
            if (!zkClient.exists(rootPath)) {
                zkClient.createPersistent(rootPath);
            }
        }
    }

    public void subscribeChildChanges(){
        zkClient.subscribeChildChanges(rootPath,new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                CACHE.invalidate(s);
                List<ServerNode> serverNodes = new ArrayList<>();
                list.forEach(server->{
                    String[] pairs = server.split(":");
                    serverNodes.add(new ServerNode(pairs[0],Integer.valueOf(pairs[1])));
                });
                CACHE.put(s,serverNodes);
            }
        });
        zkClient.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                System.out.println(keeperState.name());
            }

            @Override
            public void handleNewSession() throws Exception {

            }

            @Override
            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {

            }
        });
    }

}
