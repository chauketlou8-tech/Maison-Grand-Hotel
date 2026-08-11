export default function roomTypeMap(type: string): string {
    switch (type) {
        case "ST":
            return "Standard";
        case "DE":
            return "Deluxe";
        case "EX":
            return "Executive";
        case "SU":
            return "Suite";
        case "PE":
            return "Penthouse";
        case "FA":
            return "Family";
        case "SI":
            return "Single";
        case "DO":
            return "Double";
        case "KI":
            return "King";
        case "QU":
            return "Queen";
        default:
            return "";
    }
}