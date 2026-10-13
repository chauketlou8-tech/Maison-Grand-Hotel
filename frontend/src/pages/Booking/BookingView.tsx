import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import {
    AlertCircle, CheckCircle, Clock, Loader2, XCircle,
    Printer, Download, Calendar, BedDouble, Users,
    MapPin, Phone, Mail, IdCard,
    Banknote, Clock4, ShieldCheck, ShieldAlert
} from "lucide-react";
import getReservation from "../../services/getReservation";
import type { Reservation } from "../../types/Reservation";

export default function BookingView() {
    const { reservationid } = useParams<{ reservationid: string }>();

    const [reservation, setReservation] = useState<Reservation | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [notFound, setNotFound] = useState(false);

    useEffect(() => {
        async function load() {
            if (!reservationid) {
                setNotFound(true);
                setIsLoading(false);
                return;
            }

            const minLoadingTime = 3500;
            const startTime = Date.now();

            try {
                const data = await getReservation(reservationid);
                setReservation(data);
            }

            catch {
                setNotFound(true);
            }

            const deltaTime = Date.now() - startTime;
            const remainingTime = minLoadingTime - deltaTime;

            setTimeout(() => {
                setIsLoading(false);
            }, remainingTime > 0 ? remainingTime : 0);
        }

        void load();
    }, [reservationid]);

    if (isLoading) {
        return (
            <section className="py-32 px-8 md:px-20 bg-[#0f0e0c] min-h-screen flex items-center justify-center">
                <Loader2 size={28} className="animate-spin text-[#c9a84c]" />
            </section>
        );
    }

    if (notFound || !reservation) {
        return (
            <section className="py-32 px-8 md:px-20 bg-[#0f0e0c] min-h-screen flex items-center justify-center">
                <div className="max-w-md w-full text-center">
                    <XCircle size={32} className="text-[#a84c4c] mx-auto mb-6" />
                    <p className="text-[11px] tracking-[0.5em] uppercase mb-3 font-light text-[#c9a84c]">Not Found</p>
                    <h1 className="text-3xl font-normal mb-4 text-[#f0ebe0] font-display">Reservation not found</h1>
                    <p className="text-sm font-light leading-relaxed mb-8 text-[#9a9080]">
                        This link may be invalid or no longer active.
                    </p>
                    <Link to="/booking/find" className="inline-flex items-center justify-center px-8 py-4 bg-[#c9a84c] text-[#0f0e0c] text-sm tracking-[0.2em] uppercase font-medium hover:bg-[#c9a84c]/90 transition-colors">
                        Find Reservation
                    </Link>
                </div>
            </section>
        );
    }

    const status = reservation.reservationstatus;

    const statusMeta =
        status === "CANCELLED"
            ? { icon: XCircle, label: "Cancelled", color: "#a84c4c" }
            : status === "PENDING"
                ? { icon: Clock, label: "Pending", color: "#d07a3c" }
                : status === "PROCESSING"
                    ? { icon: Clock, label: "Processing", color: "#c9a84c" }
                    : status === "EXPIRED"
                        ? { icon: AlertCircle, label: "Expired", color: "#9a9080" }
                        : { icon: CheckCircle, label: "Confirmed", color: "#4a9d6a" };

    const StatusIcon = statusMeta.icon;

    const checkIn = new Date(reservation.checkindate);
    const checkOut = new Date(reservation.checkoutdate);
    const bookedOn = new Date(reservation.reservationdate);
    const created = new Date(reservation.created_at);
    const updated = new Date(reservation.updated_at);
    const dob = new Date(reservation.dateofbirth);

    const nights = Math.max(0, Math.round((checkOut.getTime() - checkIn.getTime()) / 86_400_000));
    const perNight = nights > 0 ? Math.round(reservation.total / nights) : reservation.total;

    const occupancy = reservation.capacity > 0
        ? Math.round((reservation.numberofguests / reservation.capacity) * 100)
        : 0;

    const fullAddress = [
        reservation.street,
        reservation.city,
        reservation.province,
        reservation.postalcode,
        reservation.country
    ].filter(Boolean).join(", ");

    const fmtDate = (d: Date) => d.toLocaleDateString("en-ZA", { day: "2-digit", month: "short", year: "numeric" });
    const fmtDateTime = (d: Date) => d.toLocaleString("en-ZA", { day: "2-digit", month: "short", year: "numeric", hour: "2-digit", minute: "2-digit" });

    const roomOk = ["available", "clean", "ready", "vacant"].includes(reservation.roomstatus?.toLowerCase() ?? "");

    return (
        <div className="min-h-screen bg-[#0f0e0c] text-[#f0ebe0]">
            <main className="max-w-7xl mx-auto px-6 md:px-20 py-14">

                {/* ── Header row ─────────────────────────────────── */}
                <div className="flex flex-col md:flex-row md:items-end justify-between gap-8 pb-10 border-b border-[#c9a84c]/10">
                    <div>
                        <div className="inline-flex items-center gap-3 mb-6">
                            <StatusIcon size={13} style={{ color: statusMeta.color }} />
                            <span className="text-[11px] tracking-[0.4em] uppercase font-light" style={{ color: statusMeta.color }}>
                                {statusMeta.label}
                            </span>
                        </div>
                        <h1 className="text-5xl md:text-6xl font-normal leading-[1.05] text-[#f0ebe0] font-display">
                            {reservation.firstname} <em className="not-italic text-[#c9a84c]">{reservation.lastname}</em>
                        </h1>
                        <p className="text-xs font-light text-[#9a9080] tracking-[0.2em] uppercase mt-4">
                            Guest #{reservation.guest_id} · Booking #{reservation.id}
                        </p>
                    </div>

                    <div className="flex flex-wrap gap-3">
                        <button onClick={() => window.print()} className="inline-flex items-center gap-3 px-6 py-4 border text-[11px] tracking-[0.25em] uppercase font-light transition-colors hover:border-[#c9a84c]" style={{ borderColor: "rgba(201, 168, 76, 0.15)", color: "rgba(240, 235, 224, 0.7)" }}>
                            <Printer size={13} /> Print
                        </button>
                        <button className="inline-flex items-center gap-3 px-6 py-4 border text-[11px] tracking-[0.25em] uppercase font-light transition-colors hover:border-[#c9a84c]" style={{ borderColor: "rgba(201, 168, 76, 0.15)", color: "rgba(240, 235, 224, 0.7)" }}>
                            <Download size={13} /> Export
                        </button>
                    </div>
                </div>

                {/* ── Key metrics ────────────────────────────────── */}
                <div className="grid grid-cols-2 md:grid-cols-4 gap-px bg-[#c9a84c]/10 border border-[#c9a84c]/10 mt-10">
                    <Metric label="Nights" value={String(nights)} sub={`${fmtDate(checkIn)} → ${fmtDate(checkOut)}`} />
                    <Metric label="Guests" value={String(reservation.numberofguests)} sub={`Capacity ${reservation.capacity} · ${occupancy}%`} />
                    <Metric label="Room" value={String(reservation.roomnumber)} sub={`${reservation.roomtype} · Floor ${reservation.floornumber}`} />
                    <Metric label="Total" value={`R${Number(reservation.total).toLocaleString()}`} sub={`R${perNight.toLocaleString()} / night`} accent />
                </div>

                {/* ── Two-column body ───────────────────────────── */}
                <div className="grid lg:grid-cols-3 gap-8 mt-12">

                    {/* Left column */}
                    <div className="lg:col-span-2 flex flex-col gap-8">

                        <Panel title="Stay" icon={<Calendar size={13} />}>
                            <div className="grid sm:grid-cols-2 gap-x-10 gap-y-7">
                                <Detail label="Reservation Date" value={fmtDate(bookedOn)} />
                                <Detail label="Check-in" value={fmtDate(checkIn)} />
                                <Detail label="Check-out" value={fmtDate(checkOut)} />
                                <Detail label="Nights" value={String(nights)} />
                                <Detail label="Guests" value={String(reservation.numberofguests)} />
                                <Detail label="Status" value={status} />
                            </div>
                        </Panel>

                        <Panel title="Room" icon={<BedDouble size={13} />}>
                            <div className="grid sm:grid-cols-2 gap-x-10 gap-y-7">
                                <Detail label="Room ID" value={`#${reservation.roomid}`} mono />
                                <Detail label="Room Number" value={String(reservation.roomnumber)} />
                                <Detail label="Floor" value={String(reservation.floornumber)} />
                                <Detail label="Requested Type" value={reservation.requestedroomtype} />
                                <Detail label="Room Type" value={reservation.roomtype} />
                                <Detail label="Capacity" value={String(reservation.capacity)} />
                                <Detail label="Price / Night" value={`R${Number(reservation.price).toLocaleString()}`} />
                                <Detail label="Room Status" value={reservation.roomstatus} />
                            </div>
                            {reservation.description && (
                                <p className="mt-7 pt-7 border-t border-[#c9a84c]/10 text-sm font-light text-[#9a9080] leading-relaxed italic font-display">
                                    {reservation.description}
                                </p>
                            )}
                        </Panel>

                        <Panel title="Guest" icon={<Users size={13} />}>
                            <div className="grid sm:grid-cols-2 gap-x-10 gap-y-7">
                                <Detail label="Guest ID" value={`#${reservation.guest_id}`} mono />
                                <Detail label="Full Name" value={`${reservation.firstname} ${reservation.lastname}`} />
                                <Detail label="Email" value={reservation.email} icon={<Mail size={12} />} />
                                <Detail label="Phone" value={reservation.phonenumber} icon={<Phone size={12} />} />
                                <Detail label="ID Number" value={reservation.idnumber} icon={<IdCard size={12} />} mono />
                                <Detail label="Date of Birth" value={fmtDate(dob)} />
                                <Detail label="Nationality" value={reservation.nationality} />
                                <Detail label="Address" value={fullAddress} icon={<MapPin size={12} />} />
                            </div>
                        </Panel>

                        {(reservation.requests || reservation.selectedpackages?.length > 0) && (
                            <Panel title="Notes & Add-ons" icon={<AlertCircle size={13} />}>
                                {reservation.selectedpackages?.length > 0 && (
                                    <div className="mb-7">
                                        <p className="text-[10px] tracking-[0.35em] uppercase text-[#9a9080] mb-4">Selected Packages</p>
                                        <div className="flex flex-wrap gap-2">
                                            {reservation.selectedpackages.map((p) => (
                                                <span key={p} className="text-xs tracking-wider px-4 py-2 border border-[#c9a84c]/25 text-[#c9a84c] font-light">
                                                    {p}
                                                </span>
                                            ))}
                                        </div>
                                    </div>
                                )}
                                {reservation.requests && (
                                    <div>
                                        <p className="text-[10px] tracking-[0.35em] uppercase text-[#9a9080] mb-4">Guest Requests</p>
                                        <blockquote className="pl-6 border-l-2 border-[#c9a84c]/30">
                                            <p className="text-base font-light leading-relaxed text-[#f0ebe0]/75 italic font-display">
                                                {reservation.requests}
                                            </p>
                                        </blockquote>
                                    </div>
                                )}
                            </Panel>
                        )}
                    </div>

                    {/* Right column */}
                    <aside className="flex flex-col gap-6">

                        <div className="bg-[#1a1814] border p-6" style={{ borderColor: `${statusMeta.color}33` }}>
                            <div className="flex items-center gap-3 mb-4">
                                <StatusIcon size={13} style={{ color: statusMeta.color }} />
                                <span className="text-[10px] tracking-[0.4em] uppercase font-light" style={{ color: statusMeta.color }}>
                                    {statusMeta.label}
                                </span>
                            </div>
                            <p className="text-sm font-light text-[#9a9080] leading-relaxed">
                                {status === "CONFIRMED" && "Reservation is confirmed and locked in."}
                                {status === "PENDING" && "Awaiting payment or confirmation."}
                                {status === "CANCELLED" && "This reservation has been cancelled."}
                                {status === "EXPIRED" && "This reservation has expired and is no longer valid."}
                            </p>
                        </div>

                        <Panel title="Payment" icon={<Banknote size={13} />}>
                            <div className="flex flex-col gap-4">
                                <Row label="Room Rate" value={`R${Number(reservation.price).toLocaleString()} / night`} />
                                <Row label="Nights" value={String(nights)} />
                                <Row label="Subtotal" value={`R${(Number(reservation.price) * nights).toLocaleString()}`} />
                                <div className="pt-5 border-t border-[#c9a84c]/10 flex items-baseline justify-between">
                                    <span className="text-[10px] tracking-[0.35em] uppercase text-[#9a9080]">Total</span>
                                    <span className="text-3xl font-light text-[#c9a84c] font-display">R{Number(reservation.total).toLocaleString()}</span>
                                </div>
                            </div>
                        </Panel>

                        <Panel title="Audit Trail" icon={<Clock4 size={13} />}>
                            <div className="flex flex-col gap-4">
                                <Row label="Created" value={fmtDateTime(created)} />
                                <Row label="Last Updated" value={fmtDateTime(updated)} />
                                <Row label="Reservation ID" value={reservation.reservationid} mono />
                                <Row label="Internal ID" value={`#${reservation.id}`} mono />
                            </div>
                        </Panel>

                        <Panel title="Room State" icon={roomOk ? <ShieldCheck size={13} /> : <ShieldAlert size={13} />}>
                            <Row label="Status" value={reservation.roomstatus} />
                            <Row label="Room" value={`${reservation.roomnumber} · Floor ${reservation.floornumber}`} />
                        </Panel>
                    </aside>
                </div>
            </main>
        </div>
    );
}

function Panel({ title, icon, children }: { title: string; icon?: React.ReactNode; children: React.ReactNode }) {
    return (
        <section className="bg-[#1a1814] border border-[#c9a84c]/15">
            <header className="flex items-center gap-3 px-6 py-4 border-b border-[#c9a84c]/10">
                <span className="text-[#c9a84c]">{icon}</span>
                <h2 className="text-[10px] tracking-[0.4em] uppercase text-[#c9a84c] font-light">{title}</h2>
            </header>
            <div className="p-6">{children}</div>
        </section>
    );
}

function Detail({ label, value, mono, icon }: { label: string; value: string; mono?: boolean; icon?: React.ReactNode }) {
    return (
        <div>
            <p className="text-[10px] tracking-[0.35em] uppercase mb-2 text-[#9a9080]">{label}</p>
            <p className={`text-sm font-light text-[#f0ebe0]/85 break-words inline-flex items-center gap-2 ${mono ? "font-mono" : ""}`}>
                {icon && <span className="text-[#c9a84c]/60">{icon}</span>}
                {value || "—"}
            </p>
        </div>
    );
}

function Row({ label, value, mono }: { label: string; value: string; mono?: boolean }) {
    return (
        <div className="flex justify-between gap-4">
            <span className="text-[10px] tracking-[0.3em] uppercase text-[#9a9080]">{label}</span>
            <span className={`text-sm text-[#f0ebe0]/85 font-light text-right ${mono ? "font-mono" : ""}`}>{value}</span>
        </div>
    );
}

function Metric({ label, value, sub, accent }: { label: string; value: string; sub: string; accent?: boolean }) {
    return (
        <div className="bg-[#0f0e0c] p-6">
            <p className="text-[10px] tracking-[0.4em] uppercase text-[#9a9080] mb-3">{label}</p>
            <p className={`text-3xl font-light font-display ${accent ? "text-[#c9a84c]" : "text-[#f0ebe0]"}`}>{value}</p>
            <p className="text-xs font-light text-[#9a9080] mt-2 tracking-wider">{sub}</p>
        </div>
    );
}