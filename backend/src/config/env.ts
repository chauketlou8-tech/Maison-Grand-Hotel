import dotenv from "dotenv";
import path from "path"

dotenv.config({
    path: path.join(__dirname, "../../.env"),
});

const port = process.env.PORT || 3002;
const node_env = process.env.NODE_ENV;
const db_url = process.env.DATABASE_URL;
const email_user = process.env.EMAIL_USER;
const email_password = process.env.EMAIL_PASSWORD;
const yoco_secret = process.env.YOCO_SECRET_KEY;
const base_url = process.env.BASE_URL;
const yoco_webhook_secret = process.env.YOCO_WEBHOOK_SECRET_KEY;
const success_url = process.env.SUCCESS_URL;
const payment_url = process.env.PAYMENT_URL;
const feedback_url = process.env.FEEDBACK_URL;

if (!node_env) {
    throw new Error('Missing environment environment');
}

if (!db_url && node_env === "development") {
    throw new Error("Database URL missing");
}

if (!email_user && node_env === "development") {
    throw new Error("Missing email user");
}

if (!email_password && node_env === "development") {
    throw new Error("Missing email password");
}

if (!yoco_secret && node_env === "development") {
    throw new Error("Missing yoco secret");
}

if (!base_url && node_env === "development") {
    throw new Error("Missing base_url");
}

if (!yoco_webhook_secret && node_env === "development") {
    throw new Error("Missing yoco webhook secret");
}

if (!success_url && node_env === "development") {
    throw new Error("Missing success_url");
}

if (!payment_url && node_env === "development") {
    throw new Error("Missing payment_url");
}

if (!feedback_url && node_env === "development") {
    throw new Error("Missing success_url");
}
    
export {
    port,
    node_env,
    db_url,
    email_user,
    email_password,
    yoco_secret,
    base_url,
    yoco_webhook_secret,
    success_url,
    payment_url,
    feedback_url,
}