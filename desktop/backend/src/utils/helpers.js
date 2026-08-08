import { v4 as uuIdv4 } from "uuid";
import crypto from "crypto";
import bcrypt from "bcryptjs";

class HelperFunctions {

    //this function helps to generate a string for use (e.g. a jwt secrete)
    static generateRandomString() {
        return crypto.randomBytes(200).toString('hex');
    }

    //function to generate random id's
    generateRandomID() {
        return uuIdv4();
    }

    //function to hash a password
    async hashPassword(password) {
        const salt = await bcrypt.genSalt(20);
        return await bcrypt.hash(password, salt);
    }

    //regex email validator
    validateEmail(email) {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return emailRegex.test(email.trim());
    }
}

export default new HelperFunctions();