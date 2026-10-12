import type { Settings } from "../types/settings";

export default function buildDirectBenefits(settings: Settings | null) {
    return [
        "Best available rate — guaranteed",
        "Complimentary early check-in (subject to availability)",
        "Welcome amenity in-room on arrival",
        `Flexible cancellation (${settings?.cancellation_window_hours ?? "—"} hours)`,
        "Priority access to restaurant"
    ];
}