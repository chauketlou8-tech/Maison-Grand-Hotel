import LegalPage from "../../components/legal/LegalPage";

const HERO_IMAGE = "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=1920&h=1080&fit=crop&auto=format";
const CLOSING_IMAGE = "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=1920&h=1080&fit=crop&auto=format";

export default function CookieSettings() {
    return (
        <LegalPage
            eyebrow="Legal"
            title="Cookie Policy"
            updated="2026-10-13"
            heroImage={HERO_IMAGE}
            closingImage={CLOSING_IMAGE}
            intro="We use a small number of cookies to keep this website working and to understand how it is used. This page explains exactly what is set, why, and how you can control it."
            related={[
                { to: "/privacy", label: "Privacy Policy", desc: "What we collect, why, and what you can do about it." },
                { to: "/terms", label: "Terms of Use", desc: "What applies when you book and stay with us." },
            ]}
            sections={[
                {
                    id: "what-are-cookies",
                    title: "What are cookies",
                    body: (
                        <p>Cookies are small text files placed on your device by the websites you visit. They help a site remember you between pages, keep your session secure, and understand how visitors move through it.</p>
                    ),
                },
                {
                    id: "essential",
                    title: "Essential cookies",
                    body: (
                        <>
                            <p>These are required for this website to function and cannot be turned off:</p>
                            <ul>
                                <li>Session cookies — keep you signed in and preserve your booking progress</li>
                                <li>Security cookies — protect against cross-site request forgery</li>
                                <li>Load-balancing cookies — ensure your requests reach a consistent server</li>
                            </ul>
                            <p>These cookies do not track you across other sites and are not shared with any third party.</p>
                        </>
                    ),
                },
                {
                    id: "analytics",
                    title: "Analytics cookies",
                    body: (
                        <>
                            <p>These help us understand which pages are used and how guests move through the site. Data is aggregated and never tied to your identity.</p>
                            <p>We use this to improve the booking flow, fix pages that don't perform, and know which content is read.</p>
                        </>
                    ),
                },
                {
                    id: "marketing",
                    title: "Marketing cookies",
                    body: (
                        <>
                            <p>These are set only with your explicit consent and are used to show relevant offers on other platforms. If you do not consent, no marketing cookies are placed.</p>
                            <p>We do not sell your data, and we do not use marketing cookies for any purpose other than the one described here.</p>
                        </>
                    ),
                },
                {
                    id: "managing",
                    title: "Managing cookies",
                    body: (
                        <>
                            <p>You can control cookies in your browser settings. Every major browser allows you to:</p>
                            <ul>
                                <li>View cookies currently stored</li>
                                <li>Delete individual cookies or all of them</li>
                                <li>Block cookies from specific sites, or all sites</li>
                                <li>Browse in private or incognito mode, which discards cookies when you close the window</li>
                            </ul>
                            <p>Blocking essential cookies will prevent parts of this site — including booking — from working.</p>
                        </>
                    ),
                },
                {
                    id: "more",
                    title: "More information",
                    body: (
                        <p>If you would like more detail about the cookies we set, write to <a href="mailto:stay@maisongrandhotel.co.za">stay@maisongrandhotel.co.za</a>.</p>
                    ),
                },
                {
                    id: "changes",
                    title: "Changes to this policy",
                    body: (
                        <p>If we change which cookies we set, we will update this page and revise the date at the top.</p>
                    ),
                },
            ]}
        />
    );
}