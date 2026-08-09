// components/booking/BookingForm.tsx
import { ChevronDown, Check, Phone, Mail, MapPin } from "lucide-react";
import rooms from "../../constants/rooms";
import packages from "../../constants/packages";

export default function BookingForm({
                                        selectedRoom,
                                        setSelectedRoom,
                                        checkIn,
                                        setCheckIn,
                                        checkOut,
                                        setCheckOut,
                                        guests,
                                        setGuests,
                                        selectedPackages,
                                        firstName,
                                        setFirstName,
                                        lastName,
                                        setLastName,
                                        email,
                                        setEmail,
                                        phone,
                                        setPhone,
                                        requests,
                                        setRequests,
                                        handleSubmit,
                                        togglePackage,
                                        room,
                                        nights,
                                        roomTotal,
                                        total
                                    }) {
    return (
        <section className="py-16 px-8 md:px-20 bg-[#0f0e0c]">
            <div className="max-w-6xl mx-auto">
                <form onSubmit={handleSubmit} className="grid lg:grid-cols-3 gap-10">
                    <div className="lg:col-span-2 flex flex-col gap-8">
                        <div>
                            <h2 className="text-2xl font-normal mb-5 text-[#f0ebe0] font-display">1. Choose Your Room</h2>
                            <div className="grid sm:grid-cols-2 gap-3">
                                {rooms.map((r) => (
                                    <button key={r.name} type="button" onClick={() => setSelectedRoom(r.name)} className={`text-left p-5 border transition-all duration-200 ${selectedRoom === r.name ? "border-[#c9a84c] bg-[#c9a84c]/5" : "border-[#c9a84c]/15 hover:border-[#c9a84c]/30"}`}>
                                        <div className="flex justify-between items-start gap-2">
                                            <div className="flex-1">
                                                <div className="flex items-center gap-2 mb-0.5">
                                                    <code className="text-[10px] font-mono text-[#c9a84c]/70 border border-[#c9a84c]/25 px-1.5 py-0.5">{r.code}</code>
                                                    <p className="text-sm text-[#f0ebe0]/85 leading-tight">{r.name}</p>
                                                </div>
                                                <p className="text-xs text-[#9a9080]">{r.size} · Floors {r.floors}</p>
                                            </div>
                                            <div className="text-right flex-shrink-0">
                                                <p className="text-[#c9a84c] font-light">€{r.price}</p>
                                                <p className="text-xs text-[#9a9080]">/night</p>
                                            </div>
                                        </div>
                                        {selectedRoom === r.name && <div className="mt-3 flex items-center gap-1.5 text-xs text-[#c9a84c]"><Check size={11} /> Selected</div>}
                                    </button>
                                ))}
                            </div>
                        </div>

                        <div>
                            <h2 className="text-2xl font-normal mb-5 text-[#f0ebe0] font-display">2. Dates &amp; Guests</h2>
                            <div className="grid sm:grid-cols-3 gap-4">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Check-in</label>
                                    <input type="date" value={checkIn} min={new Date().toISOString().split("T")[0]} onChange={(e) => setCheckIn(e.target.value)} className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Check-out</label>
                                    <input type="date" value={checkOut} min={checkIn || new Date().toISOString().split("T")[0]} onChange={(e) => setCheckOut(e.target.value)} className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Guests</label>
                                    <div className="relative">
                                        <select value={guests} onChange={(e) => setGuests(e.target.value)} className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] focus:outline-none focus:border-[#c9a84c] transition-colors appearance-none">
                                            {["1", "2", "3", "4"].map((n) => (<option key={n}>{n}</option>))}
                                        </select>
                                        <ChevronDown size={14} className="absolute right-3 top-1/2 -translate-y-1/2 text-[#9a9080] pointer-events-none" />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div>
                            <h2 className="text-2xl font-normal mb-5 text-[#f0ebe0] font-display">3. Add-ons</h2>
                            <div className="flex flex-col gap-3">
                                {packages.map((pkg) => (
                                    <button key={pkg.id} type="button" onClick={() => togglePackage(pkg.id)} className={`text-left p-5 border transition-all duration-200 ${selectedPackages.includes(pkg.id) ? "border-[#c9a84c] bg-[#c9a84c]/5" : "border-[#c9a84c]/15 hover:border-[#c9a84c]/30"}`}>
                                        <div className="flex items-start justify-between gap-4">
                                            <div className="flex-1">
                                                <div className="flex items-center gap-3 mb-2">
                                                    <p className="text-sm text-[#f0ebe0]/85">{pkg.label}</p>
                                                    {selectedPackages.includes(pkg.id) && <Check size={12} className="text-[#c9a84c]" />}
                                                </div>
                                                <div className="flex flex-col gap-1">
                                                    {pkg.includes.map((inc) => (<p key={inc} className="text-xs text-[#9a9080] font-light">· {inc}</p>))}
                                                </div>
                                            </div>
                                            <span className="text-[#c9a84c] font-light text-sm flex-shrink-0">+€{pkg.price}<span className="text-xs text-[#9a9080]">/night</span></span>
                                        </div>
                                    </button>
                                ))}
                            </div>
                        </div>

                        <div>
                            <h2 className="text-2xl font-normal mb-5 text-[#f0ebe0] font-display">4. Your Details</h2>
                            <div className="grid sm:grid-cols-2 gap-4 mb-4">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">First Name</label>
                                    <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} required placeholder="Jean" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Last Name</label>
                                    <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} required placeholder="Dupont" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                            </div>
                            <div className="grid sm:grid-cols-2 gap-4 mb-4">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Email</label>
                                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required placeholder="jean@example.com" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Phone</label>
                                    <input type="tel" value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="+33 6 00 00 00 00" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                            </div>
                            <div>
                                <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Special Requests</label>
                                <textarea rows={3} value={requests} onChange={(e) => setRequests(e.target.value)} placeholder="Dietary requirements, occasion arrangements, floor preference..." className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors resize-none" />
                            </div>
                        </div>
                    </div>

                    <div className="lg:col-span-1">
                        <div className="sticky top-28 bg-[#1a1814] border border-[#c9a84c]/15 p-7">
                            <p className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-5">Reservation Summary</p>
                            <div className="flex flex-col gap-3 text-sm mb-6 pb-6 border-b border-[#c9a84c]/15">
                                <div className="flex justify-between items-start gap-3">
                                    <span className="text-[#9a9080] text-xs">Room</span>
                                    <span className="text-[#f0ebe0]/80 text-right leading-tight">{selectedRoom}</span>
                                </div>
                                {checkIn && <div className="flex justify-between"><span className="text-[#9a9080] text-xs">Check-in</span><span className="text-[#f0ebe0]/80">{checkIn}</span></div>}
                                {checkOut && <div className="flex justify-between"><span className="text-[#9a9080] text-xs">Check-out</span><span className="text-[#f0ebe0]/80">{checkOut}</span></div>}
                                {nights > 0 && <div className="flex justify-between"><span className="text-[#9a9080] text-xs">Nights</span><span className="text-[#f0ebe0]/80">{nights}</span></div>}
                                <div className="flex justify-between"><span className="text-[#9a9080] text-xs">Guests</span><span className="text-[#f0ebe0]/80">{guests}</span></div>
                            </div>

                            <div className="flex flex-col gap-2 text-sm mb-6 pb-6 border-b border-[#c9a84c]/15">
                                <div className="flex justify-between">
                                    <span className="text-[#9a9080] text-xs">{room.name.split(" ")[0]} ({nights > 0 ? `${nights}n` : "per night"})</span>
                                    <span className="text-[#f0ebe0]/75">€{nights > 0 ? roomTotal.toLocaleString() : room.price}</span>
                                </div>
                                {packages.filter((p) => selectedPackages.includes(p.id)).map((p) => (
                                    <div key={p.id} className="flex justify-between">
                                        <span className="text-[#9a9080] text-xs">{p.label}</span>
                                        <span className="text-[#f0ebe0]/75">€{nights > 0 ? (p.price * nights).toLocaleString() : p.price}</span>
                                    </div>
                                ))}
                            </div>

                            <div className="flex justify-between items-baseline mb-6">
                                <span className="text-xs tracking-[0.15em] uppercase text-[#9a9080]">{nights > 0 ? "Total Est." : "From"}</span>
                                <span className="text-2xl text-[#c9a84c] font-light font-display">€{nights > 0 ? total.toLocaleString() : room.price}</span>
                            </div>

                            <button type="submit" className="w-full py-4 bg-[#c9a84c] text-[#0f0e0c] text-sm tracking-[0.2em] uppercase font-medium hover:bg-[#c9a84c]/90 transition-colors mb-3">Request Reservation</button>
                            <p className="text-xs text-[#9a9080] text-center font-light">No payment now. We'll confirm within 2 hours.</p>

                            <div className="mt-6 pt-6 border-t border-[#c9a84c]/15 flex flex-col gap-3">
                                <p className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080]">Prefer to call?</p>
                                <a href="tel:+33142000000" className="flex items-center gap-2 text-xs text-[#f0ebe0]/60 hover:text-[#c9a84c] transition-colors">
                                    <Phone size={11} className="text-[#c9a84c]" /> +33 1 42 00 00 00
                                </a>
                                <a href="mailto:reservations@maisongrandhotel.com" className="flex items-center gap-2 text-xs text-[#f0ebe0]/60 hover:text-[#c9a84c] transition-colors">
                                    <Mail size={11} className="text-[#c9a84c]" /> reservations@maison...
                                </a>
                                <span className="flex items-start gap-2 text-xs text-[#f0ebe0]/60">
                                    <MapPin size={11} className="text-[#c9a84c] flex-shrink-0 mt-0.5" /> 12 Rue de la Paix, 75002 Paris
                                </span>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    );
}