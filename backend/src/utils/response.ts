//helper class to format responses
import { res } from "../types/express"

class Response {

    public success(res: res, meta?: { message?: string, data?: any }, status = 200) {
        return res.status(status).json({
            success: true,
            message: meta?.message || null,
            data: meta?.data || null,
            status,
        });
    }

    public notFound(res: res, meta?: { message?: string, data?: any }, status = 404) {
        return res.status(status).json({
            success: false,
            message: meta?.message || null,
            data: meta?.data || null,
            status,
        });
    }
}

export default new Response();