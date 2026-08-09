import { useState } from "react";
import { Info } from "lucide-react";

export default function GuestIdGuide() {
    const [showIdGuide, setShowIdGuide] = useState(false);

    return (
        <div className="border-b px-8 md:px-20" style={{ borderColor: "rgba(201, 168, 76, 0.15)", backgroundColor: "#1a1814" }}>
            <div className="max-w-6xl mx-auto py-4 flex items-center justify-between gap-4">
                <div className="flex items-center gap-3 flex-wrap">
                    <Info size={13} style={{ color: "#c9a84c" }} className="flex-shrink-0" />
                    <span className="text-xs font-light" style={{ color: "#9a9080" }}>Every guest receives a unique ID:</span>
                    <code className="text-xs font-mono px-2 py-0.5 tracking-wider" style={{ color: "rgba(240, 235, 224, 0.8)", backgroundColor: "#252218" }}>
                        G<span style={{ color: "#c9a84c" }}>05</span>-<span style={{ color: "#c9a84c" }}>KI</span>-<span style={{ color: "#c9a84c" }}>012</span>
                    </code>
                    <span className="text-xs font-light hidden sm:inline" style={{ color: "#9a9080" }}>Floor · Room type · Guest sequence</span>
                </div>
                <button onClick={() => setShowIdGuide(!showIdGuide)} className="text-xs tracking-wider flex-shrink-0 hover:underline underline-offset-4" style={{ color: "#c9a84c" }}>
                    {showIdGuide ? "Hide guide" : "Learn more"}
                </button>
            </div>

            {showIdGuide && (
                <div className="max-w-6xl mx-auto pb-6">
                    <div className="border p-6 grid sm:grid-cols-3 gap-6" style={{ borderColor: "rgba(201, 168, 76, 0.15)", backgroundColor: "rgba(15, 14, 12, 0.5)" }}>
                        <div>
                            <p className="text-[10px] tracking-[0.35em] uppercase mb-3" style={{ color: "#c9a84c" }}>ID Format</p>
                            <p className="font-mono text-lg tracking-widest mb-2" style={{ color: "#f0ebe0" }}>G<span style={{ color: "#c9a84c" }}>XX</span>-<span style={{ color: "#c9a84c" }}>YY</span>-<span style={{ color: "#c9a84c" }}>ZZZ</span></p>
                            <p className="text-xs font-light leading-relaxed" style={{ color: "#9a9080" }}>Encoded so any staff member can read your floor, room type, and sequence number at a glance — no lookup needed.</p>
                        </div>
                        <div>
                            <p className="text-[10px] tracking-[0.35em] uppercase mb-3" style={{ color: "#c9a84c" }}>Room Type Codes</p>
                            <div className="grid grid-cols-2 gap-x-4 gap-y-1.5">
                                {[["SI","Single"],["ST","Standard"],["DO","Double"],["QU","Queen"],["KI","King"],["DE","Deluxe"],["EX","Executive"],["FA","Family"],["SU","Suite"],["PE","Penthouse"]].map(([code, label]) => (
                                    <div key={code} className="flex items-center gap-2 text-xs">
                                        <code className="font-mono" style={{ color: "#c9a84c" }}>{code}</code>
                                        <span style={{ color: "#9a9080" }}>{label}</span>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <div>
                            <p className="text-[10px] tracking-[0.35em] uppercase mb-3" style={{ color: "#c9a84c" }}>Examples</p>
                            <div className="flex flex-col gap-2">
                                {[
                                    ["G03-ST-001", "Floor 3, Standard, 1st guest"],
                                    ["G05-KI-012", "Floor 5, King, 12th guest"],
                                    ["G15-PE-001", "Floor 15, Penthouse, 1st guest"],
                                ].map(([id, desc]) => (
                                    <div key={id} className="text-xs">
                                        <code className="font-mono" style={{ color: "rgba(240, 235, 224, 0.8)" }}>{id}</code>
                                        <p className="mt-0.5" style={{ color: "#9a9080" }}>{desc}</p>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}