// pages/Booking.tsx
import { useState, type FormEvent } from "react";
import { useLocation } from "react-router";
import { Check } from "lucide-react";
import HeroBanner from "../../components/booking/HeroBanner";
import DirectBenefits from "../../components/booking/DirectBenefits";
import BookingForm from "../../components/booking/BookingForm";
import PoliciesSection from "../../components/booking/PoliciesSection";
import rooms from "../../constants/rooms";
import packages from "../../constants/packages";
import nightsBetween from "../../utils/nightsBetween";

export default function Booking() {
    const location = useLocation();
    const stateRoom = (location.state as { room?: string } | null)?.room ?? "";
    const [selectedRoom, setSelectedRoom] = useState(rooms.find((r) => r.name === stateRoom)?.name ?? rooms[4].name);
    const [checkIn, setCheckIn] = useState("");
    const [checkOut, setCheckOut] = useState("");
    const [guests, setGuests] = useState("2");
    const [selectedPackages, setSelectedPackages] = useState<string[]>([]);
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [requests, setRequests] = useState("");
    const [submitted, setSubmitted] = useState(false);

    const room = rooms.find((r) => r.name === selectedRoom) ?? rooms[4];
    const nights = nightsBetween(checkIn, checkOut);
    const packageTotal = packages.filter((p) => selectedPackages.includes(p.id)).reduce((s, p) => s + p.price, 0);
    const roomTotal = room.price * Math.max(nights, 1);
    const total = roomTotal + packageTotal * Math.max(nights, 1);

    function togglePackage(id: string) {
        setSelectedPackages((prev) => prev.includes(id) ? prev.filter((p) => p !== id) : [...prev, id]);
    }

    function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setSubmitted(true);
    }

    if (submitted) {
        return (
            <div className="min-h-screen flex items-center justify-center px-8 pt-20">
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
                    <button onClick={() => setSubmitted(false)} className="text-sm text-[#c9a84c] tracking-wider underline-offset-4 hover:underline">Make another reservation</button>
                </div>
            </div>
        );
    }

    return (
        <div>
            <HeroBanner />
            <DirectBenefits />
            <BookingForm
                selectedRoom={selectedRoom}
                setSelectedRoom={setSelectedRoom}
                checkIn={checkIn}
                setCheckIn={setCheckIn}
                checkOut={checkOut}
                setCheckOut={setCheckOut}
                guests={guests}
                setGuests={setGuests}
                selectedPackages={selectedPackages}
                firstName={firstName}
                setFirstName={setFirstName}
                lastName={lastName}
                setLastName={setLastName}
                email={email}
                setEmail={setEmail}
                phone={phone}
                setPhone={setPhone}
                requests={requests}
                setRequests={setRequests}
                handleSubmit={handleSubmit}
                togglePackage={togglePackage}
                room={room}
                nights={nights}
                roomTotal={roomTotal}
                total={total}
            />
            <PoliciesSection />
        </div>
    );
}