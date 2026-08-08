import roomTeasers from "../../constants/roomTeasers";
import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";

export default function Rooms() {
    return (
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
    )
}