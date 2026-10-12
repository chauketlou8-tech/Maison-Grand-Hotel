import { type FormEvent, useEffect, useState } from "react";
import { AlertCircle, Check, ChevronDown, Loader2, Mail, MapPin, Phone } from "lucide-react";
import packages from "../../constants/packages";
import nightsBetween from "../../utils/nightsBetween";
import book from "../../services/book";
import { sendMessage, websocket } from "../../services/websock";
import extractDOB from "../../utils/extractDOB";
import pay from "../../services/pay";
import getSettings from "../../services/getSettings";
import getRooms from "../../services/getRooms";
import { buildRoomTeasers } from "../../constants/roomDetails";
import type { Settings } from "../../types/settings";
import type { Room } from "../../types/Room";
import type { RoomTeaser } from "../../types/RoomTeaser";

const EXTRA_ADULT_FEE = 10;
const CHILD_FEE = 20;

export default function BookingForm() {
    const [settings, setSettings] = useState<Settings>(null);
    const [rooms, setRooms] = useState<Room[]>([]);

    const [selectedRoom, setSelectedRoom] = useState<string>("");

    const [checkIn, setCheckIn] = useState<Date>(new Date());
    const [checkOut, setCheckOut] = useState<Date>(new Date());

    const [adults, setAdults] = useState<number>(2);
    const [children, setChildren] = useState<number>(0);
    const [selectedPackages, setSelectedPackages] = useState<string[]>([]);

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");

    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");

    const [requests, setRequests] = useState("");

    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [submitError, setSubmitError] = useState("");

    const [isLoading, setIsLoading] = useState(false);
    const [availableRooms, setAvailableRooms] = useState<Record<string, Room[]>>({});

    const [idNumber, setIdNumber] = useState("");
    const [nationality, setNationality] = useState("");

    const [dateOfBirth, setDateOfBirth] = useState<Date | null>(null);
    const [address, setAddress] = useState({ street: "", city: "", province: "", country: "", postalCode: "" });

    const guests = adults + children;

    const teasers: RoomTeaser[] = buildRoomTeasers(rooms);

    useEffect(() => {
        async function set() {
            setSettings(await getSettings());
            setRooms(await getRooms());
        }

        void set();
    }, []);

    useEffect(() => {
        if (!selectedRoom && teasers.length > 0) {
            setSelectedRoom(teasers[0].name);
        }
    }, [teasers, selectedRoom]);

    const room = teasers.find((r) => r.name === selectedRoom) ?? teasers[0];

    const nights = nightsBetween(checkIn, checkOut);

    const packageTotal = packages.filter((p) => selectedPackages.includes(p.id)).reduce((s, p) => s + p.price, 0);

    const extraAdults = Math.max(adults - 1, 0);
    const personSurchargePerNight = extraAdults * EXTRA_ADULT_FEE + children * CHILD_FEE;
    const personSurcharge = personSurchargePerNight * Math.max(nights, 1);

    const subtotal = (room?.price ?? 0) * Math.max(nights, 1)
        + packageTotal * Math.max(nights, 1)
        + personSurcharge;

    const vatRate = settings ? Number(settings.vat_rate) : 0;
    const serviceRate = settings ? Number(settings.service_charge_rate) : 0;
    const levyRate = settings ? Number(settings.tourism_levy) : 0;

    let vatAmount = 0;
    let total = subtotal;

    if (settings) {
        if (settings.prices_include_vat) {
            vatAmount = subtotal * vatRate / (100 + vatRate);
            total = subtotal;
        }

        else {
            vatAmount = subtotal * vatRate / 100;
            total = subtotal + vatAmount;
        }

        total += subtotal * serviceRate / 100;
        total += subtotal * levyRate / 100;
    }

    const serviceAmount = subtotal * serviceRate / 100;
    const levyAmount = subtotal * levyRate / 100;

    const showBreakdown = !!(settings && !settings.prices_include_vat && settings.show_tax_breakdown);

    const maxCapacity = rooms.length > 0
        ? Math.max(...rooms.map((r) => r.capacity))
        : 4;

    const adultOptions = Array.from({ length: maxCapacity }, (_, i) => i + 1);
    const childrenOptions = Array.from({ length: maxCapacity }, (_, i) => i);

    useEffect(() => {
        const handleMessage = (e: MessageEvent) => {
            try {
                const data = JSON.parse(e.data);
                if (data.type === "availableRooms") {
                    const sortedRooms: Record<string, Room[]> = {};

                    data.data.rooms.forEach((r: Room) => {
                        if (!sortedRooms[r.roomtype]) {
                            sortedRooms[r.roomtype] = [];
                        }
                        sortedRooms[r.roomtype].push(r);
                    });

                    setAvailableRooms(sortedRooms);
                }
            }
            catch (error) {
                console.error("Error parsing message:", error);
            }
        };

        websocket.addEventListener("message", handleMessage);
        sendMessage("getAvailableRooms", {});

        return () => {
            websocket.removeEventListener("message", handleMessage);
        };
    }, []);

    useEffect(() => {
        const nextDay = new Date(checkIn);
        nextDay.setDate(checkIn.getDate() + 1);
        setCheckOut(nextDay);
    }, [checkIn]);

    function togglePackage(id: string) {
        setSelectedPackages((prev) => prev.includes(id) ? prev.filter((p) => p !== id) : [...prev, id]);
    }

    function validateForm() {
        const newErrors: { [key: string]: string } = {};
        if (!firstName.trim()) newErrors.firstName = "First name is required";

        if (!lastName.trim()) newErrors.lastName = "Last name is required";

        if (!email.trim()) {
            newErrors.email = "Email is required";
        }

        else if (!/\S+@\S+\.\S+/.test(email)) {
            newErrors.email = "Please enter a valid email address";
        }

        if (!phone.trim()) newErrors.phone = "Phone number is required";

        if (settings?.id_required && !idNumber.trim()) newErrors.idNumber = "ID number is required";

        if (!nationality.trim()) newErrors.nationality = "Nationality is required";

        if (!address.street.trim()) newErrors.street = "Street is required";

        if (!address.city.trim()) newErrors.city = "City is required";

        if (!address.province.trim()) newErrors.province = "Province is required";

        if (!address.country.trim()) newErrors.country = "Country is required";

        if (!address.postalCode.trim()) newErrors.postalCode = "Postal code is required";

        if (!checkIn) newErrors.checkIn = "Check-in date is required";

        if (!checkOut) newErrors.checkOut = "Check-out date is required";

        if (checkIn && checkOut && new Date(checkOut) <= new Date(checkIn)) {
            newErrors.checkOut = "Check-out must be after check-in";
        }

        if (settings) {
            if (nights < settings.min_nights) {
                newErrors.checkOut = `Minimum stay is ${settings.min_nights} night(s)`;
            }

            if (nights > settings.max_nights) {
                newErrors.checkOut = `Maximum stay is ${settings.max_nights} night(s)`;
            }

            const today = new Date();
            today.setHours(0, 0, 0, 0);
            const daysAhead = Math.round((checkIn.getTime() - today.getTime()) / 86_400_000);

            if (daysAhead > settings.advance_booking_limit_days) {
                newErrors.checkIn = `Bookings can only be made up to ${settings.advance_booking_limit_days} days in advance`;
            }
        }

        setErrors(newErrors);

        if (Object.keys(newErrors).length > 0) {
            setTimeout(() => {
                const firstErrorField = document.querySelector('.border-red-500');
                if (firstErrorField) {
                    firstErrorField.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }, 100);
        }

        return Object.keys(newErrors).length === 0;
    }

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setSubmitError("");

        if (validateForm()) {
            setIsLoading(true);

            const minLoadingTime = 3500;
            const startTime = Date.now();

            try {
                const bookingDetails = {
                    firstName,
                    lastName,
                    email,
                    phone,
                    idNumber,
                    nationality,
                    dateOfBirth,
                    address,
                    requests,
                    checkIn,
                    checkOut,
                    guests,
                    selectedRoom,
                    selectedPackages,
                    total
                };

                const { guest_id, reservation_id } = await book(bookingDetails);

                sessionStorage.setItem("bookingState", JSON.stringify({
                    firstName,
                    email,
                    selectedRoom,
                    checkIn,
                    checkOut,
                    guests,
                    total,
                    nights,
                    paid: true,
                }));

                window.location.href = await pay(total, guest_id, reservation_id);

                const deltaTime = Date.now() - startTime;
                const remainingTime = minLoadingTime - deltaTime;

                setTimeout(() => {
                    setIsLoading(false);
                }, remainingTime > 0 ? remainingTime : 0);

            } catch (error: any) {
                setIsLoading(false);
                setSubmitError(error.response?.data?.message || "Something went wrong. Please try again.");
                setTimeout(() => {
                    const errorElement = document.querySelector('.border-red-500');
                    if (errorElement) {
                        errorElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }
                }, 100);
            }
        }
    }

    function formatDate(date: Date): string {
        return date.toISOString().split("T")[0];
    }

    function maxCheckInDate(): string {
        const d = new Date();
        d.setDate(d.getDate() + (settings?.advance_booking_limit_days ?? 365));
        return formatDate(d);
    }

    function isRoomAvailable(roomName: string): boolean {
        const roomType = roomName.split(" ")[0];
        const getter = roomType[0].toUpperCase() + roomType[1].toUpperCase();
        return availableRooms[getter]?.length > 0;
    }

    if (!room) {
        return (
            <section className="py-16 px-8 md:px-20 bg-[#0f0e0c]">
                <div className="max-w-6xl mx-auto flex items-center justify-center py-20">
                    <Loader2 size={24} className="animate-spin text-[#c9a84c]" />
                </div>
            </section>
        );
    }

    const fullAddress = [
        settings?.hotel_street,
        settings?.hotel_city,
        settings?.hotel_province,
        settings?.hotel_postal_code
    ].filter(Boolean).join(", ");

    return (
        <section className="py-16 px-8 md:px-20 bg-[#0f0e0c]">
            <div className="max-w-6xl mx-auto">
                <form onSubmit={handleSubmit} className="grid lg:grid-cols-3 gap-10">
                    <div className="lg:col-span-2 flex flex-col gap-8">
                        {
                            submitError &&

                            <div className="bg-red-500/10 border border-red-500 p-4 flex items-center gap-3">
                                <AlertCircle size={16} className="text-red-500 flex-shrink-0" />
                                <p className="text-red-500 text-sm font-light">{submitError}</p>
                            </div>
                        }
                        <div>
                            <h2 className="text-2xl font-normal mb-5 text-[#f0ebe0] font-display">1. Choose Your Room</h2>

                            <div className="grid sm:grid-cols-2 gap-3">
                                {
                                    teasers.map((r) => {
                                        const available = isRoomAvailable(r.name);

                                        return (
                                            <button key={r.name} type="button" onClick={() => available && setSelectedRoom(r.name)} disabled={!available} className={`text-left p-5 border transition-all duration-200 cursor-pointer ${!available ? "opacity-40 cursor-not-allowed" : ""} ${selectedRoom === r.name ? "border-[#c9a84c] bg-[#c9a84c]/5" : "border-[#c9a84c]/15 hover:border-[#c9a84c]/30"}`}>
                                                <div className="flex justify-between items-start gap-2">
                                                    <div className="flex-1">
                                                        <div className="flex items-center gap-2 mb-0.5">
                                                            <code className="text-[10px] font-mono text-[#c9a84c]/70 border border-[#c9a84c]/25 px-1.5 py-0.5">{r.code}</code>
                                                            <p className="text-sm text-[#f0ebe0]/85 leading-tight">{r.name}</p>
                                                            {!available && <span className="text-[10px] text-red-500">(Unavailable)</span>}
                                                        </div>
                                                        <p className="text-xs text-[#9a9080]">{r.size} · Floors {r.floors}</p>
                                                    </div>
                                                    <div className="text-right flex-shrink-0">
                                                        <p className="text-[#c9a84c] font-light">R{r.price}</p>
                                                        <p className="text-xs text-[#9a9080]">/night</p>
                                                    </div>
                                                </div>
                                                {selectedRoom === r.name && available && <div className="mt-3 flex items-center gap-1.5 text-xs text-[#c9a84c]"><Check size={11} /> Selected</div>}
                                            </button>
                                        );
                                    })
                                }
                            </div>
                        </div>

                        <div>
                            <h2 className="text-2xl font-normal mb-5 text-[#f0ebe0] font-display">2. Dates &amp; Guests</h2>

                            <div className={`grid gap-4 ${settings?.children_allowed ? "sm:grid-cols-4" : "sm:grid-cols-3"}`}>
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Check-in</label>
                                    <input type="date" value={formatDate(checkIn)} min={formatDate(new Date())} max={maxCheckInDate()} onChange={(e) => setCheckIn(new Date(e.target.value))} className={`w-full bg-[#1a1814] border px-4 py-3 text-sm text-[#f0ebe0] focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.checkIn ? "border-red-500" : "border-[#c9a84c]/15"}`} />

                                    {
                                        errors.checkIn &&
                                        <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.checkIn}</p>
                                    }
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Check-out</label>
                                    <input type="date" value={formatDate(checkOut)} min={formatDate(checkIn)} onChange={(e) => setCheckOut(new Date(e.target.value))} className={`w-full bg-[#1a1814] border px-4 py-3 text-sm text-[#f0ebe0] focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.checkOut ? "border-red-500" : "border-[#c9a84c]/15"}`} />

                                    {
                                        errors.checkOut &&
                                        <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.checkOut}</p>
                                    }
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Adults</label>

                                    <div className="relative">
                                        <select value={adults} onChange={(e) => setAdults(Number(e.target.value))} className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] focus:outline-none focus:border-[#c9a84c] transition-colors appearance-none cursor-pointer">
                                            {adultOptions.map((n) => (<option key={n}>{n}</option>))}
                                        </select>

                                        <ChevronDown size={14} className="absolute right-3 top-1/2 -translate-y-1/2 text-[#9a9080] pointer-events-none" />
                                    </div>
                                </div>

                                {settings?.children_allowed && (
                                    <div>
                                        <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Children</label>

                                        <div className="relative">
                                            <select value={children} onChange={(e) => setChildren(Number(e.target.value))} className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] focus:outline-none focus:border-[#c9a84c] transition-colors appearance-none cursor-pointer">
                                                {childrenOptions.map((n) => (<option key={n}>{n}</option>))}
                                            </select>

                                            <ChevronDown size={14} className="absolute right-3 top-1/2 -translate-y-1/2 text-[#9a9080] pointer-events-none" />
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>

                        <div>
                            <h2 className="text-2xl font-normal mb-5 text-[#f0ebe0] font-display">3. Add-ons</h2>

                            <div className="flex flex-col gap-3">
                                {
                                    packages.map((pkg) => (
                                        <button key={pkg.id} type="button" onClick={() => togglePackage(pkg.id)} className={`text-left p-5 border transition-all duration-200 cursor-pointer ${selectedPackages.includes(pkg.id) ? "border-[#c9a84c] bg-[#c9a84c]/5" : "border-[#c9a84c]/15 hover:border-[#c9a84c]/30"}`}>
                                            <div className="flex items-start justify-between gap-4">
                                                <div className="flex-1">
                                                    <div className="flex items-center gap-3 mb-2">
                                                        <p className="text-sm text-[#f0ebe0]/85">{pkg.label}</p>

                                                        {
                                                            selectedPackages.includes(pkg.id) &&
                                                            <Check size={12} className="text-[#c9a84c]" />
                                                        }
                                                    </div>

                                                    <div className="flex flex-col gap-1">
                                                        {
                                                            pkg.includes.map((inc) => (
                                                                <p key={inc} className="text-xs text-[#9a9080] font-light">· {inc}</p>
                                                            ))
                                                        }
                                                    </div>
                                                </div>
                                                <span className="text-[#c9a84c] font-light text-sm flex-shrink-0">R{pkg.price}<span className="text-xs text-[#9a9080]">/night</span></span>
                                            </div>
                                        </button>
                                    ))
                                }
                            </div>
                        </div>

                        <div>
                            <h2 className="text-2xl font-normal mb-5 text-[#f0ebe0] font-display">4. Your Details</h2>

                            <div className="grid sm:grid-cols-2 gap-4 mb-4">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">First Name</label>
                                    <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} placeholder="Jean" className={`w-full bg-[#1a1814] border px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.firstName ? "border-red-500" : "border-[#c9a84c]/15"}`} />
                                    {errors.firstName && <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.firstName}</p>}
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Last Name</label>
                                    <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} placeholder="Dupont" className={`w-full bg-[#1a1814] border px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.lastName ? "border-red-500" : "border-[#c9a84c]/15"}`} />
                                    {errors.lastName && <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.lastName}</p>}
                                </div>
                            </div>

                            <div className="grid sm:grid-cols-2 gap-4 mb-4">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Email</label>
                                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="jean@example.com" className={`w-full bg-[#1a1814] border px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.email ? "border-red-500" : "border-[#c9a84c]/15"}`} />
                                    {errors.email && <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.email}</p>}
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Phone</label>
                                    <input type="tel" value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="+27 21 000 0000" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                            </div>

                            <div className="grid sm:grid-cols-2 gap-4 mb-4">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">ID Number {settings?.id_required ? "" : "(optional)"}</label>
                                    <input type="text" value={idNumber} onChange={(e) => { setIdNumber(e.target.value); const dob = extractDOB(e.target.value); if (dob) setDateOfBirth(dob); }} placeholder="ID Number" className={`w-full bg-[#1a1814] border px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors ${errors.idNumber ? "border-red-500" : "border-[#c9a84c]/15"}`} />
                                    {errors.idNumber && <p className="text-red-500 text-xs mt-1 flex items-center gap-1"><AlertCircle size={12} /> {errors.idNumber}</p>}
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Nationality</label>
                                    <input type="text" value={nationality} onChange={(e) => setNationality(e.target.value)} placeholder="South African" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                            </div>

                            <div className="grid sm:grid-cols-2 gap-4 mb-4">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Street</label>
                                    <input type="text" value={address.street} onChange={(e) => setAddress({...address, street: e.target.value})} placeholder="123 Main St" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">City</label>
                                    <input type="text" value={address.city} onChange={(e) => setAddress({...address, city: e.target.value})} placeholder="Cape Town" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                            </div>

                            <div className="grid sm:grid-cols-3 gap-4 mb-4">
                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Province</label>
                                    <input type="text" value={address.province} onChange={(e) => setAddress({...address, province: e.target.value})} placeholder="Western Cape" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Country</label>
                                    <input type="text" value={address.country} onChange={(e) => setAddress({...address, country: e.target.value})} placeholder="South Africa" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>

                                <div>
                                    <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Postal Code</label>
                                    <input type="text" value={address.postalCode} onChange={(e) => setAddress({...address, postalCode: e.target.value})} placeholder="7700" className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors" />
                                </div>
                            </div>

                            <div>
                                <label className="block text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-2">Special Requests</label>
                                <textarea rows={3} value={requests} onChange={(e) => setRequests(e.target.value)} placeholder="Dietary requirements, occasion arrangements, floor preference..." className="w-full bg-[#1a1814] border border-[#c9a84c]/15 px-4 py-3 text-sm text-[#f0ebe0] placeholder:text-[#9a9080]/40 focus:outline-none focus:border-[#c9a84c] transition-colors resize-none" />
                            </div>
                        </div>
                    </div>

                    <div className="lg:col-span-1">
                        <div className="sticky top-28 bg-[#1a1814] border border-[#c9a84c]/15 p-7">
                            <p className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080] mb-5">Reservation Summary</p>

                            <div className="flex flex-col gap-3 text-sm mb-6 pb-6 border-b border-[#c9a84c]/15">
                                <div className="flex justify-between items-start gap-3">
                                    <span className="text-[#9a9080] text-xs">Room</span>
                                    <span className="text-[#f0ebe0]/80 text-right leading-tight">{selectedRoom}</span>
                                </div>

                                {
                                    checkIn &&
                                    <div className="flex justify-between">
                                        <span className="text-[#9a9080] text-xs">Check-in</span>
                                        <span className="text-[#f0ebe0]/80">{formatDate(checkIn)}</span>
                                    </div>
                                }

                                {
                                    checkOut &&
                                    <div className="flex justify-between">
                                        <span className="text-[#9a9080] text-xs">Check-out</span>
                                        <span className="text-[#f0ebe0]/80">{formatDate(checkOut)}</span>
                                    </div>
                                }

                                {
                                    nights > 0 &&
                                    <div className="flex justify-between">
                                        <span className="text-[#9a9080] text-xs">Nights</span>
                                        <span className="text-[#f0ebe0]/80">{nights}</span>
                                    </div>
                                }

                                <div className="flex justify-between">
                                    <span className="text-[#9a9080] text-xs">Guests</span>
                                    <span className="text-[#f0ebe0]/80">{guests}</span>
                                </div>
                            </div>

                            <div className="flex flex-col gap-2 text-sm mb-6 pb-6 border-b border-[#c9a84c]/15">
                                <div className="flex justify-between">
                                    <span className="text-[#9a9080] text-xs">{room.name.split(" ")[0]} ({nights > 0 ? `${nights}n` : "per night"})</span>
                                    <span className="text-[#f0ebe0]/75">R{nights > 0 ? ((room?.price ?? 0) * nights).toLocaleString() : room?.price}</span>
                                </div>

                                {
                                    packages.filter((p) => selectedPackages.includes(p.id)).map((p) => (
                                        <div key={p.id} className="flex justify-between">
                                            <span className="text-[#9a9080] text-xs">{p.label}</span>
                                            <span className="text-[#f0ebe0]/75">R{nights > 0 ? (p.price * nights).toLocaleString() : p.price}</span>
                                        </div>
                                    ))
                                }

                                {showBreakdown && (
                                    <>
                                        <div className="flex justify-between">
                                            <span className="text-[#9a9080] text-xs">VAT ({settings?.vat_rate}%)</span>
                                            <span className="text-[#f0ebe0]/75">R{vatAmount.toFixed(2)}</span>
                                        </div>

                                        {serviceRate > 0 && (
                                            <div className="flex justify-between">
                                                <span className="text-[#9a9080] text-xs">Service ({serviceRate}%)</span>
                                                <span className="text-[#f0ebe0]/75">R{serviceAmount.toFixed(2)}</span>
                                            </div>
                                        )}

                                        {levyRate > 0 && (
                                            <div className="flex justify-between">
                                                <span className="text-[#9a9080] text-xs">Tourism Levy ({levyRate}%)</span>
                                                <span className="text-[#f0ebe0]/75">R{levyAmount.toFixed(2)}</span>
                                            </div>
                                        )}
                                    </>
                                )}
                            </div>

                            <div className="flex justify-between items-baseline mb-6">
                                <span className="text-xs tracking-[0.15em] uppercase text-[#9a9080]">{nights > 0 ? "Total Est." : "From"}</span>
                                <span className="text-2xl text-[#c9a84c] font-light font-display">R{nights > 0 ? total.toLocaleString() : room.price}</span>
                            </div>

                            <button type="submit" disabled={isLoading} className="w-full py-4 bg-[#c9a84c] text-[#0f0e0c] text-sm tracking-[0.2em] uppercase font-medium hover:bg-[#c9a84c]/90 transition-colors mb-3 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2">
                                {
                                    isLoading ?
                                        <div className="flex items-center gap-2">
                                            <Loader2 size={16} className="animate-spin" />Submitting...
                                        </div>
                                        :
                                        "Request Reservation"
                                }
                            </button>

                            <p className="text-xs text-[#9a9080] text-center font-light">You'll be redirected to complete payment.</p>

                            <div className="mt-6 pt-6 border-t border-[#c9a84c]/15 flex flex-col gap-3">
                                <p className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080]">Prefer to call?</p>

                                <a href={`tel:${settings?.hotel_phone?.replace(/\s/g, "") ?? ""}`} className="flex items-center gap-2 text-xs text-[#f0ebe0]/60 hover:text-[#c9a84c] transition-colors">
                                    <Phone size={11} className="text-[#c9a84c]" /> {settings?.hotel_phone}
                                </a>

                                <a href={`mailto:${settings?.hotel_email ?? ""}`} className="flex items-center gap-2 text-xs text-[#f0ebe0]/60 hover:text-[#c9a84c] transition-colors">
                                    <Mail size={11} className="text-[#c9a84c]" /> {settings?.hotel_email}
                                </a>

                                <span className="flex items-start gap-2 text-xs text-[#f0ebe0]/60">
                                    <MapPin size={11} className="text-[#c9a84c] flex-shrink-0 mt-0.5" /> {fullAddress}
                                </span>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    );
}