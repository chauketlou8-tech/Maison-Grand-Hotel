import { useState, useEffect } from "react";
import { Star } from "lucide-react";
import getReviews from "../../services/getReviews";
import type { Review } from "../../types/review";

export default function Testimonials() {
    const [reviews, setReviews] = useState<Review[]>([]);

    useEffect(() => {
        const setRvs = async () => {
            const data = await getReviews();
            if (data) setReviews(data);
        }

        void setRvs();
    }, []);

    const displayReviews = reviews.slice(0, 6);
    const ghostsNeeded = Math.max(0, 6 - displayReviews.length);

    return (
        <section className="py-28 px-8 md:px-20 border-t" style={{ backgroundColor: "#1a1814", borderColor: "rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-6xl mx-auto">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-center" style={{ color: "#c9a84c" }}>Guest Voices</p>
                <h2 className="text-4xl font-normal text-center mb-16" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>What our guests say</h2>

                <div className="grid md:grid-cols-3 gap-8">
                    {displayReviews.map((t) => (
                        <div key={t.id} className="border p-8 flex flex-col gap-5 transition-colors duration-300" style={{ borderColor: "rgba(201, 168, 76, 0.15)" }}>
                            <div className="flex gap-1">
                                {[...Array(t.rating)].map((_, i) => (
                                    <Star key={i} size={12} style={{ fill: "#c9a84c", color: "#c9a84c" }} />
                                ))}
                            </div>

                            <blockquote className="text-lg font-normal italic leading-relaxed flex-1" style={{ fontFamily: "'Playfair Display', serif", color: "rgba(240, 235, 224, 0.75)" }}>
                                "{t.comment}"
                            </blockquote>

                            <div>
                                <p className="text-sm" style={{ color: "rgba(240, 235, 224, 0.7)" }}>{t.name}</p>
                                <p className="text-xs tracking-wider" style={{ color: "#9a9080" }}>Verified Guest</p>
                            </div>
                        </div>
                    ))}

                    {[...Array(ghostsNeeded)].map((_, i) => (
                        <div key={`ghost-${i}`} className="border p-8 flex flex-col gap-5 animate-pulse" style={{ borderColor: "rgba(201, 168, 76, 0.05)" }}>
                            <div className="flex gap-1">
                                {[...Array(5)].map((_, j) => (
                                    <Star key={j} size={12} style={{ fill: "#3a3428", color: "#3a3428" }} />
                                ))}
                            </div>

                            <div className="flex-1 flex flex-col gap-3">
                                <div className="h-4 bg-[#3a3428] w-full rounded-sm"></div>
                                <div className="h-4 bg-[#3a3428] w-5/6 rounded-sm"></div>
                                <div className="h-4 bg-[#3a3428] w-4/6 rounded-sm"></div>
                            </div>

                            <div>
                                <div className="h-3 bg-[#3a3428] w-1/3 rounded-sm mb-2"></div>
                                <div className="h-2 bg-[#2a2520] w-1/4 rounded-sm"></div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}