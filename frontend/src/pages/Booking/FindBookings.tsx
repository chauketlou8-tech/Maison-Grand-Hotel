import { type FormEvent, useState } from "react";
import { AlertCircle, Loader2, Mail } from "lucide-react";
import findBooking from "../../services/findBooking";

export default function FindBooking() {
    const [email, setEmail] = useState("");
    const [error, setError] = useState("");
    const [sent, setSent] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setError("");
        setSent(false);

        if (!email.trim()) {
            setError("Email is required");
            return;
        }

        if (!/\S+@\S+\.\S+/.test(email)) {
            setError("Please enter a valid email address");
            return;
        }

        setIsLoading(true);

        const minLoadingTime = 3500;
        const startTime = Date.now();

        try {
            await findBooking(email);

            const deltaTime = Date.now() - startTime;
            const remainingTime = minLoadingTime - deltaTime;

            setTimeout(() => {
                setSent(true);
                setEmail("");
                setIsLoading(false);
            }, remainingTime > 0 ? remainingTime : 0);
        }

        catch {
            const deltaTime = Date.now() - startTime;
            const remainingTime = minLoadingTime - deltaTime;

            setTimeout(() => {
                setError("Something went wrong. Please try again.");
                setIsLoading(false);
            }, remainingTime > 0 ? remainingTime : 0);
        }
    }

    return (
        <section className="py-16 px-8 md:px-20 bg-[#0f0e0c] min-h-screen flex items-center justify-center">
            <div className="max-w-md w-full">
                <div className="bg-[#1a1814] border border-[#c9a84c]/15 p-10">
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Find Booking</p>
                    <h1 className="text-3xl font-normal mb-4 text-[#f0ebe0] font-display">Lost your confirmation?</h1>
                    <p className="text-sm font-light leading-relaxed mb-8 text-[#9a9080]">
                        Enter the email you used to book. We'll send you links to your reservations.
                    </p>

                    {sent && (
                        <div className="bg-[#4a9d6a]/10 border border-[#4a9d6a] p-4 flex items-center gap-3 mb-6">
                            <Mail size={16} className="text-[#4a9d6a] flex-shrink-0" />
                            <p className="text-[#4a9d6a] text-sm font-light">If that email is on file, we've sent your booking links.</p>
                        </div>
                    )}

                    {error && (
                        <div className="bg-red-500/10 border border-red-500 p-4 flex items-center gap-3 mb-6">
                            <AlertCircle size={16} className="text-red-500 flex-shrink-0" />
                            <p className="text-red-500 text-sm font-light">{error}</p>
                        </div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <label className="block text-[10px] tracking-[0.3em] uppercase mb-2 text-[#9a9080]">Email</label>
                        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="jean@example.com" className={`w-full bg-[#0f0e0c] border px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors mb-6 ${error ? "border-red-500" : "border-[#c9a84c]/15"}`} />

                        <button type="submit" disabled={isLoading} className="w-full py-4 bg-[#c9a84c] text-[#0f0e0c] text-sm tracking-[0.2em] uppercase font-medium hover:bg-[#c9a84c]/90 transition-colors cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2">
                            {isLoading ? (
                                <div className="flex items-center gap-2">
                                    <Loader2 size={16} className="animate-spin" />Sending...
                                </div>
                            ) : "Send Booking Links"}
                        </button>
                    </form>
                </div>
            </div>
        </section>
    );
}