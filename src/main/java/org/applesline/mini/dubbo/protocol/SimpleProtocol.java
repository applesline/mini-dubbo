package org.applesline.mini.dubbo.protocol;

import com.google.inject.Inject;
import org.applesline.mini.dubbo.registry.RegistryService;
import org.applesline.mini.dubbo.transporter.Transporter;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */

public class SimpleProtocol extends AbstractProtocol {

    @Inject
    private Transporter transporter;
    @Inject
    private RegistryService registryService;

    public void doExport(int port) {

        try {
            channel = transporter.openServer().bind(port);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            System.exit(-1);
        }
        registryService.register();
    }

    public void destroy() {
        registryService.unregister();
        transporter.openServer().close();
    }

}
