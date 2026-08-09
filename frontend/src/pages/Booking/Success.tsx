// pages/BookingSuccess.tsx
import { useLocation, Link } from "react-router-dom";
import { Check } from "lucide-react";

export default function Success() {
    const location = useLocation();
    const { firstName, email, selectedRoom, checkIn, checkOut, guests } = location.state || {};

    return (
        <div className="min-h-screen flex items-center justify-center px-8 pt-20 bg-[#0f0e0c]">
            <div className="max-w-lg text-center">
                <div className="w-16 h-16 border border-[#c9a84c] flex items-center justify-center mx-auto mb-8"><Check size={24} className="text-[#c9a84c]" /></div>
                <h2 className="text-4xl font-normal text-[#f0ebe0] mb-4 font-display">Request received.</h2>
                <p className="text-[#f0ebe0]/55 font-light leading-relaxed mb-3">Thank you, <strong className="text-[#f0ebe0]/80 font-normal">{firstName}</strong>. Our reservations team will confirm your stay at <strong className="text-[#f0ebe0]/80 font-normal">Maison Grand Hôtel</strong> within two hours.</p>
                <p className="text-sm text-[#9a9080] font-light mb-8">A confirmation will be sent to <span className="text-[#c9a84c]">{email}</span>.</p>
                <div className="border border-[#c9a84c]/15 p-6 text-left mb-8">
                    <p className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-4">Reservation Summary</p>
                    <div className="flex flex-col gap-2 text-sm">
                        <div className="flex justify-between"><span className="text-[#9a9080]">Room</span><span className="text-[#f0ebe0]/80">{selectedRoom}</span></div>
                        {checkIn && <div className="flex justify-between"><span className="text-[#9a9080]">Check-in</span><span className="text-[#f0ebe0]/80">{checkIn}</span></div>}
                        {checkOut && <div className="flex justify-between"><span className="text-[#9a9080]">Check-out</span><span className="text-[#f0ebe0]/80">{checkOut}</span></div>}
                        <div className="flex justify-between"><span className="text-[#9a9080]">Guests</span><span className="text-[#f0ebe0]/80">{guests}</span></div>
                    </div>
                </div>
                <Link to="/" className="text-sm text-[#c9a84c] tracking-wider underline-offset-4 hover:underline">Return to Home</Link>
            </div>
        </div>
    );
}