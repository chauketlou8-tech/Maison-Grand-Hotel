export default function GuestIdBadge({ code, floor = "XX" }: { code: string; floor?: string }) {
    return (
        <span className="inline-flex items-center gap-1.5 text-[10px] tracking-[0.25em] font-mono px-2 py-1" style={{ color: "rgba(201, 168, 76, 0.7)", border: "1px solid rgba(201, 168, 76, 0.2)" }} title="Guest ID format: G[floor]-[type]-[sequence]">
            G{floor}-<span style={{ color: "#c9a84c" }}>{code}</span>-###
        </span>
);
}