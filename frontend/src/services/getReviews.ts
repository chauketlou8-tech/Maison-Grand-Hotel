import axios from "axios"

export default async function getReviews() {
    const { data } = await axios.get("/api/v1/reviews");
    return data.data;
}