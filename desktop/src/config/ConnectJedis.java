package config;

import redis.clients.jedis.RedisClient;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.JedisClientConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectJedis {
    public static RedisClient connect() throws IOException {
        Properties props = new Properties();

        try (InputStream inputStream = ConnectJedis.class.getClassLoader().getResourceAsStream("config/application.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("config/application.properties not found");
            }
            props.load(inputStream);
        }

        String password = props.getProperty("redis.password");
        int port = Integer.parseInt(props.getProperty("redis.port"));
        String host = props.getProperty("redis.host");

        JedisClientConfig config = DefaultJedisClientConfig.builder().user("default").password(password).build();

        return RedisClient.builder().hostAndPort(host, port).clientConfig(config).build();
    }
}