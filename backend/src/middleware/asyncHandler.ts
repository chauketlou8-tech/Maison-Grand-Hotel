import type { req, res, next } from "../types/express"

//@ts-ignore
const asyncHandler = (fn) => {
    return async (req: req, res: res, next: next) => {
        try {
            await fn(req, res, next)
        }
        catch (error) {
            next(error);
        }
    }
}

export default asyncHandler;