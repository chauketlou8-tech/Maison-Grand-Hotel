import { useState } from "react";
import { Link } from "react-router-dom";
import { ArrowRight, ChevronDown, ChevronLeft, ChevronRight, Star } from "lucide-react";
import stats from "../../constants/stats";
import roomTeasers from "../../constants/roomTeasers";
import gallery from "../../constants/gallery";
import testimonials from "../../constants/testimonials";

export default function Home() {
    const [galleryIndex, setGalleryIndex] = useState(0);

    function prevSlide() {
        setGalleryIndex((i) => (i === 0 ? gallery.length - 1 : i - 1));
    }
    function nextSlide() {
        setGalleryIndex((i) => (i === gallery.length - 1 ? 0 : i + 1));
    }

    return (
        <div>
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

            <section className="py-16 px-8 md:px-20 bg-secondary border-y border-border">
                <div className="max-w-6xl mx-auto grid grid-cols-2 md:grid-cols-4 gap-8">
                    {stats.map(({ value, label, icon: Icon }) => (
                        <div key={label} className="flex flex-col items-center text-center gap-3">
                            <Icon size={18} className="text-primary" />
                            <p className="text-4xl font-light text-foreground" style={{ fontFamily: "'Playfair Display', serif" }}>{value}</p>
                            <p className="text-xs tracking-[0.2em] uppercase text-muted-foreground">{label}</p>
                        </div>
                    ))}
                </div>
            </section>

            <section className="py-28 px-8 md:px-20">
                <div className="max-w-6xl mx-auto">
                    <div className="flex items-end justify-between mb-12">
                        <div>
                            <p className="text-[11px] tracking-[0.5em] uppercase text-primary mb-3 font-light">Accommodation</p>
                            <h2 className="text-4xl md:text-5xl font-normal" style={{ fontFamily: "'Playfair Display', serif" }}>Rooms &amp; Suites</h2>
                        </div>
                        <Link to="/rooms" className="hidden sm:flex items-center gap-2 text-sm text-primary tracking-[0.15em] uppercase hover:gap-4 transition-all duration-200">All Rooms <ArrowRight size={14} /></Link>
                    </div>
                    <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
                        {roomTeasers.map((room) => (
                            <Link key={room.name} to="/rooms" className="group block bg-stone-900 overflow-hidden">
                                <div className="relative overflow-hidden">
                                    {room.tag && <span className="absolute top-4 left-4 z-10 text-[10px] tracking-[0.3em] uppercase bg-primary text-primary-foreground px-3 py-1">{room.tag}</span>}
                                    <span className="absolute top-4 right-4 z-10 font-mono text-[10px] tracking-widest bg-background/80 text-primary px-2 py-1 border border-primary/30">{room.code}</span>
                                    <img src={room.img} alt={room.name} className="w-full h-56 object-cover opacity-75 group-hover:opacity-95 group-hover:scale-105 transition-all duration-500" />
                                </div>
                                <div className="p-5 flex items-center justify-between">
                                    <div>
                                        <p className="text-sm tracking-wider text-foreground/80">{room.name}</p>
                                    </div>
                                    <div className="text-right">
                                        <p className="text-primary font-light">€{room.price}</p>
                                        <p className="text-xs text-muted-foreground">/ night</p>
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>
                    <div className="mt-8 sm:hidden text-center">
                        <Link to="/rooms" className="inline-flex items-center gap-2 text-sm text-primary tracking-[0.15em] uppercase">View All Rooms <ArrowRight size={14} /></Link>
                    </div>
                </div>
            </section>

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

            <section className="py-16 px-8 md:px-20">
                <div className="max-w-6xl mx-auto">
                    <div className="relative overflow-hidden bg-stone-900 mb-4" style={{ aspectRatio: "16/7" }}>
                        {gallery.map(({ img, alt }, i) => (
                            <div key={alt} className="absolute inset-0 transition-opacity duration-500" style={{ opacity: i === galleryIndex ? 1 : 0, pointerEvents: i === galleryIndex ? "auto" : "none" }}>
                                <img src={img} alt={alt} className="w-full h-full object-cover" />
                                <div className="absolute inset-0 bg-gradient-to-t from-background/40 to-transparent" />
                                <p className="absolute bottom-5 left-6 text-xs tracking-[0.25em] uppercase text-foreground/70">{alt}</p>
                            </div>
                        ))}
                        <button onClick={prevSlide} className="absolute left-4 top-1/2 -translate-y-1/2 w-10 h-10 border border-foreground/20 bg-background/60 hover:bg-background/90 hover:border-primary flex items-center justify-center transition-all duration-200" aria-label="Previous image">
                            <ChevronLeft size={18} className="text-foreground" />
                        </button>
                        <button onClick={nextSlide} className="absolute right-4 top-1/2 -translate-y-1/2 w-10 h-10 border border-foreground/20 bg-background/60 hover:bg-background/90 hover:border-primary flex items-center justify-center transition-all duration-200" aria-label="Next image">
                            <ChevronRight size={18} className="text-foreground" />
                        </button>
                        <div className="absolute top-5 right-6 text-xs font-mono text-foreground/50 tracking-widest">
                            {String(galleryIndex + 1).padStart(2, "0")} / {String(gallery.length).padStart(2, "0")}
                        </div>
                    </div>
                    <div className="grid grid-cols-5 gap-2">
                        {gallery.map(({ img, alt }, i) => (
                            <button key={alt} onClick={() => setGalleryIndex(i)} className={`overflow-hidden bg-stone-900 transition-all duration-200 ${i === galleryIndex ? "ring-1 ring-primary opacity-100" : "opacity-50 hover:opacity-75"}`} aria-label={alt}>
                                <img src={img} alt={alt} className="w-full h-16 object-cover" />
                            </button>
                        ))}
                    </div>
                </div>
            </section>

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

            <section className="relative py-28 px-8 md:px-20 bg-stone-950 overflow-hidden">
                <img src="https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=1600&h=600&fit=crop&auto=format" alt="Hotel pool at night" className="absolute inset-0 w-full h-full object-cover opacity-30" />
                <div className="absolute inset-0 bg-gradient-to-r from-background/90 via-background/60 to-transparent" />
                <div className="relative z-10 max-w-2xl">
                    <p className="text-[11px] tracking-[0.5em] uppercase text-primary mb-4 font-light">Book Direct</p>
                    <h2 className="text-4xl md:text-5xl font-normal text-foreground mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>Best rate guaranteed<br /><em>every time.</em></h2>
                    <p className="text-foreground/55 font-light mb-8 max-w-md">Reserve directly with us for our lowest available rate, complimentary early check-in, and a welcome amenity curated for your stay.</p>
                    <Link to="/booking" className="inline-flex items-center gap-3 px-8 py-4 bg-primary text-primary-foreground text-sm tracking-[0.2em] uppercase font-medium hover:bg-primary/90 transition-colors duration-200">Reserve Now <ArrowRight size={15} /></Link>
                </div>
            </section>
        </div>
    );
}