//transporter for nodemailer

import * as nodemailer from "nodemailer";
import { email_user, email_password } from "./env";

const transporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
        user: email_user as string,
        pass: email_password as string
    }
});

export default transporter;