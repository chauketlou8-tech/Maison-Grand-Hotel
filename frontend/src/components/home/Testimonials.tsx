import testimonials from "../../constants/testimonials";
import { Star } from "lucide-react";

export default function Testimonials() {
    return (
        <section className="py-28 px-8 md:px-20 border-t" style={{ backgroundColor: "#1a1814", borderColor: "rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-6xl mx-auto">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-center" style={{ color: "#c9a84c" }}>Guest Voices</p>
                <h2 className="text-4xl font-normal text-center mb-16" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>What our guests say</h2>
                <div className="grid md:grid-cols-3 gap-8">
                    {testimonials.map((t) => (
                        <div key={t.author} className="border p-8 flex flex-col gap-5 transition-colors duration-300" style={{ borderColor: "rgba(201, 168, 76, 0.15)" }}>
                            <div className="flex gap-1">
                                {[...Array(t.rating)].map((_, i) => <Star key={i} size={12} style={{ fill: "#c9a84c", color: "#c9a84c" }} />)}
                            </div>
                            <blockquote className="text-lg font-normal italic leading-relaxed flex-1" style={{ fontFamily: "'Playfair Display', serif", color: "rgba(240, 235, 224, 0.75)" }}>"{t.quote}"</blockquote>
                            <div>
                                <p className="text-sm" style={{ color: "rgba(240, 235, 224, 0.7)" }}>{t.author}</p>
                                <p className="text-xs tracking-wider" style={{ color: "#9a9080" }}>{t.location}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    )
}