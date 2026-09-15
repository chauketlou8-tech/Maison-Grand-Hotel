import { email_user } from "../config/env";
import { logger } from "../config/logger";
import transporter from "../config/mailer";

async function sendEmail(email: string, subject: string, message: string, name: string) {
    try{
        await transporter.sendMail({
            from: email_user,
            to: email,
            subject: subject,
            text: message
        });

        logger.info(`Successfully sent email to ${name}`);
    }
    catch(err){
        logger.error(`Error sending email to ${name}`, { error: err });
    }
}

export default sendEmail;