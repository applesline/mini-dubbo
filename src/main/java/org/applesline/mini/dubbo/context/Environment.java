package org.applesline.mini.dubbo.context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author liuyaping
 * 创建时间：2020年06月12日
 */
public class Environment {

    private static Logger log = LoggerFactory.getLogger(Environment.class);

    private static final Gson GSON = new GsonBuilder().create();

    private static final Properties CONFIG = new Properties();

    static {
        InputStream inputStream = Environment.class.getResourceAsStream("/mini-dubbo.properties");
        try {
            CONFIG.load(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    public static void loadConfig(String configPath) {
        Properties prop = new Properties();
        try {
            prop.load(Environment.class.getResourceAsStream(configPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.entrySet().forEach(entry->{
            CONFIG.setProperty(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
        });
        if (CONFIG.getProperty("mini-dubbo.server.config.log").equals("true")) {
            CONFIG.entrySet().forEach(entry->{
                log.info("{}={}",String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            });
        }
    }

    public static <T> T getEnv(String key,Class<T> type) {
        String value = CONFIG.getProperty(key);
        if (type == String.class) {
            return (T)value;
        }
        return GSON.fromJson(value,type);
    }

}
