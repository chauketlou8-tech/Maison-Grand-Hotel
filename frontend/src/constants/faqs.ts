import type { Settings } from "../types/settings";

export default function buildFaqs(settings: Settings | null) {
    return [
        {
            q: "When do you open?",
            a: `${settings?.hotel_name ?? "The hotel"} is newly opened. Bookings are open now for stays from opening week onwards.`
        },
        {
            q: "What is the check-in and check-out time?",
            a: `Check-in from ${settings?.check_in_time ?? "—"}; check-out by ${settings?.check_out_time ?? "—"}. Early check-in and late check-out are subject to availability and may be arranged through our concierge.`
        },
        {
            q: "How do I book a room?",
            a: "Book directly through our website for our lowest available rate. You'll receive a confirmation with your Reservation ID immediately."
        },
        {
            q: "How does the Guest ID system work?",
            a: "Each guest receives a unique ID in the format GXX-YY-ZZZ. The floor number, room type code, and a sequential number are encoded directly into the ID — allowing any staff member to identify your location and room type at a glance, without needing to look anything up."
        },
        {
            q: "Do you allow pets?",
            a: settings?.pets_allowed
                ? "We welcome well-behaved dogs under 10 kg in our ground-floor rooms. A pet amenity package is provided. Please notify us in advance."
                : "We do not allow pets at this property."
        },
        {
            q: "What is your cancellation policy?",
            a: `Standard reservations may be cancelled free of charge up to ${settings?.cancellation_window_hours ?? "—"} hours before arrival.`
        }
    ];
}