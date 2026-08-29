export type Payment = {
    id?: number,
    amount: number,
    paymentDate: Date,
    paymentStatus: 'created'| 'started'| 'processing'| 'completed'| 'failed'| 'cancelled',
    transactionReference: string
}