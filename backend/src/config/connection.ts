import { db_url } from "./env";
import connectDB from "./db";

const connection = connectDB(db_url!);
export default connection;