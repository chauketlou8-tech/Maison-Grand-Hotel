import { ChevronDown, ArrowRight } from "lucide-react";
import { Link } from "react-router-dom";

export default function HeroBanner() {
    return (
        <section className="relative h-screen min-h-[700px] flex items-end pb-28 bg-stone-950">
            <img src="https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=1800&h=1100&fit=crop&auto=format" alt="Maison Grand Hôtel exterior at dusk" className="absolute inset-0 w-full h-full object-cover object-center opacity-55" />
            <div className="absolute inset-0 bg-gradient-to-t from-background via-background/45 to-transparent" />
            <div className="relative z-10 px-8 md:px-20 max-w-5xl">
                <p className="text-[11px] tracking-[0.5em] uppercase text-primary mb-5 font-light">Est. 1924 · Paris, France</p>
                <h1 className="text-5xl md:text-7xl font-normal text-foreground leading-[1.05] mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>Where time<br /><em className="text-primary">slows</em> down.</h1>
                <p className="text-base md:text-lg text-foreground/60 max-w-md mb-10 font-light leading-relaxed">A century of quiet distinction. Forty-seven rooms and suites, two restaurants, and a staff who remember your name.</p>
                <div className="flex flex-col sm:flex-row gap-4">
                    <Link to="/booking" className="inline-flex items-center gap-3 px-8 py-4 bg-primary text-primary-foreground text-sm tracking-[0.2em] uppercase font-medium hover:bg-primary/90 transition-colors duration-200">Reserve a Room <ArrowRight size={15} /></Link>
                    <Link to="/rooms" className="inline-flex items-center gap-3 px-8 py-4 border border-foreground/20 text-foreground/70 text-sm tracking-[0.2em] uppercase font-light hover:border-primary hover:text-primary transition-colors duration-200">Explore Rooms</Link>
                </div>
            </div>
            <div className="absolute bottom-10 left-1/2 -translate-x-1/2 flex flex-col items-center gap-2 opacity-30">
                <div className="w-px h-10 bg-foreground/40 animate-pulse" />
                <ChevronDown size={14} className="text-foreground/40" />
            </div>
        </section>
    )
}