export type Payment = {
    id?: number;
    amount: number;
    guest_id: number;
    paymentdate: Date;
    paymentmethod: string;
    yoco_checkout_id: string;
    paymentstatus: 'created' | 'started' | 'processing' | 'completed' | 'failed' | 'cancelled' | 'succeeded' | 'overdue';
    transactionreference: string;
    created_at: Date;
    updated_at: Date;
};