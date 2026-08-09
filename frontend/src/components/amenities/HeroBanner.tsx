export default function HeroBanner() {
    return (
        <section className="relative h-[55vh] min-h-[420px] flex items-end pb-16 pt-20 bg-[#0f0e0c]">
            <img src="https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=1600&h=900&fit=crop&auto=format" alt="Hotel infinity pool at night" className="absolute inset-0 w-full h-full object-cover opacity-40" />
            <div className="absolute inset-0 bg-gradient-to-t from-[#0f0e0c] via-[#0f0e0c]/50 to-[#0f0e0c]/20" />
            <div className="relative z-10 px-8 md:px-20 max-w-6xl mx-auto w-full">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">At Your Disposal</p>
                <h1 className="text-5xl md:text-6xl font-normal text-[#f0ebe0] font-display">Hotel Amenities</h1>
                <p className="font-light mt-4 max-w-lg text-[#f0ebe0]/50">From the pool at dawn to the late-night bar, every facility is designed to feel like your own.</p>
            </div>
        </section>
    );
}