import axios from "axios"

export default async function submitFeedback(rating: number, comments: string, name: string, email: string) {
    await axios.post("/api/v1/feedback", {
        rating,
        comments,
        name,
        email
    });
}