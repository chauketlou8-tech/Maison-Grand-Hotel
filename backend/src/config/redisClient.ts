import { connectRedis } from "./cache";
const client = connectRedis();

export default client;