import asyncHandler from "../middleware/asyncHandler";
import Response from "../utils/response";
import type { req, res, next } from "../types/express";
import connection from "../config/connection";

const submitFeedBack = asyncHandler(async (req: req, res: res, next: next) => {
    const { rating, comments, name, email } = req.body;

    const review = await connection.query(`
        insert into reviews(name, email, comment, rating) values($1, $2, $3, $4) returning *
    `, [name, email, comments, rating]);

    if (!review) {
        return Response.notFound(res, {
            message: "Error recording the review"
        });
    }

    return Response.success(res, {
        message: "Feedback submitted successfully",
        data: {
            success: true,
            review: review.rows[0]
        }
    })
});

const getReviews = asyncHandler(async (req: req, res: res, next: next) => {
    const reviews = await connection.query(`
        select * from reviews order by created_at desc;
    `);

    if (!reviews) {
        return Response.notFound(res, {
            message: "Reviews not found"
        });
    }

    return Response.success(res, {
        data: reviews.rows,
    });
})

export {
    submitFeedBack,
    getReviews,
}