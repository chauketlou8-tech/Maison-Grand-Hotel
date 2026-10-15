import { createClient } from "redis";
import { logger } from "./logger";
import { redis_username as username, redis_password as password, redis_host as host, redis_port as port } from "./env";

export const connectRedis = async () => {
    const client = createClient({
        username,
        password,
        socket: {
            host,
            port
        }
    });

    client.on("error", (err) => {
        logger.error(`Redis Error: ${err}`);
    });

    await client.connect();
    return client;
}