import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";

export default function Dining() {
    return (
        <section className="py-28 px-8 md:px-20 bg-secondary">
            <div className="max-w-6xl mx-auto">
                <div className="flex items-end justify-between mb-12">
                    <div>
                        <p className="text-[11px] tracking-[0.5em] uppercase text-primary mb-3 font-light">Culinary</p>
                        <h2 className="text-4xl md:text-5xl font-normal" style={{ fontFamily: "'Playfair Display', serif" }}>Dining at Maison</h2>
                    </div>
                    <Link to="/restaurant" className="hidden sm:flex items-center gap-2 text-sm text-primary tracking-[0.15em] uppercase hover:gap-4 transition-all duration-200">Explore Dining <ArrowRight size={14} /></Link>
                </div>
                <div className="grid md:grid-cols-5 gap-0">
                    <div className="md:col-span-3 relative overflow-hidden group bg-stone-900">
                        <img src="https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=1000&h=700&fit=crop&auto=format" alt="Aurore restaurant" className="w-full h-80 md:h-full object-cover opacity-70 group-hover:opacity-85 group-hover:scale-105 transition-all duration-700" />
                        <div className="absolute inset-0 bg-gradient-to-t from-background/80 via-transparent to-transparent" />
                        <div className="absolute bottom-0 left-0 p-8">
                            <span className="text-[10px] tracking-[0.4em] uppercase text-primary">Fine Dining · 2 Michelin Stars</span>
                            <h3 className="text-3xl font-normal text-foreground mt-1" style={{ fontFamily: "'Playfair Display', serif" }}>Aurore</h3>
                            <p className="text-sm text-foreground/60 mt-2 max-w-xs">Chef Élise Laurent's celebrated tasting menus. Dinner only.</p>
                        </div>
                    </div>
                    <div className="md:col-span-2 flex flex-col">
                        <div className="relative overflow-hidden group flex-1 bg-stone-900">
                            <img src="https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600&h=350&fit=crop&auto=format" alt="The Atrium" className="w-full h-48 md:h-full object-cover opacity-70 group-hover:opacity-85 group-hover:scale-105 transition-all duration-700" />
                            <div className="absolute inset-0 bg-gradient-to-t from-background/80 via-transparent to-transparent" />
                            <div className="absolute bottom-0 left-0 p-6">
                                <span className="text-[10px] tracking-[0.4em] uppercase text-primary">All-Day Dining</span>
                                <h3 className="text-xl font-normal text-foreground mt-1" style={{ fontFamily: "'Playfair Display', serif" }}>The Atrium</h3>
                            </div>
                        </div>
                        <div className="bg-card p-6 border-t border-border flex-shrink-0">
                            <span className="text-[10px] tracking-[0.4em] uppercase text-primary">Private Events</span>
                            <h3 className="text-xl font-normal text-foreground mt-1 mb-2" style={{ fontFamily: "'Playfair Display', serif" }}>La Salle Dorée</h3>
                            <p className="text-xs text-muted-foreground font-light leading-relaxed">Private dining room for up to 24 guests. Bespoke menus available.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}