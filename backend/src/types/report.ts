export type Report = {
    id: number;
    report_date: string;
    total_rooms: number;
    occupied_rooms: number;
    available_rooms: number;
    maintenance_rooms: number;
    arrivals: number;
    departures: number;
    in_house: number;
    revenue: string;
    tasks_created: number;
    tasks_completed: number;
    created_at: string;
    updated_at: string;
};