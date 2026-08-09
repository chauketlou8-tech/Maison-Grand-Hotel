import { useState } from "react";

import HeroBanner from "../../components/rooms/HeroBanner";
import GuestIdGuide from "../../components/rooms/GuestIDGuide";
import FilterBar from "../../components/rooms/FilterBar";
import RoomGrid from "../../components/rooms/RoomGrid";
import ComparisonTable from "../../components/rooms/ComparisonTable";
import FaqSection from "../../components/rooms/FAQ";
import CTA from "../../components/rooms/CTA";

import allRooms from "../../constants/rooms.json";

import type { Room } from "../../types/Room";

export default function RoomsPage() {
    const [filter, setFilter] = useState("all");
    const filtered = allRooms.filter((room: Room) => filter === "all" || room.category === filter);

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