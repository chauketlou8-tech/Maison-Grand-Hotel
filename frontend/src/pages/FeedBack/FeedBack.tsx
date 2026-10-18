import { type FormEvent, useState } from "react";
import { Link } from "react-router-dom";
import { AlertCircle, CheckCircle, Loader2, Star } from "lucide-react";
import axios from "axios";
import submitFeedback from "../../services/submitFeedback";
import { BackgroundWrapper } from "./BackgroundWrapper"

export default function Feedback() {
    const [rating, setRating] = useState(0);
    const [hoveredRating, setHoveredRating] = useState(0);
    const [comments, setComments] = useState("");
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");

    const [isLoading, setIsLoading] = useState(false);
    const [sent, setSent] = useState(false);
    const [error, setError] = useState("");

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setError("");

        if (rating === 0) {
            setError("Please select a star rating.");
            return;
        }

        if (!name.trim()) {
            setError("Name is required.");
            return;
        }

        if (!email.trim() || !/\S+@\S+\.\S+/.test(email)) {
            setError("A valid email is required.");
            return;
        }

        if (!comments.trim()) {
            setError("Please write a comment.");
            return;
        }

        setIsLoading(true);

        const minLoadingTime = 3500;
        const startTime = Date.now();

        try {
            await submitFeedback(rating, comments, name, email);

            const deltaTime = Date.now() - startTime;
            const remainingTime = minLoadingTime - deltaTime;

            setTimeout(() => {
                setSent(true);
                setIsLoading(false);
            }, remainingTime > 0 ? remainingTime : 0);
        }

        catch (err) {
            const deltaTime = Date.now() - startTime;
            const remainingTime = minLoadingTime - deltaTime;

            setTimeout(() => {
                if (axios.isAxiosError(err)) {
                    setError(err.response?.data?.message || "Something went wrong. Please try again.");
                } else {
                    setError("Something went wrong. Please try again.");
                }
            }, remainingTime > 0 ? remainingTime : 0);

            setIsLoading(false);
        }
    }

    if (sent) {
        return (
            <BackgroundWrapper>
                <div className="text-center bg-[#1a1814]/70 backdrop-blur-md border border-[#c9a84c]/20 p-10 shadow-[0_0_50px_rgba(201,168,76,0.05)]">
                    <div className="mx-auto w-20 h-20 bg-[#4a9d6a]/10 rounded-full flex items-center justify-center mb-6">
                        <CheckCircle size={40} className="text-[#4a9d6a]" />
                    </div>
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Thank You</p>
                    <h1 className="text-3xl font-normal mb-4 text-[#f0ebe0] font-display">We value your feedback</h1>
                    <p className="text-sm font-light leading-relaxed mb-8 text-[#9a9080]">
                        Your review helps us make Maison Grand Hotel even better for every guest.
                    </p>
                    <Link to="/" className="inline-flex items-center justify-center px-8 py-4 bg-[#c9a84c] text-[#0f0e0c] text-sm tracking-[0.2em] uppercase font-medium hover:bg-[#d4b85a] transition-all duration-300 shadow-[0_0_20px_rgba(201,168,76,0.3)] hover:shadow-[0_0_30px_rgba(201,168,76,0.5)]">
                        Return Home
                    </Link>
                </div>
            </BackgroundWrapper>
        );
    }

    return (
        <BackgroundWrapper>
            <div className="bg-[#1a1814]/70 backdrop-blur-md border border-[#c9a84c]/20 p-10 shadow-[0_0_50px_rgba(0,0,0,0.5)] w-[750px]">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Leave a Review</p>
                <h1 className="text-3xl font-normal mb-4 text-[#f0ebe0] font-display">How was your stay?</h1>
                <p className="text-sm font-light leading-relaxed mb-8 text-[#9a9080]">
                    We would love to hear your thoughts about your recent visit to Maison Grand Hotel.
                </p>

                {error && (
                    <div className="bg-red-500/10 border border-red-500 p-4 flex items-center gap-3 mb-6">
                        <AlertCircle size={16} className="text-red-500 flex-shrink-0" />
                        <p className="text-red-500 text-sm font-light">{error}</p>
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div className="mb-8">
                        <label className="block text-[10px] tracking-[0.3em] uppercase mb-4 text-[#9a9080] text-center">Rate your stay</label>
                        <div className="flex justify-between items-center bg-[#0f0e0c]/50 border border-[#c9a84c]/15 px-8 py-4">
                            {[1, 2, 3, 4, 5].map((star) => (
                                <button
                                    key={star}
                                    type="button"
                                    onClick={() => setRating(star)}
                                    onMouseEnter={() => setHoveredRating(star)}
                                    onMouseLeave={() => setHoveredRating(0)}
                                    className="focus:outline-none transition-transform hover:scale-125 duration-200"
                                >
                                    <Star
                                        size={36}
                                        className={`transition-all duration-300 ${
                                            star <= (hoveredRating || rating) ? "fill-[#c9a84c] text-[#c9a84c] drop-shadow-[0_0_8px_rgba(201,168,76,0.5)]" : "text-[#9a9080]/30 hover:text-[#9a9080]/50"
                                        }`}
                                    />
                                </button>
                            ))}
                        </div>
                    </div>

                    <div className="grid grid-cols-2 gap-4 mb-6">
                        <div>
                            <label className="block text-[10px] tracking-[0.3em] uppercase mb-2 text-[#9a9080]">Name</label>
                            <input
                                type="text"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                placeholder="John"
                                className="w-full bg-[#0f0e0c]/50 border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] focus:bg-[#0f0e0c] transition-colors"
                            />
                        </div>
                        <div>
                            <label className="block text-[10px] tracking-[0.3em] uppercase mb-2 text-[#9a9080]">Email</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="john@example.com"
                                className="w-full bg-[#0f0e0c]/50 border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] focus:bg-[#0f0e0c] transition-colors"
                            />
                        </div>
                    </div>

                    <div className="mb-8">
                        <label className="block text-[10px] tracking-[0.3em] uppercase mb-2 text-[#9a9080]">Your Comments</label>
                        <textarea
                            value={comments}
                            onChange={(e) => setComments(e.target.value)}
                            placeholder="Tell us what you loved, or what we can improve..."
                            rows={4}
                            className="w-full bg-[#0f0e0c]/50 border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] focus:bg-[#0f0e0c] transition-colors resize-none"
                        />
                    </div>

                    <button type="submit" disabled={isLoading} className="w-full py-4 bg-[#c9a84c] text-[#0f0e0c] text-sm tracking-[0.2em] uppercase font-medium hover:bg-[#d4b85a] transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 shadow-[0_0_20px_rgba(201,168,76,0.2)] hover:shadow-[0_0_30px_rgba(201,168,76,0.4)]">
                        {isLoading ? (
                            <div className="flex items-center gap-2">
                                <Loader2 size={16} className="animate-spin" />Submitting...
                            </div>
                        ) : "Submit Feedback"}
                    </button>
                </form>
            </div>
        </BackgroundWrapper>
    );
}