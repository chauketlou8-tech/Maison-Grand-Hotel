import compareFeatures from "../../constants/compareFeatures";
import compareRooms from "../../constants/compareRooms.json";

export default function ComparisonTable() {
    return (
        <section className="py-20 px-8 md:px-20 border-t" style={{ backgroundColor: "#252218", borderColor: "rgba(201, 168, 76, 0.15)" }}>
            <div className="max-w-6xl mx-auto">
                <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light" style={{ color: "#c9a84c" }}>At a Glance</p>
                <h2 className="text-4xl font-normal mb-12" style={{ fontFamily: "'Playfair Display', serif", color: "#f0ebe0" }}>Room Comparison</h2>
                <div className="overflow-x-auto">
                    <table className="w-full text-sm min-w-[640px]">
                        <thead>
                        <tr className="border-b" style={{ borderColor: "rgba(201, 168, 76, 0.15)" }}>
                            <th className="text-left py-4 pr-6 text-xs tracking-[0.2em] uppercase font-normal w-28" style={{ color: "#9a9080" }}>Feature</th>
                            {compareRooms.map((r) => (
                                <th key={r.name} className="text-left py-4 px-3 font-normal whitespace-nowrap" style={{ color: "rgba(240, 235, 224, 0.8)" }}>
                                    <span className="text-xs tracking-wider block">{r.name}</span>
                                    <code className="text-[10px] font-mono" style={{ color: "rgba(201, 168, 76, 0.6)" }}>{r.code}</code>
                                </th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {compareFeatures.map((feat, fi) => (
                            <tr key={feat} className="border-b" style={{ borderColor: "rgba(201, 168, 76, 0.08)" }}>
                                <td className="py-4 pr-6 text-xs tracking-[0.1em] w-28" style={{ color: "#9a9080" }}>{feat}</td>
                                {compareRooms.map((r) => (
                                    <td key={r.name} className="py-4 px-3 text-xs" style={{ color: r.values[fi] === "✓" || r.values[fi] === "360°" ? "#c9a84c" : r.values[fi] === "—" ? "rgba(154, 144, 128, 0.3)" : "rgba(240, 235, 224, 0.7)" }}>
                                        {r.values[fi]}
                                    </td>
                                ))}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    );
}