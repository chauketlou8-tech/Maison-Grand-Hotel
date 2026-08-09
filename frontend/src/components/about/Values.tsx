import { Star, Users, Award } from "lucide-react"

export default function Values() {
    return (
        <section className="py-28 px-8 md:px-20" style={{ backgroundColor: "#0f0e0c" }}>
            <div className="max-w-6xl mx-auto">
                <div className="text-center mb-16">
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-4 font-light" style={{ color: "#c9a84c" }}>Our Values</p>
                    <h2 className="text-4xl md:text-5xl font-normal text-white" style={{ fontFamily: "'Playfair Display', serif" }}>What we <em style={{ color: "#c9a84c" }}>believe</em> in</h2>
                </div>
                <div className="grid md:grid-cols-3 gap-8">
                    {[
                        { title: "Attention to Detail", desc: "From the hand-embroidered linen to the single-origin coffee served at breakfast — every detail is considered.", icon: Star },
                        { title: "Personal Service", desc: "We know our guests by name, remember their preferences, and anticipate their needs before they arise.", icon: Users },
                        { title: "Timeless Elegance", desc: "We believe true luxury is felt in the details, not announced in the lobby. Quiet, confident, and enduring.", icon: Award }
                    ].map(({ title, desc, icon: Icon }) => (
                        <div key={title} className="p-8 border" style={{ backgroundColor: "#1a1814", borderColor: "rgba(201, 168, 76, 0.15)" }}>
                            <Icon size={24} style={{ color: "#c9a84c" }} className="mb-4" />
                            <h3 className="text-xl font-normal mb-3" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>{title}</h3>
                            <p className="text-sm font-light leading-relaxed" style={{ color: "#9a9080" }}>{desc}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    )
}