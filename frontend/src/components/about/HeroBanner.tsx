export default function HeroBanner() {
    return (
        <section className="relative h-[60vh] min-h-[500px] flex items-center" style={{ backgroundColor: "#0f0e0c" }}>
            <img src="../../../assets/about/hotelInterior.avif" alt="Maison Grand Hôtel interior" className="absolute inset-0 w-full h-full object-cover opacity-40" />
            <div className="absolute inset-0 bg-gradient-to-r from-[#0f0e0c]/90 via-[#0f0e0c]/50 to-transparent" />
            <div className="relative z-10 px-8 md:px-20 max-w-4xl">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-4 font-light" style={{ color: "#c9a84c" }}>About Maison</p>
                <h1 className="text-5xl md:text-7xl font-normal text-white leading-[1.05] mb-6" style={{ fontFamily: "'Playfair Display', serif" }}>
                    A century of
                    <br />
                    <em style={{ color: "#c9a84c" }}>quiet distinction</em>
                </h1>
                <p className="text-lg text-white/60 max-w-2xl font-light leading-relaxed">Since 1924, Maison Grand Hôtel has stood as a testament to timeless elegance on the Rue de la Paix.</p>
            </div>
        </section>
    )
}