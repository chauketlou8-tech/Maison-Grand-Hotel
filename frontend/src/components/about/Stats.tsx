import { Calendar, Users, Award, Star } from "lucide-react";

export default function Stats() {
    return (
        <section className="py-20 px-8 md:px-20" style={{ backgroundColor: "#1a1814", borderTop: "1px solid rgba(201, 168, 76, 0.15)", borderBottom: "1px solid rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-6xl mx-auto grid grid-cols-2 md:grid-cols-4 gap-8">
                {[
                    { value: "1924", label: "Year Founded", icon: Calendar },
                    { value: "47", label: "Rooms & Suites", icon: Users },
                    { value: "2", label: "Michelin Stars", icon: Award },
                    { value: "100+", label: "Years of Excellence", icon: Star }
                ].map(({ value, label, icon: Icon }) => (
                    <div key={label} className="flex flex-col items-center text-center gap-3">
                        <Icon size={18} style={{ color: "#c9a84c" }} />
                        <p className="text-4xl font-light" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>{value}</p>
                        <p className="text-xs tracking-[0.2em] uppercase" style={{ color: "#9a9080" }}>{label}</p>
                    </div>
                ))}
            </div>
        </section>
    )
}