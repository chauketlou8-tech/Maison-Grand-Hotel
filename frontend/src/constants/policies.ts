import { Clock, Shield, Gift } from "lucide-react";
import type { Settings } from "../types/settings";

export default function buildPolicies(settings: Settings | null) {
    return [
        {
            icon: Clock,
            title: "Check-in / Check-out",
            body: `Check-in from ${settings?.check_in_time ?? "—"}; check-out by ${settings?.check_out_time ?? "—"}. Early and late options available via concierge.`
        },
        {
            icon: Shield,
            title: "Cancellation",
            body: `Free cancellation up to ${settings?.cancellation_window_hours ?? "—"} hours before arrival.`
        },
        {
            icon: Gift,
            title: "Special Occasions",
            body: "Birthdays, anniversaries, proposals — let us know and our team will prepare something memorable."
        }
    ];
}