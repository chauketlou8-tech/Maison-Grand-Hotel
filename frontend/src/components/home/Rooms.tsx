import roomTeasers from "../../constants/roomTeasers";
import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";

export default function Rooms() {
    return (
        <section className="py-28 px-8 md:px-20" style={{ backgroundColor: "#0f0e0c" }}>
            <div className="max-w-6xl mx-auto">
                <div className="flex items-end justify-between mb-12">
                    <div>
                        <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light" style={{ color: "#c9a84c" }}>Accommodation</p>
                        <h2 className="text-4xl md:text-5xl font-normal" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Rooms &amp; Suites</h2>
                    </div>
                    <Link to="/rooms" className="hidden sm:flex items-center gap-2 text-sm tracking-[0.15em] uppercase hover:gap-4 transition-all duration-200" style={{ color: "#c9a84c" }}>All Rooms <ArrowRight size={14} /></Link>
                </div>
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
                    {roomTeasers.map((room) => (
                        <Link key={room.name} to="/rooms" className="group block overflow-hidden" style={{ backgroundColor: "#1c1917" }}>
                            <div className="relative overflow-hidden">
                                {room.tag && <span className="absolute top-4 left-4 z-10 text-[10px] tracking-[0.3em] uppercase px-3 py-1" style={{ backgroundColor: "#c9a84c", color: "#0f0e0c" }}>{room.tag}</span>}
                                <span className="absolute top-4 right-4 z-10 font-mono text-[10px] tracking-widest px-2 py-1 border" style={{ backgroundColor: "rgba(15, 14, 12, 0.8)", color: "#c9a84c", borderColor: "rgba(201, 168, 76, 0.3)" }}>{room.code}</span>
                                <img src={room.img} alt={room.name} className="w-full h-56 object-cover opacity-75 group-hover:opacity-95 group-hover:scale-105 transition-all duration-500" />
                            </div>
                            <div className="p-5 flex items-center justify-between">
                                <div>
                                    <p className="text-sm tracking-wider" style={{ color: "rgba(240, 235, 224, 0.8)" }}>{room.name}</p>
                                </div>
                                <div className="text-right">
                                    <p className="font-light" style={{ color: "#c9a84c" }}>R{room.price}</p>
                                    <p className="text-xs" style={{ color: "#9a9080" }}>/ night</p>
                                </div>
                            </div>
                        </Link>
                    ))}
                </div>
                <div className="mt-8 sm:hidden text-center">
                    <Link to="/rooms" className="inline-flex items-center gap-2 text-sm tracking-[0.15em] uppercase" style={{ color: "#c9a84c" }}>View All Rooms <ArrowRight size={14} /></Link>
                </div>
            </div>
        </section>
    )
}