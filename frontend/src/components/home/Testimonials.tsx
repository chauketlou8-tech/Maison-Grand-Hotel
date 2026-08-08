import testimonials from "../../constants/testimonials";
import { Star } from "lucide-react";

export default function Testimonials() {
    return (
        <section className="py-28 px-8 md:px-20 bg-card border-t border-border">
            <div className="max-w-6xl mx-auto">
                <p className="text-[11px] tracking-[0.5em] uppercase text-primary mb-3 font-light text-center">Guest Voices</p>
                <h2 className="text-4xl font-normal text-center mb-16" style={{ fontFamily: "'Playfair Display', serif" }}>What our guests say</h2>
                <div className="grid md:grid-cols-3 gap-8">
                    {testimonials.map((t) => (
                        <div key={t.author} className="border border-border p-8 flex flex-col gap-5 hover:border-primary/30 transition-colors duration-300">
                            <div className="flex gap-1">
                                {[...Array(t.rating)].map((_, i) => <Star key={i} size={12} className="fill-primary text-primary" />)}
                            </div>
                            <blockquote className="text-lg font-normal italic text-foreground/75 leading-relaxed flex-1" style={{ fontFamily: "'Playfair Display', serif" }}>"{t.quote}"</blockquote>
                            <div>
                                <p className="text-sm text-foreground/70">{t.author}</p>
                                <p className="text-xs text-muted-foreground tracking-wider">{t.location}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    )
}