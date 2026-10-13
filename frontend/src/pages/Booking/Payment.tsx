import { useState, type FormEvent } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { CreditCard, Shield, ArrowRight, Lock, AlertCircle } from "lucide-react";
import isValidCardNumber from "../../utils/validateCard";

export default function Payment() {
    const location = useLocation();
    const navigate = useNavigate();
    const booking = location.state || {};

    const [cardNumber, setCardNumber] = useState("");
    const [expiry, setExpiry] = useState("");
    const [cvc, setCvc] = useState("");
    const [name, setName] = useState("");

    const [isProcessing, setIsProcessing] = useState(false);

    const [errors, setErrors] = useState<{ [key: string]: string }>({});

    const total = booking.total || 0;
    const nights = booking.nights || 1;
    const roomName = booking.selectedRoom || "Room";
    const checkIn = booking.checkIn ? new Date(booking.checkIn).toLocaleDateString() : "";
    const checkOut = booking.checkOut ? new Date(booking.checkOut).toLocaleDateString() : "";
    const guests = booking.guests || 1;
    const firstName = booking.firstName || "";
    const lastName = booking.lastName || "";
    const email = booking.email || "";

    function formatCardNumber(value: string) {
        const cleaned = value.replace(/\D/g, "");
        const groups = cleaned.match(/.{1,4}/g);
        return groups ? groups.join(" ") : cleaned;
    }

    function handleCardChange(e: React.ChangeEvent<HTMLInputElement>) {
        const value = e.target.value.replace(/\s/g, "");
        if (value.length <= 16) {
            setCardNumber(formatCardNumber(value));
        }
        if (errors.cardNumber) {
            setErrors((prev) => ({ ...prev, cardNumber: "" }));
        }
    }

    function handleExpiryChange(e: React.ChangeEvent<HTMLInputElement>) {
        const value = e.target.value.replace(/\D/g, "");
        if (value.length <= 4) {
            if (value.length >= 3) {
                setExpiry(value.slice(0, 2) + "/" + value.slice(2));
            } else {
                setExpiry(value);
            }
        }
        if (errors.expiry) {
            setErrors((prev) => ({ ...prev, expiry: "" }));
        }
    }

    function handleCvcChange(e: React.ChangeEvent<HTMLInputElement>) {
        const value = e.target.value.replace(/\D/g, "");
        if (value.length <= 4) {
            setCvc(value);
        }
        if (errors.cvc) {
            setErrors((prev) => ({ ...prev, cvc: "" }));
        }
    }

    function handleNameChange(e: React.ChangeEvent<HTMLInputElement>) {
        setName(e.target.value);
        if (errors.name) {
            setErrors((prev) => ({ ...prev, name: "" }));
        }
    }

    function validateForm() {
        const newErrors: { [key: string]: string } = {};
        const cardClean = cardNumber.replace(/\s/g, "");

        // Card number
        if (cardClean.length < 16) {
            newErrors.cardNumber = "Please enter a valid card number";
        }

        else if (!isValidCardNumber(cardNumber)) {
            newErrors.cardNumber = "Invalid card number";
        }

        // Expiry
        if (expiry.length < 5) {
            newErrors.expiry = "Please enter a valid expiry date";
        }

        else {
            const [month, year] = expiry.split("/");
            const currentYear = new Date().getFullYear() % 100;
            const currentMonth = new Date().getMonth() + 1;

            if (parseInt(month) < 1 || parseInt(month) > 12) {
                newErrors.expiry = "Invalid expiry month";
            }

            else if (parseInt(year) < currentYear || (parseInt(year) === currentYear && parseInt(month) < currentMonth)) {
                newErrors.expiry = "Card has expired";
            }
        }

        // CVC
        if (cvc.length < 3) {
            newErrors.cvc = "Please enter a valid CVC";
        }

        else if (!/^\d+$/.test(cvc)) {
            newErrors.cvc = "CVC must be numbers only";
        }

        // Name
        if (!name.trim()) {
            newErrors.name = "Please enter the name on the card";
        }

        else if (name.trim().length < 2) {
            newErrors.name = "Please enter a valid name";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    }

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        if (!validateForm()) return;

        setIsProcessing(true);

        const minLoadingTime = 3500;
        const startTime = Date.now();

        try {

            // const paymentDetails = {
            //     cardNumber,
            //     expiry,
            //     cvc,
            //     name
            // }

            //await pay(paymentDetails);

            const deltaTime = Date.now() - startTime;
            const remainingTime = minLoadingTime - deltaTime;

            setTimeout(() => {
                setIsProcessing(false);
                navigate("/booking/success", {
                    state: {
                        firstName,
                        email,
                        selectedRoom: roomName,
                        checkIn: booking.checkIn,
                        checkOut: booking.checkOut,
                        guests,
                        total,
                        nights,
                        paid: true,
                    }
                });
            }, remainingTime > 0 ? remainingTime : 0);
        } catch (error: any) {
            setIsProcessing(false);
            setErrors((prev) => ({ ...prev, submit: error.response.data.message || "Something went wrong. Please try again." }));
        }
    }

    return (
        <section className="py-20 px-8 md:px-20 bg-[#0f0e0c] min-h-screen">
            <div className="max-w-4xl mx-auto">
                <div className="flex items-center gap-4 mb-8">
                    <div className="w-8 h-8 border border-[#c9a84c]/30 flex items-center justify-center rounded-full">
                        <span className="text-xs text-[#c9a84c] font-light">2</span>
                    </div>
                    <h1 className="text-3xl md:text-4xl font-normal text-[#f0ebe0] font-display">Payment</h1>
                </div>

                <div className="grid md:grid-cols-5 gap-8">
                    <div className="md:col-span-3">
                        <div className="bg-[#1a1814] border border-[#c9a84c]/15 p-8">
                            <div className="flex items-center gap-2 mb-6">
                                <Lock size={14} className="text-[#c9a84c]" />
                                <p className="text-xs text-[#9a9080] tracking-[0.15em] uppercase font-light">Secure Payment</p>
                            </div>

                            {errors.submit && (
                                <div className="bg-red-500/10 border border-red-500 p-4 flex items-center gap-3 mb-6">
                                    <AlertCircle size={16} className="text-red-500 flex-shrink-0" />
                                    <p className="text-red-500 text-sm font-light">{errors.submit}</p>
                                </div>
                            )}

                            <form onSubmit={handleSubmit} className="space-y-5">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Card Number</label>
                                    <div className="relative">
                                        <input type="text" value={cardNumber} onChange={handleCardChange} placeholder="4242 4242 4242 4242" maxLength={19} className={`w-full bg-[#0f0e0c] border px-4 py-3.5 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/30 focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.cardNumber ? "border-red-500" : "border-[#c9a84c]/15"}`} />
                                        <CreditCard size={16} className="absolute right-4 top-1/2 -translate-y-1/2 text-[#9a9080]/50" />
                                    </div>
                                    {errors.cardNumber && <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.cardNumber}</p>}
                                </div>

                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Expiry</label>
                                        <input type="text" value={expiry} onChange={handleExpiryChange} placeholder="MM/YY" maxLength={5} className={`w-full bg-[#0f0e0c] border px-4 py-3.5 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/30 focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.expiry ? "border-red-500" : "border-[#c9a84c]/15"}`} />
                                        {errors.expiry && <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.expiry}</p>}
                                    </div>
                                    <div>
                                        <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">CVC</label>
                                        <input type="text" value={cvc} onChange={handleCvcChange} placeholder="123" maxLength={4} className={`w-full bg-[#0f0e0c] border px-4 py-3.5 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/30 focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.cvc ? "border-red-500" : "border-[#c9a84c]/15"}`} />
                                        {errors.cvc && <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.cvc}</p>}
                                    </div>
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Name on Card</label>
                                    <input type="text" value={name} onChange={handleNameChange} placeholder="John Doe" className={`w-full bg-[#0f0e0c] border px-4 py-3.5 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/30 focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.name ? "border-red-500" : "border-[#c9a84c]/15"}`} />
                                    {errors.name && <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.name}</p>}
                                </div>

                                <button type="submit" disabled={isProcessing} className="w-full py-4 bg-[#c9a84c] text-[#0f0e0c] text-sm tracking-[0.2em] uppercase font-medium hover:bg-[#c9a84c]/90 transition-colors flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed">
                                    {isProcessing ? <>Processing...</> : <>Pay €{total.toFixed(0)} <ArrowRight size={14} /></>}
                                </button>

                                <div className="flex items-center gap-3 pt-2">
                                    <Shield size={12} className="text-[#c9a84c]" />
                                    <p className="text-[10px] text-[#9a9080] font-light tracking-wider">Secured with 256-bit encryption</p>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div className="md:col-span-2">
                        <div className="bg-[#1a1814] border border-[#c9a84c]/15 p-8 sticky top-28">
                            <p className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-5">Booking Summary</p>

                            <div className="flex flex-col gap-3 text-sm mb-6 pb-6 border-b border-[#c9a84c]/15">
                                <div className="flex justify-between">
                                    <span className="text-[#9a9080] text-xs">Room</span>
                                    <span className="text-[#f0ebe0]/80">{roomName}</span>
                                </div>
                                <div className="flex justify-between">
                                    <span className="text-[#9a9080] text-xs">Check-in</span>
                                    <span className="text-[#f0ebe0]/80">{checkIn}</span>
                                </div>
                                <div className="flex justify-between">
                                    <span className="text-[#9a9080] text-xs">Check-out</span>
                                    <span className="text-[#f0ebe0]/80">{checkOut}</span>
                                </div>
                                <div className="flex justify-between">
                                    <span className="text-[#9a9080] text-xs">Nights</span>
                                    <span className="text-[#f0ebe0]/80">{nights}</span>
                                </div>
                                <div className="flex justify-between">
                                    <span className="text-[#9a9080] text-xs">Guests</span>
                                    <span className="text-[#f0ebe0]/80">{guests}</span>
                                </div>
                            </div>

                            <div className="flex justify-between items-baseline">
                                <span className="text-xs tracking-[0.15em] uppercase text-[#9a9080]">Total</span>
                                <span className="text-3xl text-[#c9a84c] font-light font-display">€{total.toFixed(0)}</span>
                            </div>

                            <div className="mt-6 pt-6 border-t border-[#c9a84c]/15">
                                <p className="text-[10px] text-[#9a9080] font-light">Guest: <span className="text-[#f0ebe0]/80">{firstName} {lastName}</span></p>
                                <p className="text-[10px] text-[#9a9080] font-light">Email: <span className="text-[#f0ebe0]/80">{email}</span></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}