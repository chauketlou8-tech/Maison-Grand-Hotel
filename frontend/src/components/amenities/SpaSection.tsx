// components/amenities/SpaSection.jsx
import spaTreatments from "../../constants/spaTreatment";

export default function SpaSection() {
    return (
        <section id="spa-wellness" className="py-24 px-8 md:px-20 bg-[#0f0e0c]">
            <div className="max-w-6xl mx-auto">
                <div className="grid md:grid-cols-2 gap-16 items-center mb-16">
                    <div>
                        <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Restoration</p>
                        <h2 className="text-4xl md:text-5xl font-normal mb-6 text-[#f0ebe0] font-display">Spa &amp; Wellness</h2>
                        <p className="font-light leading-relaxed mb-4 text-[#f0ebe0]/55">The Maison Spa occupies the entire lower-ground floor — 800 square metres of deliberate quiet. Our team of eight resident therapists trained in Paris, Bali, and Stockholm bring an unusually wide range of traditions to a single address.</p>
                        <p className="font-light leading-relaxed text-[#f0ebe0]/55">Treatments are available from 08:00 to 21:00 daily. Private spa hire for exclusive use is available by arrangement.</p>
                    </div>
                    <div className="relative bg-[#1c1917]">
                        <img src="https://images.unsplash.com/photo-1544161515-4ab6ce6db874?w=700&h=500&fit=crop&auto=format" alt="Spa treatment room" className="w-full h-80 object-cover opacity-85" />
                        <div className="absolute -bottom-4 -right-4 p-5 hidden md:block bg-[#1a1814] border border-[#c9a84c]/15">
                            <p className="text-2xl font-light text-[#c9a84c] font-display">800 m²</p>
                            <p className="text-xs tracking-[0.2em] uppercase text-[#9a9080]">Private Spa Floor</p>
                        </div>
                    </div>
                </div>

                <h3 className="text-2xl font-normal mb-8 text-[#f0ebe0]/80 font-display">Featured Treatments</h3>
                <div className="grid md:grid-cols-2 gap-5">
                    {spaTreatments.map((t) => (
                        <div key={t.name} className="border border-[#c9a84c]/15 p-7 transition-colors duration-300 group hover:border-[#c9a84c]/30">
                            <div className="flex items-start justify-between mb-3">
                                <h4 className="text-lg font-normal text-[#f0ebe0] font-display">{t.name}</h4>
                                <span className="font-light text-sm flex-shrink-0 ml-4 text-[#c9a84c]">{t.price}</span>
                            </div>
                            <p className="text-xs tracking-wider mb-3 text-[#c9a84c]/70">{t.duration}</p>
                            <p className="text-sm font-light leading-relaxed text-[#9a9080]">{t.desc}</p>
                        </div>
                    ))}
                </div>
                <p className="text-xs font-light mt-5 italic text-[#9a9080]">Advance booking required. Full spa menu available at the reception or via concierge.</p>
            </div>
        </section>
    );
}