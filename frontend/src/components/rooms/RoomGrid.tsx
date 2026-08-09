import { useState } from "react";
import { Link } from "react-router-dom";
import { Check, ArrowRight } from "lucide-react";
import GuestIdBadge from "../../utils/GuestIDBadge";
import type { Room } from "../../types/Room"

export default function RoomGrid({ rooms }) {
    const [selectedRoom, setSelectedRoom] = useState(null);

    return (
        <section className="py-16 px-8 md:px-20" style={{ backgroundColor: "#0f0e0c" }}>
            <div className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-2 gap-8">
                {rooms.map((room: Room) => (
                    <div key={room.id} className={`group overflow-hidden border transition-all duration-300 ${selectedRoom === room.id ? "border-primary" : "border-border hover:border-primary/30"}`} style={{ backgroundColor: "#1a1814", borderColor: selectedRoom === room.id ? "#c9a84c" : "rgba(201, 168, 76, 0.15)" }}>
                        <div className="relative overflow-hidden" style={{ backgroundColor: "#1c1917" }}>
                            {room.tag && <span className="absolute top-4 left-4 z-10 text-[10px] tracking-[0.3em] uppercase px-3 py-1" style={{ backgroundColor: "#c9a84c", color: "#0f0e0c" }}>{room.tag}</span>}
                            <span className="absolute top-4 right-4 z-10 font-mono text-[10px] tracking-widest px-2 py-1 border" style={{ backgroundColor: "rgba(15, 14, 12, 0.8)", color: "#c9a84c", borderColor: "rgba(201, 168, 76, 0.3)" }}>{room.code}</span>
                            <img src={room.img} alt={room.name} className="w-full h-56 object-cover opacity-80 group-hover:opacity-100 group-hover:scale-105 transition-all duration-700" />
                        </div>
                        <div className="p-8">
                            <div className="flex items-start justify-between mb-3">
                                <div>
                                    <h2 className="text-2xl font-normal" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>{room.name}</h2>
                                    <p className="text-xs tracking-wider mt-1" style={{ color: "#9a9080" }}>{room.size} · Floors {room.floors}</p>
                                </div>
                                <div className="text-right flex-shrink-0">
                                    <p className="text-2xl font-light" style={{ color: "#c9a84c" }}>€{room.price.toLocaleString()}</p>
                                    <p className="text-xs" style={{ color: "#9a9080" }}>per night</p>
                                </div>
                            </div>

                            <div className="mb-4">
                                <GuestIdBadge code={room.code} />
                            </div>

                            <p className="text-sm font-light leading-relaxed mb-6" style={{ color: "rgba(240, 235, 224, 0.55)" }}>{room.desc}</p>
                            <div className="grid grid-cols-2 gap-2 mb-6">
                                {room.features.slice(0, 6).map((f) => (
                                    <div key={f} className="flex items-center gap-2 text-xs" style={{ color: "rgba(240, 235, 224, 0.65)" }}>
                                        <Check size={11} style={{ color: "#c9a84c" }} className="flex-shrink-0" />
                                        {f}
                                    </div>
                                ))}
                            </div>
                            <div className="flex gap-3">
                                <button onClick={() => setSelectedRoom(selectedRoom === room.id ? null : room.id)} className="flex-1 py-3 border text-xs tracking-[0.15em] uppercase transition-colors duration-200" style={{ borderColor: "rgba(201, 168, 76, 0.15)", color: "rgba(240, 235, 224, 0.7)" }}>
                                    {selectedRoom === room.id ? "Hide Details" : "All Features"}
                                </button>
                                <Link to="/booking" state={{ room: room.name }} className="flex-1 py-3 text-xs tracking-[0.15em] uppercase flex items-center justify-center gap-2 transition-colors duration-200" style={{ backgroundColor: "#c9a84c", color: "#0f0e0c" }}>
                                    Book <ArrowRight size={12} />
                                </Link>
                            </div>
                            {selectedRoom === room.id && (
                                <div className="mt-6 pt-6 border-t grid grid-cols-2 gap-2" style={{ borderColor: "rgba(201, 168, 76, 0.15)" }}>
                                    {room.features.map((f) => (
                                        <div key={f} className="flex items-center gap-2 text-xs" style={{ color: "rgba(240, 235, 224, 0.65)" }}>
                                            <Check size={11} style={{ color: "#c9a84c" }} className="flex-shrink-0" />
                                            {f}
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </section>
    );
}