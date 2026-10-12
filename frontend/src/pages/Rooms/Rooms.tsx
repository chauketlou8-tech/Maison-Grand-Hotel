import { useState, useEffect } from "react";

import HeroBanner from "../../components/rooms/HeroBanner";
import GuestIdGuide from "../../components/rooms/GuestIDGuide";
import FilterBar from "../../components/rooms/FilterBar";
import RoomGrid from "../../components/rooms/RoomGrid";
import ComparisonTable from "../../components/rooms/ComparisonTable";
import FaqSection from "../../components/rooms/FAQ";
import CTA from "../../components/rooms/CTA";

import getRooms from "../../services/getRooms";
import type { Room } from "../../types/Room";
import { buildRoomTeasers } from "../../constants/roomDetails";

export default function RoomsPage() {
    const [filter, setFilter] = useState("all");
    const [rooms, setRooms] = useState<Room[]>([]);

    useEffect(() => {
        async function set() {
            setRooms(await getRooms());
        }

        void set();
    }, []);

    const teasers = buildRoomTeasers(rooms);
    const filtered = teasers.filter((t) => filter === "all" || t.category === filter);

    return (
        <div>
            <HeroBanner />
            <GuestIdGuide />
            <FilterBar filter={filter} setFilter={setFilter} count={filtered.length} />
            <RoomGrid rooms={filtered} />
            <ComparisonTable />
            <FaqSection />
            <CTA />
        </div>
    );
}