import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";

export default function Bookings() {
    return (
        <section className="relative py-28 px-8 md:px-20 bg-stone-950 overflow-hidden">
            <img src="https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=1600&h=600&fit=crop&auto=format" alt="Hotel pool at night" className="absolute inset-0 w-full h-full object-cover opacity-30" />
            <div className="absolute inset-0 bg-gradient-to-r from-background/90 via-background/60 to-transparent" />
            <div className="relative z-10 max-w-2xl">
                <p className="text-[11px] tracking-[0.5em] uppercase text-primary mb-4 font-light">Book Direct</p>
                <h2 className="text-4xl md:text-5xl font-normal text-foreground mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>Best rate guaranteed<br /><em>every time.</em></h2>
                <p className="text-foreground/55 font-light mb-8 max-w-md">Reserve directly with us for our lowest available rate, complimentary early check-in, and a welcome amenity curated for your stay.</p>
                <Link to="/book" className="inline-flex items-center gap-3 px-8 py-4 bg-primary text-primary-foreground text-sm tracking-[0.2em] uppercase font-medium hover:bg-primary/90 transition-colors duration-200">Reserve Now <ArrowRight size={15} /></Link>
            </div>
        </section>
    )
}