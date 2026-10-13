import LegalPage from "../../components/legal/LegalPage";

const HERO_IMAGE = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=1920&h=1080&fit=crop&auto=format";
const CLOSING_IMAGE = "https://images.unsplash.com/photo-1618773928121-c32242e63f39?w=1920&h=1080&fit=crop&auto=format";

export default function TermsOfUse() {
    return (
        <LegalPage
            eyebrow="Legal"
            title="Terms of Use"
            updated="2026-10-13"
            heroImage={HERO_IMAGE}
            closingImage={CLOSING_IMAGE}
            intro="These terms apply to this website and to any reservation made through it. You accept them by completing a booking and paying the amount due."
            related={[
                { to: "/privacy", label: "Privacy Policy", desc: "What we collect, why, and what you can do about it." },
                { to: "/cookies", label: "Cookie Settings", desc: "What we set in your browser and how to control it." },
            ]}
            sections={[
                {
                    id: "reservations",
                    title: "Reservations",
                    body: (
                        <>
                            <p>A reservation is confirmed when payment has been received in full and you have received a confirmation email containing a reservation ID.</p>
                            <p>Guests must be 18 or older. Occupancy may not exceed the stated room capacity.</p>
                            <p>Rates are quoted in South African Rand (ZAR) and include VAT where indicated at checkout. Tourism levies and service charges, where applicable, are shown separately.</p>
                            <p>We may decline a reservation or correct a pricing error before confirmation.</p>
                        </>
                    ),
                },
                {
                    id: "payment",
                    title: "Payment",
                    body: (
                        <>
                            <p>Payment is processed by Yoco at the time of booking. We do not hold or store card details.</p>
                            <p>Reservations that remain unpaid after the payment window may be cancelled and the room released.</p>
                            <p>A refundable incidental deposit may be required at check-in and is released after check-out, less any charges for damage or unpaid incidentals.</p>
                        </>
                    ),
                },
                {
                    id: "cancellation",
                    title: "Cancellation and refunds",
                    body: (
                        <>
                            <p>Reservations may be cancelled free of charge within the cancellation window shown at booking. After that window, the first night is non-refundable.</p>
                            <p>Refunds are issued to the original payment method within 10 business days.</p>
                            <p>If we cannot honour a confirmed reservation, we will offer at your choice a comparable room, relocation to an equivalent property, or a full refund.</p>
                        </>
                    ),
                },
                {
                    id: "check-in-out",
                    title: "Check-in and check-out",
                    body: (
                        <>
                            <p>Check-in begins at the time shown on your confirmation. Check-out is by the stated time. Late check-out may incur a charge.</p>
                            <p>Early check-in and late check-out are subject to availability and must be confirmed in advance.</p>
                            <p>A valid form of identification may be requested at check-in.</p>
                        </>
                    ),
                },
                {
                    id: "conduct",
                    title: "Guest conduct",
                    body: (
                        <>
                            <p>Guests are expected to respect other guests, staff, and the property. Smoking is not permitted indoors. Noise should be kept reasonable after 22:00.</p>
                            <p>Pets are not permitted, except for registered assistance animals. Please tell us in advance.</p>
                            <p>You are responsible for any damage caused by you or your guests during your stay, and we may charge the cost of repair or replacement to the payment method on file.</p>
                            <p>We may end a stay without refund where conduct is unsafe, illegal, or disruptive.</p>
                        </>
                    ),
                },
                {
                    id: "liability",
                    title: "Liability",
                    body: (
                        <>
                            <p>We are not liable for loss of or damage to personal belongings except where caused by our negligence. Please use the in-room safe for valuables.</p>
                            <p>We are not liable for interruptions outside our control, including power outages, load shedding, or severe weather, where reasonable alternatives are offered.</p>
                            <p>Nothing in these terms limits any right you have under the Consumer Protection Act 68 of 2008 or any other law that cannot be excluded.</p>
                        </>
                    ),
                },
                {
                    id: "changes",
                    title: "Changes to these terms",
                    body: (
                        <p>We may update these terms from time to time. The version that applies to your booking is the one published on the date you made it.</p>
                    ),
                },
                {
                    id: "governing-law",
                    title: "Governing law",
                    body: (
                        <>
                            <p>These terms are governed by the laws of the Republic of South Africa, and disputes will be handled by the courts of the Western Cape.</p>
                            <p>If something goes wrong, please contact us first at <a href="mailto:stay@maisongrandhotel.co.za">stay@maisongrandhotel.co.za</a> so we have the chance to resolve it before either party escalates.</p>
                        </>
                    ),
                },
            ]}
        />
    );
}