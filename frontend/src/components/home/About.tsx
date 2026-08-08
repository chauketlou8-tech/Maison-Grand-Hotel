import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react"

export default function About() {
    return (
        <section className="py-28 px-8 md:px-20">
            <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-16 items-center">
                <div>
                    <p className="text-[11px] tracking-[0.5em] uppercase text-primary mb-4 font-light">Our Story</p>
                    <h2 className="text-4xl md:text-5xl font-normal leading-tight mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>A century of<br /><em>considered</em> hospitality.</h2>
                    <p className="text-foreground/55 font-light leading-relaxed mb-5">Maison Grand Hôtel opened its doors in 1924 on the Rue de la Paix, steps from the Place Vendôme. Built for discerning travellers who valued substance over spectacle, it has remained in the same family for three generations.</p>
                    <p className="text-foreground/55 font-light leading-relaxed mb-8">Every decision — from the hand-embroidered linen to the single-origin coffee served at breakfast — reflects a belief that true luxury is felt in the details, not announced in the lobby.</p>
                    <Link to="/about" className="inline-flex items-center gap-2 text-sm text-primary tracking-[0.15em] uppercase hover:gap-4 transition-all duration-200">Discover the Experience <ArrowRight size={14} /></Link>
                </div>
                <div className="relative">
                    <img src="https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=700&h=850&fit=crop&auto=format" alt="Hotel grand lobby" className="w-full object-cover opacity-80" />
                    <div className="absolute -bottom-5 -left-5 bg-card border border-border p-5 hidden md:block">
                        <p className="text-3xl font-light text-primary mb-1" style={{ fontFamily: "'Playfair Display', serif" }}>100+</p>
                        <p className="text-xs tracking-[0.2em] uppercase text-muted-foreground">Years of Excellence</p>
                    </div>
                </div>
            </div>
        </section>
    )
}