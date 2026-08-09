export default function PrivateDiningSection() {
    return (
        <section className="py-24 px-8 md:px-20 border-t border-[#c9a84c]/15 bg-[#0f0e0c]">
            <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-16 items-center">
                <div>
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Exclusive</p>
                    <h2 className="text-4xl md:text-5xl font-normal mb-6 text-[#f0ebe0] font-display">La Salle Dorée</h2>
                    <p className="font-light leading-relaxed mb-4 text-[#f0ebe0]/55">Our private dining room seats up to 24 guests in the original gilded salon of the 1924 building. It is available for business dinners, celebrations, and tastings — with Chef Laurent's team cooking exclusively for your table.</p>
                    <p className="font-light leading-relaxed mb-8 text-[#f0ebe0]/55">Menus are designed collaboratively. Wine pairings are arranged by our sommelier, Pierre Morel, who will walk guests through each selection over the course of the evening.</p>
                    <div className="flex flex-col gap-3 text-sm border border-[#c9a84c]/15 p-6">
                        <div className="flex justify-between">
                            <span className="text-[#9a9080] text-xs tracking-wider">Capacity</span>
                            <span className="text-[#f0ebe0]/75">Up to 24 guests</span>
                        </div>
                        <div className="border-t border-[#c9a84c]/15 pt-3 flex justify-between">
                            <span className="text-[#9a9080] text-xs tracking-wider">Hire fee</span>
                            <span className="text-[#f0ebe0]/75">From €800 + menu</span>
                        </div>
                        <div className="border-t border-[#c9a84c]/15 pt-3 flex justify-between">
                            <span className="text-[#9a9080] text-xs tracking-wider">Notice</span>
                            <span className="text-[#f0ebe0]/75">5 days minimum</span>
                        </div>
                    </div>
                </div>
                <div className="relative bg-[#1c1917]">
                    <img src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=700&h=560&fit=crop&auto=format" alt="La Salle Dorée private dining room" className="w-full h-80 object-cover opacity-75" />
                </div>
            </div>
        </section>
    );
}