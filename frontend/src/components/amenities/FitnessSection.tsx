import { Dumbbell } from "lucide-react";
import gymEquipment from "../../constants/gymEquipment";

export default function FitnessSection() {
    return (
        <section id="fitness" className="py-24 px-8 md:px-20 border-t border-[#c9a84c]/15 bg-[#0f0e0c]">
            <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-16 items-start">
                <div>
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Movement</p>
                    <h2 className="text-4xl md:text-5xl font-normal mb-6 text-[#f0ebe0] font-display">Fitness Centre</h2>
                    <p className="font-light leading-relaxed mb-8 text-[#f0ebe0]/55">The Maison Fitness Centre is open around the clock. Fully equipped with Technogym's latest generation, a Pilates studio, and a dedicated stretch floor. Personal trainers are available to book in advance or on the day.</p>
                    <div className="grid grid-cols-2 gap-3">
                        {gymEquipment.map((item) => (
                            <div key={item} className="flex items-start gap-2 text-sm text-[#f0ebe0]/65">
                                <span className="text-[#c9a84c] mt-0.5">—</span>
                                {item}
                            </div>
                        ))}
                    </div>
                </div>
                <div className="space-y-4">
                    <div className="relative overflow-hidden bg-[#1c1917]">
                        <img src="https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=700&h=420&fit=crop&auto=format" alt="Hotel gym" className="w-full h-56 object-cover opacity-75" />
                    </div>
                    <div className="p-6 border border-[#c9a84c]/15 bg-[#1a1814]">
                        <Dumbbell size={18} className="text-[#c9a84c] mb-3" />
                        <h4 className="text-lg font-normal mb-2 text-[#f0ebe0] font-display">Open 24 Hours</h4>
                        <p className="text-sm font-light text-[#9a9080]">The gym never closes. Your key card grants access at any hour. Towels and cold water are always provided.</p>
                    </div>
                </div>
            </div>
        </section>
    );
}