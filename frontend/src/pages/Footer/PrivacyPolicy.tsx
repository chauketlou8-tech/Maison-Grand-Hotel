import LegalPage from "../../components/legal/LegalPage";

const HERO_IMAGE = "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=1920&h=1080&fit=crop&auto=format";
const CLOSING_IMAGE = "https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=1920&h=1080&fit=crop&auto=format";

export default function PrivacyPolicy() {
    return (
        <LegalPage
            eyebrow="Legal"
            title="Privacy Policy"
            updated="2026-10-13"
            heroImage={HERO_IMAGE}
            closingImage={CLOSING_IMAGE}
            intro="We collect the minimum we need to hold your reservation, welcome you to the hotel, and satisfy South African hospitality and tax requirements. This document explains exactly what we hold, why we are allowed to hold it, and what you can do about it."
            related={[
                { to: "/terms", label: "Terms of Use", desc: "What applies when you book and stay with us." },
                { to: "/cookies", label: "Cookie Settings", desc: "What we set in your browser and how to control it." },
            ]}
            sections={[
                {
                    id: "who-we-are",
                    title: "Who we are",
                    body: (
                        <>
                            <p>This policy is issued by <strong>Maison Grand Hôtel</strong>, the responsible party for the personal information described below.</p>
                            <p>Our Information Officer is the person responsible for how we handle personal information and for responding to any questions or requests you may have. You can reach the Information Officer at <a href="mailto:privacy@maisongrandhotel.co.za">privacy@maisongrandhotel.co.za</a>.</p>
                        </>
                    ),
                },
                {
                    id: "what-we-collect",
                    title: "What we collect",
                    body: (
                        <>
                            <p>When you make a reservation, we collect:</p>
                            <ul>
                                <li>Your name, email address, and phone number</li>
                                <li>Your postal address, nationality, and identity number</li>
                                <li>Your date of birth, where required for identity verification</li>
                                <li>Special requests you provide (dietary needs, accessibility, occasion details)</li>
                            </ul>
                            <p>When you pay, our payment provider Yoco collects your card details directly. We never see or store card numbers.</p>
                            <p>If you are travelling with a child, we may hold the child's name and date of birth as part of the reservation. We do not collect personal information directly from children, and we ask that a parent or guardian provides it on their behalf.</p>
                            <p>You are not obliged to give us this information, but if you do not, we cannot confirm your booking or check you in — South African hospitality and identity law requires us to record it.</p>
                        </>
                    ),
                },
                {
                    id: "how-we-use-it",
                    title: "How we use it",
                    body: (
                        <>
                            <p>Your information is used to:</p>
                            <ul>
                                <li>Hold and confirm your reservation</li>
                                <li>Process payment and issue receipts</li>
                                <li>Send pre-arrival information and post-stay follow-up</li>
                                <li>Comply with South African hospitality, identity, and tax laws</li>
                            </ul>
                            <p>We process this information because it is necessary to perform the booking you have asked us to make, and because we are required to keep certain records by law. Where we send you marketing communication, we do so only with your consent, and you can withdraw that consent at any time.</p>
                            <p>We do not use your data for advertising or sell it to third parties.</p>
                            <p>We do not carry out automated decision-making or profiling that has a legal or significant effect on you.</p>
                        </>
                    ),
                },
                {
                    id: "who-we-share-it-with",
                    title: "Who we share it with",
                    body: (
                        <>
                            <p>Only where necessary to run the hotel:</p>
                            <ul>
                                <li>Yoco — to process payments (South Africa)</li>
                                <li>Our email service provider — to send confirmations and reminders</li>
                                <li>Our property management system — to manage your stay</li>
                            </ul>
                            <p>These providers act on our instructions and are bound by confidentiality and data protection obligations. Nobody else receives your information.</p>
                            <p>Some of our service providers may store data on servers outside South Africa. Where that happens, we take reasonable steps to ensure your information receives an equivalent level of protection, as required by POPIA.</p>
                        </>
                    ),
                },
                {
                    id: "how-long-we-keep-it",
                    title: "How long we keep it",
                    body: (
                        <>
                            <p>Reservation and payment records are retained for <strong>seven years</strong> from the date of your stay, as required by South African tax law.</p>
                            <p>Marketing preferences are retained until you withdraw consent. Identity documents collected at check-in are destroyed after the legally required period.</p>
                            <p>Once a retention period ends, we delete or anonymise the information.</p>
                        </>
                    ),
                },
                {
                    id: "your-rights",
                    title: "Your rights",
                    body: (
                        <>
                            <p>Under the Protection of Personal Information Act (POPIA), you may:</p>
                            <ul>
                                <li>Request a copy of the personal information we hold about you</li>
                                <li>Ask us to correct anything that is inaccurate</li>
                                <li>Ask us to delete information we are not legally required to keep</li>
                                <li>Object to processing for marketing purposes</li>
                                <li>Withdraw consent you have previously given</li>
                            </ul>
                            <p>Write to <a href="mailto:privacy@maisongrandhotel.co.za">privacy@maisongrandhotel.co.za</a> and we will respond within 30 days. If you are unhappy with how we have handled your information, you may also lodge a complaint with the Information Regulator of South Africa at <a href="mailto:complaints.IR@justice.gov.za">complaints.IR@justice.gov.za</a> or via <a href="https://inforegulator.org.za" target="_blank" rel="noreferrer">inforegulator.org.za</a>.</p>
                        </>
                    ),
                },
                {
                    id: "security",
                    title: "Security",
                    body: (
                        <>
                            <p>Reservation data is stored on encrypted servers, accessible only to staff who need it to run the hotel. Payment data is handled entirely by Yoco and never touches our systems.</p>
                            <p>If a security compromise affects your personal information, we will notify you and the Information Regulator as soon as reasonably possible, in line with POPIA.</p>
                        </>
                    ),
                },
                {
                    id: "changes",
                    title: "Changes to this policy",
                    body: (
                        <p>If we change how we handle your information, we will update this page and revise the date at the top. Material changes will be communicated by email to guests with active reservations.</p>
                    ),
                },
            ]}
        />
    );
}