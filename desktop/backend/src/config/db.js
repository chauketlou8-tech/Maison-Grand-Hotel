const { Pool } = require('pg');

const connectDB = (url) => {
    return new Pool({
        connectionString: url,
        host: "localhost",
    });
}

module.exports = connectDB;