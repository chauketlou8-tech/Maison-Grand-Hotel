import asyncHandler from "../middleware/asyncHandler";
import type { Settings } from "../types/settings";
import type { req, res, next } from "../types/express";
import { getSettings as getSetts } from "../sockets/settingsClient";
import { NotFoundError } from "../errors";
import Response from "../utils/response";

const getSettings = asyncHandler(async (req: req, res: res, next: next) => {
    const settings: Settings | null = getSetts();

    if (!settings) {
        return next(new NotFoundError("Settings not found."));
    }

    return Response.success(res, {
        data: settings
    })
});

export default getSettings;