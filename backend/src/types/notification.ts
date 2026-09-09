export type Notification = {
    notificationid?: number;
    type: "reservation_request" | "payment_confirmation" | "reservation_cancelled";
    title: string;
    message: string;
    payment_id?: number;
    reservation_id?: number;
    guest_id?: number;
    status: "unread" | "read";
    created_at: Date;
    updated_at: Date;
};