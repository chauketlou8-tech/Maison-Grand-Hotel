import app from "./app";
import { db_url, port } from "./src/config/env";
import { logger } from "./src/config/logger";
import connectDB from "./src/config/db";
import { connectRedis } from "./src/config/cache";
import websocket from "./src/sockets/websock";
import settingsSocket from "./src/sockets/settingsSocket";
import "./src/jobs/cancelExpiredReservations";
import "./src/jobs/NotifyCheckInReminder";
import "./src/jobs/sendFeedBackRequest";

class Server {
    public async start() {
        try {
            connectDB(db_url!);
            await connectRedis();

            void websocket;
            void settingsSocket;

            logger.info("Connected to database");
            logger.info("Redis Connected");

            app.listen(port, () => {
                logger.info(`Server started on port ${port}`);
            });

        } catch (err) {
            logger.error(`Error connecting to DB: ${err}`);
            process.exit(1);
        }
    }
}

const server = new Server();
void server.start();